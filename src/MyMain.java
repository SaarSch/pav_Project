import soot.PackManager;
import soot.Transform;

public class MyMain {

	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.CodeImplant", new CodeImplant()));
		soot.Main.main(args);
	}
}
