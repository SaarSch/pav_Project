import bgu.cs.util.ReflectionUtils;
import jminor.JmStore;
import jminor.java.JavaEnv;
import jminor.java.JavaHeapWalker;

import java.io.PrintWriter;
import java.lang.reflect.Method;

public class Logger {
    static StringBuilder str = new StringBuilder();
    static private final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Logger.class.getName());
    static JavaHeapWalker walker = null;

    public static void init(Class mmethodClass, String sMethod, Class envClass) {
        walker = new JavaHeapWalker(ReflectionUtils.getMethodByName(mmethodClass, sMethod), envClass, logger);
    }

    public static void logCmd(String cmd) {
        System.out.println(cmd);
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
