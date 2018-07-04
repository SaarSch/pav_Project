import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import jminor.JmStore;
import jminor.java.JavaEnv;
import jminor.java.JavaHeapWalker;

public class PexynLogger {
	static StringBuilder str = new StringBuilder();
	static private final Logger logger = Logger.getLogger(PexynLogger.class.getName());
	static JavaHeapWalker walker = null;
	
	public static void init(Method m, Class c) {
		walker = new JavaHeapWalker(m, c, logger);
	}
	public static void logCmd(String cmd) { 
		str.append(cmd);
		str.append("\n");
	}
	
	public static void logEnv(JavaEnv env) {
		if (walker != null) {
			JmStore store;
			try {
				store = walker.walk(env);
				str.append(store.toString());
				str.append("\n");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void end(String fileName){

		PrintWriter writer;
		try {
			String res = PexynLogger.getString();
			System.out.println(res);
			writer = new PrintWriter(fileName + ".spec", "UTF-8");
			writer.println(res);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getString() {
		return str.toString();
	}
}
