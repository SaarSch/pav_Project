import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;

public class Logger {
    private static StringBuilder str = new StringBuilder();
    private static HashMap<String, StringBuilder> localNameToStr = new HashMap<>();
    private static StringBuilder currentLocalStr = new StringBuilder();
    private static String currentLocalName = "";
    public static boolean storeDeltas = false;
    public static boolean logCommands = true;

    public static void addToSpec(String s) {
        if (s.equals("]")) {
            handleNewLocals("", true);
        }
        str.append(s);
    }

    public static void init(String functionName, boolean storeDeltas, boolean logCommands) {
        addToSpec("\n\n*** Method: " + functionName + " ***");
        Logger.storeDeltas = storeDeltas;
        Logger.logCommands = logCommands;
        clearVars();
    }

    public static void logCmd(String cmd) {
        if (logCommands) {
            addToSpec("\n" + cmd + ";");
        }
    }

    public static void printLocal(int localValue, String localName) {
        handleNewLocals(localName, false);
        currentLocalStr.append(" " + localName + " = " + localValue);
    }

    public static void printLocal(Object localValue, String localName) {
        handleNewLocals(localName, false);
        if (null == localValue) {
            currentLocalStr.append(" " + localName + " = " + "null");
            return;
        }
        currentLocalStr.append(" " + localName + " = " + localValue);
        Field fields[] = localValue.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().toString();
                String fieldName = field.getName();
                if (fieldType.equals("int")) {
                    int intValue = (int) field.get(localValue);
                    printLocal(intValue, fieldName);
                } else {
                    printLocal(field.get(localValue), fieldName);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addToSpec(StringBuilder s) {
        str.append(s.toString().replaceAll("#LOCAL#_", ""));
    }

    private static void clearVars() {
        localNameToStr.clear();
        currentLocalStr = new StringBuilder();
        currentLocalName = "";
    }

    private static Boolean isCurrentLocalStrNew() {
        if (localNameToStr.containsKey(currentLocalName)) {
            String str1 = localNameToStr.get(currentLocalName).toString();
            String str2 = currentLocalStr.toString();
            if (equalsLocalStrings(str1, str2))
                return false;
        }
        return true;
    }

    private static boolean equalsLocalStrings(String str1, String str2) {
        str1 = str1.replace(" |" , "");
        str2 = str2.replace(" |" , "");
        return str1.equals(str2);
    }

    private static void handleNewLocals(String localName, boolean calledAfterCloseBracket) {
        if (!calledAfterCloseBracket && !localName.contains("#LOCAL#")) return;
        if (!storeDeltas || isCurrentLocalStrNew()) {
            if (!calledAfterCloseBracket && currentLocalStr.length() > 2) {
                currentLocalStr.append(" |");
            }
            addToSpec(currentLocalStr);
        }
        localNameToStr.put(currentLocalName, currentLocalStr); // record previous state
        currentLocalStr = new StringBuilder();
        currentLocalName = localName;
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
