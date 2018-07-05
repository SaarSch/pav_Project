package pav;

import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;

import java.io.File;

public class MyMain {

	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.CodeImplant", new CodeImplant()));

        String s = Scene.v().getSootClassPath() + File.pathSeparator + "C:\\Users\\saars\\IdeaProjects\\PexynLogger\\out\\production\\PexynLogger";
        Scene.v().setSootClassPath(s);
        Scene.v().addBasicClass("Logger", SootClass.HIERARCHY);

		soot.Main.main(args);
	}
}
