import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

public class Logger {
    private static StringBuilder str = new StringBuilder();
    private static HashMap<String, StringBuilder> localNameToStr = new HashMap<>();
    private static HashSet<Object> printedObjects = new HashSet<>();
    private static StringBuilder currentLocalStr = new StringBuilder();
    private static String currentLocalName = "";
    private static boolean storeDeltas = false, logCommands = true;
    private static String separator = " && ";
    private static String functionSignature = "";

    public static void addToSpec(String s) {
        if (s.equals("]")) {
            handleNewLocals("", true);
        }
        str.append(s);
    }

    public static void init(String functionSignature, boolean storeDeltas, boolean logCommands) {
        Logger.functionSignature = functionSignature;
        Logger.storeDeltas = storeDeltas;
        Logger.logCommands = logCommands;
        clearVars();
    }

    public static void logCmd(String cmd) {
        addToSpec("\n    ");
        if (logCommands)
            addToSpec("-> " + cmd + ";");
    }

    public static void printLocal(int localValue, String localName) {
        handleNewLocals(localName, false);
        currentLocalStr.append(localName + "==" + localValue);
    }

    public static void printLocal(Object localValue, String localName, boolean inRecursion) {
        if (inRecursion && printedObjects.contains(localValue)) // Cycle detected
            return;

        printedObjects.add(localValue);
        handleNewLocals(localName, false);
        if (null == localValue) {
            currentLocalStr.append(localName + "==" + "null");
            return;
        }
        currentLocalStr.append(localName + "==" + localValue);
        Field fields[] = localValue.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldType = field.getType().toString();
                String fieldName = field.getName();
                if (fieldType.equals("int")) {
                    int intValue = (int) field.get(localValue);
                    printLocal(intValue, localName + "." + fieldName);
                } else {
                    printedObjects.add(localValue);
                    printLocal(field.get(localValue), localName + "." + fieldName, true);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        printedObjects.clear();
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
        str1 = str1.replace(separator, "");
        str2 = str2.replace(separator, "");
        return str1.equals(str2);
    }

    private static void handleNewLocals(String localName, boolean calledAfterCloseBracket) {
        if (!calledAfterCloseBracket && !localName.contains("#LOCAL#")) return;
        if (!storeDeltas || isCurrentLocalStrNew()) {
            if (!calledAfterCloseBracket && currentLocalStr.length() > 2) {
                currentLocalStr.append(separator);
            }
            addToSpec(currentLocalStr);
        }
        if (!currentLocalName.equals("")) {
            localNameToStr.put(currentLocalName, currentLocalStr); // record previous state
        }
        currentLocalStr = new StringBuilder();
        currentLocalName = localName;
    }

    public static void dumpSpecToFile(String fileName) {
        PrintWriter writer;
        try {
            writer = new PrintWriter(fileName + ".spec", "UTF-8");
            String stringToWrite = str.toString();
            while (stringToWrite.contains(separator + "]"))
                stringToWrite = stringToWrite.replace(separator + "]", "]");
            writer.println(functionSignature + stringToWrite + "}");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
