import soot.*;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.ArrayList;
import java.util.Map;

public class CodeImplant extends BodyTransformer {
    static SootClass loggerClass;
    static SootMethod init, logCmd, printIntLocal, printRefLocal, dumpSpecToFile;

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        if (body.getMethod().getName().equals("main") || body.getMethod().getName().equals("<init>") || body.getMethod().getName().equals("<clinit>")) {
            return;
        }
        loggerClass = Scene.v().getSootClass("Logger");
        init = loggerClass.getMethod("void init(java.lang.String)");
        logCmd = loggerClass.getMethod("void logCmd(java.lang.String)");
        printRefLocal = loggerClass.getMethod("void printLocal(java.lang.Object,java.lang.String)");
        printIntLocal = loggerClass.getMethod("void printLocal(int,java.lang.String)");
        dumpSpecToFile = loggerClass.getMethod("void dumpSpecToFile(java.lang.String)");

        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();

        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            //PrintLocals:
            for (Local local : locals) {
                soot.SootMethodRef printLocalMethod = null;
                if (local.getType().getClass().getName().equals("soot.IntType")) {
                    printLocalMethod = printIntLocal.makeRef();
                } else if (local.getType().getClass().getName().equals("soot.RefType")) {
                    printLocalMethod = printRefLocal.makeRef();
                }
                InvokeExpr invokePrintLocals = Jimple.v().newStaticInvokeExpr(printLocalMethod, local, StringConstant.v("#LOCAL#_" + local.getName()));
                Stmt invokePrintLocalsStmt = Jimple.v().newInvokeStmt(invokePrintLocals);
                stms.insertAfter(invokePrintLocalsStmt, stm);
            }
            // LogCmd:
            InvokeExpr invokeLogCmd = Jimple.v().newStaticInvokeExpr(logCmd.makeRef(), StringConstant.v(stm.toString()));
            Stmt invokeLogCmdStmt = Jimple.v().newInvokeStmt(invokeLogCmd);
            stms.insertAfter(invokeLogCmdStmt, stm);
        }

        // Init logger
        InvokeExpr invokeInit = Jimple.v().newStaticInvokeExpr(init.makeRef(), StringConstant.v(body.getMethod().getName()));
        Stmt invokeInitStmt = Jimple.v().newInvokeStmt(invokeInit);
        stms.insertBefore(invokeInitStmt, myStms.get(0));

        // Write to file:
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

