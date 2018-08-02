import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;

import java.util.Arrays;

public class Project {

    public static void main(String[] args) {
        String instrumentedMethod;
        boolean storeDeltas = false, logCommands = true;
        int extraArgs = 0;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-deltas")) {
                storeDeltas = true;
                extraArgs++;
            }
            if (args[i].equals("-nologcmd")) {
                logCommands = false;
                extraArgs++;
            }
        }
        instrumentedMethod = args[args.length - extraArgs - 1];
        PackManager.v().getPack("jtp").add(new Transform("jtp.CodeImplant", new CodeImplant(instrumentedMethod, storeDeltas, logCommands)));
        String s = Scene.v().getSootClassPath();
        Scene.v().setSootClassPath(s);
        System.setProperty("sun.boot.class.path", s);
        System.setProperty("java.ext.dirs", s);
        Scene.v().addBasicClass("Logger", SootClass.SIGNATURES);
        String[] sootArgs = Arrays.copyOfRange(args, 0, args.length - extraArgs - 1); // -1 for the method name
        soot.Main.main(sootArgs);
    }
}
