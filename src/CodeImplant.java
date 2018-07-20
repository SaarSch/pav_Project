import soot.*;
import soot.jimple.*;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.ArrayList;
import java.util.Map;

public class CodeImplant extends BodyTransformer {
    boolean storeDeltas, logCommands;
    static SootClass loggerClass;
    static SootMethod init, logCmd, printIntLocal, printRefLocal, dumpSpecToFile, printStr;

    public CodeImplant(boolean storeDeltas, boolean logCommands) {
        this.storeDeltas = storeDeltas;
        this.logCommands = logCommands;
    }

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        if (body.getMethod().getName().equals("main") || body.getMethod().getName().equals("<init>") || body.getMethod().getName().equals("<clinit>")) {
            return;
        }
        loggerClass = Scene.v().getSootClass("Logger");
        init = loggerClass.getMethod("void init(java.lang.String)");
        logCmd = loggerClass.getMethod("void logCmd(java.lang.String)");
        printRefLocal = loggerClass.getMethod("void printLocal(java.lang.Object,java.lang.String,boolean)");
        printIntLocal = loggerClass.getMethod("void printLocal(int,java.lang.String,boolean)");
        dumpSpecToFile = loggerClass.getMethod("void dumpSpecToFile(java.lang.String)");
        printStr = loggerClass.getMethod("void printStr(java.lang.String)");

        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();

        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            addInvokeStmt(stms, stm, printStr.makeRef(), true, StringConstant.v("]"));
            //PrintLocals:
            for (Local local : locals) {
                SootMethodRef printLocalMethod = null;
                if (local.getType().getClass().getName().equals("soot.IntType")) {
                    printLocalMethod = printIntLocal.makeRef();
                } else if (local.getType().getClass().getName().equals("soot.RefType")) {
                    printLocalMethod = printRefLocal.makeRef();
                }
                addInvokeStmt(stms, stm, printLocalMethod, false, local, StringConstant.v("#LOCAL#_" + local.getName()), IntConstant.v(storeDeltas ? 1:0));
            }
            addInvokeStmt(stms, stm, printStr.makeRef(), false, StringConstant.v(" // ["));

            if (logCommands) {
                // LogCmd:
                addInvokeStmt(stms, stm, logCmd.makeRef(), false, StringConstant.v(stm.toString()));
            }
        }

        // Init logger
        addInvokeStmt(stms, myStms.get(0), init.makeRef(), true, StringConstant.v(body.getMethod().getName()));
        // Write to file:
        addInvokeStmt(stms, stms.getLast(), dumpSpecToFile.makeRef(), true, StringConstant.v("Test_Result"));

    }

    private void addInvokeStmt(PatchingChain<Unit> stms, Unit stm, SootMethodRef method, boolean before, Value... values) {
        InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(method, values);
        Stmt invokeStmt = Jimple.v().newInvokeStmt(invoke);
        if (before)
            stms.insertBefore(invokeStmt, stm);
        else
            stms.insertAfter(invokeStmt, stm);
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

