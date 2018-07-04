import soot.PackManager;
import soot.Transform;

public class Main {

	public static void main(String[] args) {
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.traceAnalysis", new CodeImplant()));
		soot.Main.main(args);
	}
}
