import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;

import java.io.File;

public class MyMain {

    public static void main(String[] args) {
        PackManager.v().getPack("jtp").add(new Transform("jtp.CodeImplant", new CodeImplant()));
        String s = Scene.v().getSootClassPath();
        Scene.v().setSootClassPath(s);
        System.setProperty("sun.boot.class.path", s);
        System.setProperty("java.ext.dirs", s);
        Scene.v().addBasicClass("Logger", SootClass.SIGNATURES);
        soot.Main.main(args);
    }
}
