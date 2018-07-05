package pav;

import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MyMain {

	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.CodeImplant", new CodeImplant()));
		soot.Main.main(args);
	}

    public static class CodeImplant extends BodyTransformer {
        @Override
        protected void internalTransform(Body body, String s, Map<String, String> map) {
            SootMethod method = body.getMethod();
            SootClass envClass = new SootClass(method.getName() + "Env", Modifier.STATIC | Modifier.PUBLIC);
            envClass.setSuperclass(Scene.v().getSootClass("jminor.java.JavaEnv"));
            for (Local local : body.getLocals()) {
                envClass.addField(new SootField(local.getName(), local.getType(), Modifier.PUBLIC));
            }
            int count = 0;
            for (Type parameterType : method.getParameterTypes()) {
                envClass.addField(new SootField("param" + count, parameterType, Modifier.PUBLIC));
                count++;
            }

            Scene.v().addClass(envClass);

            PatchingChain<Unit> units = body.getUnits();
            LocalGenerator generator = new LocalGenerator(body);

            // add 'PexynLogger.init(ReflectionUtils.getMethodByName(Test.class, "A"), AEnv.class);':

            // add 'env = new XXXEnv();':
            Value env = generator.generateLocal(envClass.getType());
            AssignStmt stmt = Jimple.v().newAssignStmt(env, Jimple.v().newParameterRef(envClass.getType(), 0));
            units.insertBefore(stmt, units.getFirst());

            // add 'env.xxx = xxx;' after each line of code:
            for (Unit unit : units) {
                List<Unit> toInsert = new LinkedList<>();
                for (Local local : body.getLocals()) {
                    InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(local.getName()).makeRef());
                    toInsert.add(Jimple.v().newAssignStmt(fieldRef, local));
                }
                for (Local paramLocal : body.getParameterLocals()) {
                    InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(paramLocal.getName()).makeRef());
                    toInsert.add(Jimple.v().newAssignStmt(fieldRef, paramLocal));
                }
                body.getUnits().insertAfter(toInsert, unit);
            }

            // add last units
        }
    }
}
