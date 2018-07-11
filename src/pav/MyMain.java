package pav;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

import java.io.File;

public class MyMain {

	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.CodeImplant", new CodeImplant()));

        String s = Scene.v().getSootClassPath() + File.pathSeparator + "C:\\Users\\Nevo2\\Documents\\אוניברסיטה\\רומן\\pav_Project_NEW\\out\\production\\pav_Project_NEW";
        Scene.v().setSootClassPath(s);
		System.setProperty("sun.boot.class.path", s);
		System.setProperty("java.ext.dirs", s);
		CodeImplant.mmethodClassName = args[args.length - 1];
        Scene.v().addBasicClass("Logger", SootClass.SIGNATURES);
		soot.Main.main(args);
	}
}
