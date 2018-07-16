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
    static JavaEnv javaEnvObject;

    public static void init(String mmethodClassName, String sMethod, String envClassName) {
        str.append("example {\r\n" + "");
        try {
            Class<?> mmethodClass = Class.forName(mmethodClassName);
            Class<? extends JavaEnv> envClass = (Class<? extends JavaEnv>) Class.forName(envClassName);
            walker = new JavaHeapWalker(ReflectionUtils.getMethodByName(mmethodClass, sMethod), envClass, logger);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void logCmd(String cmd) {
        System.out.println("YAYAYAYAYA i'm inside logCmd!!!");
        System.out.println(cmd);
        str.append("\t" + cmd);
        str.append("\n");
    }

    public static void logEnv(JavaEnv env) {
        System.out.println("YAYAYAYAYA i'm inside logEnv!!!");
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
        str.append(" }\r\n" + "  ");
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
