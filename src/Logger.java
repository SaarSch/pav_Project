import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Logger {
    private static StringBuilder str = new StringBuilder();
    private static ArrayList<Integer> previousValues = new ArrayList<>();
    private static boolean firstRound = true;

    public static void print(String s) {
        System.out.print(s);
        str.append(s);
    }
    public static void init(String function_name) {
        print("\n\n*** Method: " + function_name + " ***");
    }

    public static void logCmd(String cmd) {
        print("\n" + cmd + ";");
    }

    public static void printStr(String str) {
        print(str);
    }

    public static void printLocal(int local, String name, boolean printDeltas) {
        firstPrint(name);
        print(local + " && ");
    }

    public static void printLocal(Object local, String name, boolean printDeltas) {
        firstPrint(name);
        if (null == local) {
            print("null");
            return;
        }
        print(local + "");

        int currentLocal = 0;
        Field fields[] = local.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().toString();
                String fieldName = field.getName();
                if (fieldType.equals("int")) {
                    int intValue = (int) field.get(local);
                    boolean print = true;
                    if (printDeltas) {
                        if (firstRound)
                            previousValues.add(intValue);
                        else {
                            if (previousValues.get(currentLocal) == intValue)
                                print = false;
                            else {
                                previousValues.remove(currentLocal);
                                previousValues.add(currentLocal, intValue);
                            }
                        }
                        currentLocal++;
                    }
                    if (print)
                        printLocal(intValue, fieldName, printDeltas);
                } else {
                    printLocal(field.get(local), fieldName, printDeltas);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        firstRound = false;
    }

    private static void firstPrint(String name) {
        if (name.contains("#LOCAL#"))
            print(name + "==");
        else
            print("\t" + name + ": ");
    }

    public static void dumpSpecToFile(String fileName) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fileName + ".spec", "UTF-8");
            writer.println(str.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
