import java.io.PrintWriter;
import java.lang.reflect.Field;

public class Logger {
    private static StringBuilder str = new StringBuilder();

    public static void init(String function_name) {
        System.out.print("\n\n*** " + function_name + " ***");
        str.append("\n\n*** " + function_name + " ***");
    }

    public static void logCmd(String cmd) {
        System.out.print("\nlogCmd: " + cmd);
        str.append("\nlogCmd: " + cmd);
    }

    public static void printLocal(int local, String name) {
        firstPrint(name);
        System.out.print(local);
        str.append(local);
    }

    public static void printLocal(Object local, String name) {
        firstPrint(name);
        if (null == local) {
            System.out.print("null");
            str.append("null");
            return;
        }
        System.out.print(local);
        str.append(local);
        Field fields[] = local.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().toString();
                String fieldName = field.getName();
                if (fieldType.equals("int")) {
                    printLocal((int) field.get(local), fieldName);
                } else {
                    printLocal(field.get(local), fieldName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void firstPrint(String name) {
        if (name.contains("#LOCAL#")) {
            System.out.print("\n" + name + "--> ");
            str.append("\n" + name + "--> ");
        } else {
            System.out.print("\t" + name + ": ");
            str.append("\t" + name + ": ");
        }
    }

    public static void dumpSpecToFile(String fileName) {
        PrintWriter writer;
        try {
            String res = Logger.getString();
            writer = new PrintWriter(fileName + ".spec", "UTF-8");
            writer.println(res);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getString() {
        return str.toString();
    }
}
