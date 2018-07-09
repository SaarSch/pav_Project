import jminor.JmStore;
import jminor.java.JavaEnv;
import jminor.java.JavaHeapWalker;

import java.io.PrintWriter;
import java.lang.reflect.Method;

public class Logger {
    static StringBuilder str = new StringBuilder();
    static private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());
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

    public static void dumpSpecToFile(String fileName) {

        PrintWriter writer;
        try {
            String res = Logger.getString();
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
