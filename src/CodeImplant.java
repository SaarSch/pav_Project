import bgu.cs.util.Pair;
import soot.*;
import soot.jimple.*;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.ArrayList;
import java.util.Map;

public class CodeImplant extends BodyTransformer {
    boolean storeDeltas, logCommands;
    static SootClass loggerClass;
    static SootMethod init, logCmd, printIntLocal, printRefLocal, dumpSpecToFile, printStr, notifyFirstRoundFinished;

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
        printRefLocal = loggerClass.getMethod("void printLocal(java.lang.Object,java.lang.String,boolean,boolean)");
        printIntLocal = loggerClass.getMethod("void printLocal(int,java.lang.String,boolean,boolean)");
        dumpSpecToFile = loggerClass.getMethod("void dumpSpecToFile(java.lang.String)");
        printStr = loggerClass.getMethod("void printStr(java.lang.String)");
        notifyFirstRoundFinished = loggerClass.getMethod("void notifyFirstRoundFinished()");

        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();

        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);
        boolean firstRound = true;

        for (Unit stm : myStms) {
            if (firstRound) {
                firstRound = false;
                addInvokeStmt(stms, stm, notifyFirstRoundFinished.makeRef(), false);
            }
            addInvokeStmt(stms, stm, printStr.makeRef(), false, StringConstant.v("]"));
            //PrintLocals:
            ArrayList<Pair<Local, SootMethodRef>> localsToLog = new ArrayList<>();
            for (Local local : locals) {
                SootMethodRef printLocalMethod = null;
                if (local.getType().getClass().getName().equals("soot.IntType")) {
                    printLocalMethod = printIntLocal.makeRef();
                } else if (local.getType().getClass().getName().equals("soot.RefType")) {
                    printLocalMethod = printRefLocal.makeRef();
                }
                localsToLog.add(new Pair(local, printLocalMethod));
            }
            for (int i=localsToLog.size()-1; i >= 0; i--) {
                Pair<Local, SootMethodRef> localPair = localsToLog.get(i);
                addInvokeStmt(stms, stm, localPair.second, false, localPair.first, StringConstant.v("#LOCAL#_" + localPair.first.getName()), IntConstant.v(storeDeltas ? 1:0), IntConstant.v(i==localsToLog.size()-1 ? 1:0));
            }
            addInvokeStmt(stms, stm, printStr.makeRef(), false, StringConstant.v(" // ["));

            if (logCommands)
                addInvokeStmt(stms, stm, logCmd.makeRef(), false, StringConstant.v(stm.toString())); // LogCmd
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

