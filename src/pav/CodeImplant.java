package pav;

import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.ArrayList;
import java.util.Map;

public class CodeImplant extends BodyTransformer {
    static SootClass counterClass;
    static SootMethod logCmd, logEnv, dumpSpecToFile, getString, init;
    static String mmethodClassName;

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        if (body.getMethod().getName().equals("main") || body.getMethod().getName().equals("<init>")) {
            return;
        }
        counterClass = Scene.v().getSootClass("Logger");
        init = counterClass.getMethod("void init(java.lang.Class,java.lang.String,java.lang.Class)");
        logCmd = counterClass.getMethod("void logCmd(java.lang.String)");
        logEnv = counterClass.getMethod("void logEnv(jminor.java.JavaEnv)");
        dumpSpecToFile = counterClass.getMethod("void dumpSpecToFile(java.lang.String)");
        getString = counterClass.getMethod("java.lang.String getString()");

        Chain<Local> locals = new HashChain<>(body.getLocals());

        SootMethod method = body.getMethod();
        SootClass envClass = new SootClass(method.getName() + "_Env", Modifier.STATIC | Modifier.PUBLIC);
        envClass.setSuperclass(Scene.v().getSootClass("jminor.java.JavaEnv"));

        LocalGenerator generator = new LocalGenerator(body);
        Local envClassType = generator.generateLocal(envClass.getType());

        for (Local local : locals) {
            envClass.addField(new SootField(local.getName(), local.getType(), Modifier.PUBLIC));
        }

        Scene.v().addClass(envClass);
        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            // add 'PexynLogger.logEnv(env);'
            InvokeExpr invokeLogCEnv = Jimple.v().newStaticInvokeExpr(logEnv.makeRef(), ClassConstant.v(envClass.getName()));
            Stmt invokeLogCEnvStmt = Jimple.v().newInvokeStmt(invokeLogCEnv);
            stms.insertAfter(invokeLogCEnvStmt, stm);

            // add 'env.xxx = xxx;' after each line of code:
            for (Local local : locals) {
                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(envClassType, envClass.getFieldByName(local.getName()).makeRef());
                stms.insertAfter(Jimple.v().newAssignStmt(fieldRef, local), stm);
            }
            // add 'PexynLogger.logCmd();'
            InvokeExpr invokeLogCmd = Jimple.v().newStaticInvokeExpr(logCmd.makeRef(), StringConstant.v(stm.toString()));
            Stmt invokeLogCmdStmt = Jimple.v().newInvokeStmt(invokeLogCmd);
            stms.insertAfter(invokeLogCmdStmt, stm);
        }

        // add 'env = new XXX_Env();':
        AssignStmt initEnvClassStmt = Jimple.v().newAssignStmt(envClassType, Jimple.v().newNewExpr(envClass.getType()));
        stms.insertBefore(initEnvClassStmt, myStms.get(0));

        // add 'PexynLogger.init(ReflectionUtils.getMethodByName(Test.class, "A"), AEnv.class);':
        InvokeExpr invokeInit = Jimple.v().newStaticInvokeExpr(init.makeRef(), ClassConstant.v(mmethodClassName), StringConstant.v(body.getMethod().getName()), ClassConstant.v(envClass.getName()));
        Stmt invokeInitStmt = Jimple.v().newInvokeStmt(invokeInit);
        stms.insertBefore(invokeInitStmt, myStms.get(0));

        // add 'PexynLogger.dumpSpecToFile("Test_Result");':
        InvokeExpr invokeDumpSpecToFile = Jimple.v().newStaticInvokeExpr(dumpSpecToFile.makeRef(), StringConstant.v("Test_Result"));
        Stmt invokeDumpSpecToFileStmt = Jimple.v().newInvokeStmt(invokeDumpSpecToFile);
        stms.insertAfter(invokeDumpSpecToFileStmt, stms.getLast());
    }

    private ArrayList<Unit> generateMyStms(PatchingChain<Unit> stms) {
        ArrayList<Unit> ans = new ArrayList<>(stms);
        int numOfStmToRemove = 0;
        Unit currentStm = stms.getFirst();
        while (currentStm.getUseAndDefBoxes().get(1).toString().contains("@")) {
            numOfStmToRemove++;
            currentStm = stms.getSuccOf(currentStm);
        }
        for (int i = 0; i < numOfStmToRemove; i++) {
            ans.remove(0);
        }
        return ans;
    }
}

