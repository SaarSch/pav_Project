import soot.*;
import soot.jimple.*;
import soot.jimple.internal.JInvokeStmt;
import soot.util.Chain;
import soot.util.HashChain;

import java.util.*;

public class CodeImplant extends BodyTransformer {
    static SootClass loggerClass;
    static SootMethod init, logCmd, printIntLocal, printRefLocal, dumpSpecToFile, addToSpec;
    static String instrumentedMethod;
    static boolean storeDeltas, logCommands;
    static HashMap<Local, Boolean> initialization;

    public CodeImplant(String instrumentedMethod, boolean storeDeltas, boolean logCommands) {
        CodeImplant.instrumentedMethod = instrumentedMethod;
        CodeImplant.storeDeltas = storeDeltas;
        CodeImplant.logCommands = logCommands;
        CodeImplant.initialization = new HashMap<>();
    }

    @Override
    protected void internalTransform(Body body, String s, Map map) {
        String methodName = body.getMethod().getName();
        if (methodName.equals("main")) {
            List<Unit> methodCalls = new ArrayList<>();
            for (Unit stm : body.getUnits())
                if (stm.toString().contains(instrumentedMethod))
                    methodCalls.add(stm);
            for (Unit stm : methodCalls) {
                addInvokeStmt(body.getUnits(), stm, addToSpec.makeRef(), true, StringConstant.v("\n  example {\n    "));
                addInvokeStmt(body.getUnits(), stm, addToSpec.makeRef(), false, StringConstant.v("\n  }\n"));
            }
            // Write to file
            addInvokeStmt(body.getUnits(), body.getUnits().getLast(), dumpSpecToFile.makeRef(), true, StringConstant.v("Test_Result"));
            return;
        }
        if (!methodName.equals(instrumentedMethod)||
             methodName.equals("<init>") || methodName.equals("<clinit>")) {
            return;
        }

        loggerClass = Scene.v().getSootClass("Logger");
        init = loggerClass.getMethod("void init(java.lang.String,boolean,boolean)");
        logCmd = loggerClass.getMethod("void logCmd(java.lang.String)");
        printRefLocal = loggerClass.getMethod("void printLocal(java.lang.Object,java.lang.String,boolean)");
        printIntLocal = loggerClass.getMethod("void printLocal(int,java.lang.String)");
        dumpSpecToFile = loggerClass.getMethod("void dumpSpecToFile(java.lang.String)");
        addToSpec = loggerClass.getMethod("void addToSpec(java.lang.String)");

        List<Local> locals = getLocals(body);
        int argCount = body.getMethod().getParameterCount();
        int localCount = body.getLocalCount() - 1; // without counting 'this'
        for (int i = 0; i < localCount; i++) {
            initialization.put(locals.get(i), false);
            if (i >= localCount - argCount) // the arguments are initialized
                initialization.put(locals.get(i), true);
        }
        List<Local> methodArgs = locals.subList(localCount - argCount, localCount);
        PatchingChain<Unit> stms = body.getUnits();
        ArrayList<Unit> myStms = generateMyStms(stms);

        for (Unit stm : myStms) {
            if (!(stm instanceof IfStmt)) { // do not print if statements
                if (stm instanceof AssignStmt) {
                    Value leftOp = ((AssignStmt) stm).getLeftOp();
                    if (leftOp instanceof Local)
                        if (initialization.containsKey(leftOp)) initialization.put((Local) leftOp, true);
                }
                // Print locals
                printLocals(locals, stms, stm, true);
                // Log command
                addInvokeStmt(stms, stm, logCmd.makeRef(), false, StringConstant.v(stm.toString()));
            }
        }

        List<String> argumentNames = new ArrayList<>();
        for (int i=localCount - argCount; i<localCount; i++)
            argumentNames.add(locals.get(i).getName());
        String signature = getSignature(body.getMethod(), body.getUnits().getLast(), argumentNames);
        // Init logger
        Unit LoggerInitStmt = addInvokeStmt(stms, myStms.get(0), init.makeRef(), true, StringConstant.v(signature), IntConstant.v(storeDeltas ? 1 : 0), IntConstant.v(logCommands ? 1 : 0));

        for (Unit stm : stms) {
            if (stm instanceof GotoStmt) {
                Unit target = ((GotoStmt)stm).getTarget();
                if (target instanceof JInvokeStmt)
                    if (target.toString().contains("init")) // Prevent a loop that goes back to init instead of the real first statement
                        ((GotoStmt) stm).setTarget(myStms.get(0));
            }
        }
        // Print method arguments as initial method state
        printLocals(methodArgs, stms, LoggerInitStmt, false);

    }

    private String getSignature(SootMethod method, Unit returnStmt, List<String> argumentNames) {
        ReturnStmt castedReturnStmt = null;
        if (returnStmt instanceof  ReturnStmt)
            castedReturnStmt = (ReturnStmt)returnStmt;
        String res = "";
        res += method.getName() + "(";
        for (int i=0; i<method.getParameterCount() && i<argumentNames.size(); i++) {
            res += "mut " + argumentNames.get(argumentNames.size()-1-i) + ":" + method.getParameterType(i) + ", ";
        }
        res = res.substring(0, res.length()-2);
        res += ") -> (";
        if (castedReturnStmt != null) {
            Local returnedLocal = (Local)castedReturnStmt.getOp();
            res += (returnedLocal.getName() + ":" + returnedLocal.getType());
        }
        res += ")";
        return res;
    }

    private void printLocals(List<Local> locals, PatchingChain<Unit> stms, Unit stm, boolean asComment) {
        addInvokeStmt(stms, stm, addToSpec.makeRef(), false, StringConstant.v("]"));

        for (Local local : locals) {
            if (initialization.containsKey(local) && initialization.get(local)) {
                if (!local.getName().startsWith("$")) {
                    if ((local.getType() instanceof IntType) || (local.getType() instanceof ByteType)) {
                        addInvokeStmt(stms, stm, printIntLocal.makeRef(), false, local, StringConstant.v("#LOCAL#_" + local.getName()));
                    } else if (local.getType() instanceof RefType) {
                        addInvokeStmt(stms, stm, printRefLocal.makeRef(), false, local, StringConstant.v("#LOCAL#_" + local.getName()), IntConstant.v(0));
                    }
                }
            }
        }

        addInvokeStmt(stms, stm, addToSpec.makeRef(), false, StringConstant.v(asComment ? " // [" : "["));
    }

    private List<Local> getLocals(Body body) {
        Chain<Local> locals = new HashChain<>(body.getLocals());
        locals.removeFirst();
        List<Local> reversedLocals = new ArrayList<>(locals);
        Collections.reverse(reversedLocals);
        return reversedLocals;
    }

    private Unit addInvokeStmt(PatchingChain<Unit> stms, Unit stm, SootMethodRef method, boolean before, Value... values) {
        InvokeExpr invoke = Jimple.v().newStaticInvokeExpr(method, values);
        Stmt invokeStmt = Jimple.v().newInvokeStmt(invoke);
        if (before) stms.insertBefore(invokeStmt, stm);
        else stms.insertAfter(invokeStmt, stm);
        return invokeStmt;
    }

    private ArrayList<Unit> generateMyStms(PatchingChain<Unit> stms) {
        ArrayList<Unit> ans = new ArrayList<>(stms);
        int numOfStmToRemove = 0;
        Unit currentStm = stms.getFirst();
        while (currentStm.getUseAndDefBoxes().size() >= 2 && currentStm.getUseAndDefBoxes().get(1).toString().contains("@")) {
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

