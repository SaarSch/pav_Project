package pav;

import javafx.util.Pair;
import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.Jimple;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CodeImplant extends BodyTransformer {
    static SootClass counterClass;
    static SootMethod logCmd, logEnv, dumpSpecToFile, getString;

    @Override
    protected void internalTransform(Body body, String s, Map<String, String> map) {
        counterClass = Scene.v().getSootClass("Logger");
        logCmd = counterClass.getMethod("void logCmd(java.lang.String)");
        logEnv = counterClass.getMethod("void logEnv(jminor.java.JavaEnv)");
        dumpSpecToFile = counterClass.getMethod("void dumpSpecToFile(java.lang.String)");
        getString = counterClass.getMethod("java.lang.string getString()");

        Chain<Local> locals = new HashChain<>(body.getLocals());

        SootMethod method = body.getMethod();
        SootClass envClass = new SootClass(method.getName() + "_Env", Modifier.STATIC | Modifier.PUBLIC);
        //envClass.setSuperclass(Scene.v().getSootClass("jminor.java.JavaEnv"));
        for (Local local : locals) {
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
        Local env = generator.generateLocal(envClass.getType());
        AssignStmt stmt = Jimple.v().newAssignStmt(env, Jimple.v().newNewExpr(envClass.getType()));
        units.insertBefore(stmt, units.getFirst());

        // add 'env.xxx = xxx;' after each line of code:
        List<Pair<List<Unit>, Unit>> newStatements = new LinkedList<>();
        for (Unit unit : units) {
            // add 'PexynLogger.logCmd();'

            List<Unit> toInsert = new LinkedList<>();
            for (Local local : locals) {
                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(local.getName()).makeRef());
                toInsert.add(Jimple.v().newAssignStmt(fieldRef, local));
            }
            for (Local paramLocal : body.getParameterLocals()) {
                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(paramLocal.getName()).makeRef());
                toInsert.add(Jimple.v().newAssignStmt(fieldRef, paramLocal));
            }
            newStatements.add(new Pair<>(toInsert, unit));
        }

        for (Pair<List<Unit>, Unit> p : newStatements) {
            body.getUnits().insertAfter(p.getKey(), p.getValue());
        }

        // add last units
    }
}