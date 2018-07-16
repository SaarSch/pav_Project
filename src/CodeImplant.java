import jminor.java.JavaEnv;
import soot.*;
import soot.javaToJimple.LocalGenerator;
import soot.jimple.*;
import soot.options.Options;
import soot.util.Chain;
import soot.util.HashChain;
import soot.util.JasminOutputStream;

import java.io.*;
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
        init = counterClass.getMethod("void init(java.lang.String,java.lang.String,java.lang.String)");
        logCmd = counterClass.getMethod("void logCmd(java.lang.String)");
        logEnv = counterClass.getMethod("void logEnv(jminor.java.JavaEnv)");
        dumpSpecToFile = counterClass.getMethod("void dumpSpecToFile(java.lang.String)");
        getString = counterClass.getMethod("java.lang.String getString()");

        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();

        SootMethod method = body.getMethod();
        SootClass envClass = new SootClass(method.getName() + "_Env", Modifier.PUBLIC);
        envClass.setSuperclass(Scene.v().getSootClass("jminor.java.JavaEnv"));

        LocalGenerator generator = new LocalGenerator(body);
        Local envClassLocal = generator.generateLocal(envClass.getType());

        for (Local local : locals) {
            envClass.addField(new SootField(local.getName(), local.getType(), Modifier.PUBLIC));
        }
        Scene.v().addClass(envClass);


        String fileName = SourceLocator.v().getFileNameFor(envClass, Options.output_format_class);
        OutputStream streamOut = null;
        try {
            streamOut = new JasminOutputStream(new FileOutputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));

        JasminClass jasminClass = new soot.jimple.JasminClass(envClass);
        jasminClass.print(writerOut);
        writerOut.flush();
        try {
            streamOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            // add 'PexynLogger.logEnv(env);'
            InvokeExpr invokeLogCEnv = Jimple.v().newStaticInvokeExpr(logEnv.makeRef(), envClassLocal);
            Stmt invokeLogCEnvStmt = Jimple.v().newInvokeStmt(invokeLogCEnv);
            stms.insertAfter(invokeLogCEnvStmt, stm);

            // add 'env.xxx = xxx;' after each line of code:
            for (Local local : locals) {
                InstanceFieldRef fieldRef = Jimple.v().newInstanceFieldRef(envClassLocal, envClass.getFieldByName(local.getName()).makeRef());
                stms.insertAfter(Jimple.v().newAssignStmt(fieldRef, local), stm);
            }
            // add 'PexynLogger.logCmd();'
            InvokeExpr invokeLogCmd = Jimple.v().newStaticInvokeExpr(logCmd.makeRef(), StringConstant.v(stm.toString()));
            Stmt invokeLogCmdStmt = Jimple.v().newInvokeStmt(invokeLogCmd);
            stms.insertAfter(invokeLogCmdStmt, stm);
        }

        // add 'env = new XXX_Env();':
        AssignStmt initEnvClassStmt = Jimple.v().newAssignStmt(envClassLocal, Jimple.v().newNewExpr(envClass.getType()));
        stms.insertBefore(initEnvClassStmt, myStms.get(0));

        // add 'PexynLogger.init(ReflectionUtils.getMethodByName(Test.class, "A"), AEnv.class);':
        InvokeExpr invokeInit = Jimple.v().newStaticInvokeExpr(init.makeRef(), StringConstant.v(mmethodClassName), StringConstant.v(body.getMethod().getName()), StringConstant.v(envClass.getName()));
        Stmt invokeInitStmt = Jimple.v().newInvokeStmt(invokeInit);
        stms.insertBefore(invokeInitStmt, myStms.get(0));

        // add 'PexynLogger.dumpSpecToFile("Test_Result");':
        InvokeExpr invokeDumpSpecToFile = Jimple.v().newStaticInvokeExpr(dumpSpecToFile.makeRef(), StringConstant.v("Test_Result"));
        Stmt invokeDumpSpecToFileStmt = Jimple.v().newInvokeStmt(invokeDumpSpecToFile);
        stms.insertBefore(invokeDumpSpecToFileStmt, stms.getLast());
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
        ans.remove(ans.size() - 1);
        return ans;
    }
}

