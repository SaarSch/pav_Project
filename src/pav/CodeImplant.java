package pav;

import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.*;

public class CodeImplant extends BodyTransformer {
    static SootClass counterClass;
    static SootMethod logCmd, logEnv, dumpSpecToFile, getString;

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        counterClass = Scene.v().getSootClass("Logger");
        logCmd = counterClass.getMethod("void logCmd(java.lang.String)");
        logEnv = counterClass.getMethod("void logEnv(jminor.java.JavaEnv)");
        dumpSpecToFile = counterClass.getMethod("void dumpSpecToFile(java.lang.String)");
        getString = counterClass.getMethod("java.lang.String getString()");

        Chain<Local> locals = new HashChain<>(body.getLocals());

        SootMethod method = body.getMethod();
        SootClass envClass = new SootClass(method.getName() + "_Env", Modifier.STATIC | Modifier.PUBLIC);
        //envClass.setSuperclass(Scene.v().getSootClass("jminor.java.JavaEnv"));
        for (Local local : locals) {
            envClass.addField(new SootField(local.getName(), local.getType(), Modifier.PUBLIC));
        }
//        int count = 0;
        method.getParameterTypes();
//        for (Type parameterType : method.getParameterTypes()) {
//            envClass.addField(new SootField("param" + count, parameterType, Modifier.PUBLIC));
//            count++;
//        }
//
        Scene.v().addClass(envClass);
        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = new ArrayList<>(stms);
//
//        // add 'PexynLogger.init(ReflectionUtils.getMethodByName(Test.class, "A"), AEnv.class);':
        for (Unit stm : myStms) {
            InvokeExpr invokeLogCmd = Jimple.v().newStaticInvokeExpr(logCmd.makeRef(), StringConstant.v(stm.toString()));
            Stmt invokeLogCmdStmt = Jimple.v().newInvokeStmt(invokeLogCmd);
            stms.insertAfter(invokeLogCmdStmt, stm);
            // add 'PexynLogger.logCmd();'
//            InvokeExpr invokeLogCmd = Jimple.v().newStaticInvokeExpr(logCmd.makeRef(), StringConstant.v(stm.toString()));
//            Stmt invokeLogCmdStmt = Jimple.v().newInvokeStmt(invokeLogCmd);
//            stms.insertAfter(invokeLogCmdStmt, stm);
//
//            // add 'env.xxx = xxx;' after each line of code:
//            for (Local local : locals) {
//                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(local.getName()).makeRef());
//                toInsert.add(Jimple.v().newAssignStmt(fieldRef, local));
//            }
//            for (Local paramLocal : body.getParameterLocals()) {
//                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(env, envClass.getFieldByName(paramLocal.getName()).makeRef());
//                toInsert.add(Jimple.v().newAssignStmt(fieldRef, paramLocal));
//            }
//
//            // add 'PexynLogger.logEnv(env);'
//            InvokeExpr invokeLogCEnv = Jimple.v().newStaticInvokeExpr(logEnv.makeRef(), env);
//            //toInsert.add(invokeLogCEnv);
//
//            newStatements.add(new Pair<>(toInsert, unit));
        }

        // add 'env = new XXX_Env();':
        LocalGenerator generator = new LocalGenerator(body);
        Local envClassType = generator.generateLocal(envClass.getType());
        AssignStmt initEnvClassStmt = Jimple.v().newAssignStmt(envClassType, Jimple.v().newNewExpr(envClass.getType()));
        stms.insertBefore(initEnvClassStmt, stms.getFirst());
        int ab = 24;

//        for (Pair<List<Unit>, Unit> p : newStatements) {
//            body.getUnits().insertAfter(p.getKey(), p.getValue());
//        }
//
//        add last units
    }
}