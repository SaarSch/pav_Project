import soot.Local;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

public class Logger {
    private static StringBuilder str = new StringBuilder();
    private static HashMap<String, Integer> currentState = new HashMap<>();
    private static boolean firstRound = true;

    public static void print(String s) {
        System.out.print(s);
        str.append(s);
    }

    public static void notifyFirstRoundFinished() {
        firstRound = false;
    }
    public static void init(String function_name) {
        print("\n\n*** Method: " + function_name + " ***");
        firstRound = true;
        currentState.clear();
    }
    public static void logCmd(String cmd) {
        print("\n" + cmd + ";");
    }
    public static void printStr(String str) {
        print(str);
    }

    public static void printLocal(int local, String name, boolean printDeltas, boolean isFirstLocal) {
        if (printDeltas) {
            if (!firstRound) {
                if (currentState.get(name) != local) {
                    firstPrint(name, isFirstLocal);
                    print(local + " ");
                }
                return;
            }
            currentState.put(name, local); // record previous state
        }
        firstPrint(name, isFirstLocal);
        print(local + " ");
    }

    public static void printLocal(Object local, String name, boolean printDeltas, boolean isFirstLocal) {
        firstPrint(name, isFirstLocal);
        if (null == local) {
            print("null");
            return;
        }
        print(local + " ");

        Field fields[] = local.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().toString();
                String fieldName = field.getName();
                if (fieldType.equals("int")) {
                    int intValue = (int) field.get(local);
                    printLocal(intValue, fieldName, printDeltas, isFirstLocal);
                } else {
                    printLocal(field.get(local), fieldName, printDeltas, isFirstLocal);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            //print("\n");
        }
    }

    private static void firstPrint(String name, boolean isFirstLocal) {
        print((isFirstLocal? "":"&& "));
        if (name.contains("#LOCAL#"))
            print(name.substring(8) + "==");
        else
            print(name + "-->");
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
