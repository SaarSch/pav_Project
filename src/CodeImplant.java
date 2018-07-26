import soot.*;
import soot.jimple.*;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.*;

public class CodeImplant extends BodyTransformer {
    static SootClass loggerClass;
    static SootMethod init, logCmd, printIntLocal, printRefLocal, dumpSpecToFile, addToSpec;
    static boolean storeDeltas, logCommands;

    public CodeImplant(boolean storeDeltas, boolean logCommands) {
        CodeImplant.storeDeltas = storeDeltas;
        CodeImplant.logCommands = logCommands;
    }

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        if (body.getMethod().getName().equals("main") || body.getMethod().getName().equals("<init>") || body.getMethod().getName().equals("<clinit>")) {
            return;
        }
        loggerClass = Scene.v().getSootClass("Logger");
        init = loggerClass.getMethod("void init(java.lang.String,boolean,boolean)");
        logCmd = loggerClass.getMethod("void logCmd(java.lang.String)");
        printRefLocal = loggerClass.getMethod("void printLocal(java.lang.Object,java.lang.String)");
        printIntLocal = loggerClass.getMethod("void printLocal(int,java.lang.String)");
        dumpSpecToFile = loggerClass.getMethod("void dumpSpecToFile(java.lang.String)");
        addToSpec = loggerClass.getMethod("void addToSpec(java.lang.String)");

        List<Local> locals = getLocals(body);
        int argCount = body.getMethod().getParameterCount();
        int localCount = body.getLocalCount()-1; // without counting 'this'
        List<Local> methodArgs = locals.subList(localCount - argCount, localCount);
        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            // Print locals
            printLocals(locals, stms, stm, true);
            // Log command
            addInvokeStmt(stms, stm, logCmd.makeRef(), false, StringConstant.v(stm.toString()));
        }

        // Init logger
        addInvokeStmt(stms, myStms.get(0), init.makeRef(), true, StringConstant.v(body.getMethod().getName()), IntConstant.v(storeDeltas ? 1 : 0), IntConstant.v(logCommands ? 1 : 0));
        // Print method arguments as initial method state
        printLocals(methodArgs, stms, myStms.get(0), false);
        // Write to file:
        addInvokeStmt(stms, stms.getLast(), dumpSpecToFile.makeRef(), true, StringConstant.v("Test_Result"));
    }

    private void printLocals(List<Local> locals, PatchingChain<Unit> stms, Unit stm, boolean asComment) {
        addInvokeStmt(stms, stm, addToSpec.makeRef(), false, StringConstant.v(" ]"));

        for (Local local : locals) {
            SootMethodRef printLocalMethod = null;
            if (local.getType().getClass().getName().equals("soot.IntType") ||
                    local.getType().getClass().getName().equals("soot.ByteType")) {
                printLocalMethod = printIntLocal.makeRef();
            } else if (local.getType().getClass().getName().equals("soot.RefType")) {
                printLocalMethod = printRefLocal.makeRef();
            }
            addInvokeStmt(stms, stm, printLocalMethod, false, local, StringConstant.v("#LOCAL#_" + local.getName()));
        }

        addInvokeStmt(stms, stm, addToSpec.makeRef(), false, StringConstant.v(asComment? " // [":"["));
    }

    private List<Local> getLocals(Body body) {
        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();
        List<Local> reversedLocals = new ArrayList<>(locals);
        Collections.reverse(reversedLocals);
        return reversedLocals;
    }

    private void addInvokeStmt(PatchingChain<Unit> stms, Unit stm, SootMethodRef method, boolean before, Value... values) {
        InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(method, values);
        Stmt invokeStmt = Jimple.v().newInvokeStmt(invoke);
        if (before) stms.insertBefore(invokeStmt, stm);
        else stms.insertAfter(invokeStmt, stm);
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

