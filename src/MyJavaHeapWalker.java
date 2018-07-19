//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import bgu.cs.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import jminor.BooleanType;
import jminor.IntType;
import jminor.IntVal;
import jminor.JmStore;
import jminor.Obj;
import jminor.PrimitiveField;
import jminor.PrimitiveVar;
import jminor.RefField;
import jminor.RefType;
import jminor.RefVar;
import jminor.Type;
import jminor.Val;
import jminor.Var;
import jminor.Var.VarRole;
import jminor.java.JavaEnv;
import jminor.java.MethodArg;

public class MyJavaHeapWalker {
    private static final boolean scanStaticFields = false;
    private Map<Class<?>, RefType> clsToRefType = new HashMap();
    private Map<Object, Obj> javaObjectToSynthObj = new HashMap();
    private Map<Integer, IntVal> intToSynthInt = new HashMap();
    private Map<String, Var> nameToVar = new HashMap();
    private Map<Field, jminor.Field> jfieldToSynthField = new HashMap();
    private Map<Var, Val> resultEnv;
    private Map<Obj, Map<jminor.Field, Val>> resultHeap;
    private final Logger logger;

    public MyJavaHeapWalker(Method m, Class<? extends JavaEnv> envClass, Logger logger) {
        this.logger = logger;
        Field[] var7;
        int var6 = (var7 = envClass.getFields()).length;

        for(int var5 = 0; var5 < var6; ++var5) {
            Field field = var7[var5];
            if (!field.isAnnotationPresent(MethodArg.class) && !field.getName().equals("ret") && !field.getName().equals("self")) {
                this.createVar(field.getName(), field.getType(), VarRole.TEMP, false, false);
            } else {
                MethodArg marg = (MethodArg)field.getAnnotation(MethodArg.class);
                boolean isOut = marg != null && marg.out() || field.getName().equals("ret") || field.getName().equals("self");
                boolean isReadonly = marg != null && marg.readonly() && !field.getName().equals("ret") || field.getName().equals("self");
                this.createVar(field.getName(), field.getType(), VarRole.ARG, isOut, isReadonly);
            }
        }

    }

    public Collection<Var> getVars() {
        return this.nameToVar.values();
    }

    public Collection<RefType> getRefTypes() {
        return this.clsToRefType.values();
    }

    private Var createVar(String name, Class<?> type, VarRole role, boolean out, boolean readonly) {
        if (ReflectionUtils.isObjectRefType(type)) {
            RefVar var = new RefVar(name, this.geRefType(type), role, out, readonly);
            this.nameToVar.put(name, var);
            return var;
        } else if (ReflectionUtils.isIntType(type)) {
            Type jminorType = javaTypeToJminorType(type);
            PrimitiveVar var = new PrimitiveVar(name, jminorType, role, out, readonly);
            this.nameToVar.put(name, var);
            return var;
        } else {
            this.logger.info("Ignoring unknown variable type: " + type.getName() + "!");
            return null;
        }
    }

    private static Type javaTypeToJminorType(Class<?> type) {
        if (type.isPrimitive() && !type.isSynthetic() && type.equals(Integer.TYPE)) {
            return IntType.v;
        } else {
            return type.isPrimitive() && !type.isSynthetic() && type.equals(Boolean.TYPE) ? BooleanType.v : null;
        }
    }

    public void reset() {
        this.javaObjectToSynthObj.clear();
    }

    public JmStore walk(Class<? extends JavaEnv> env) throws IllegalArgumentException, IllegalAccessException {
        this.resultEnv = new HashMap();
        this.resultHeap = new HashMap();
        this.processEnv(env);
        Set<Object> marked = new HashSet();
        Set<Object> frontier = new HashSet();
        frontier.addAll(this.javaObjectToSynthObj.keySet());

        while(true) {
            Object o;
            do {
                if (frontier.isEmpty()) {
                    JmStore result = new JmStore(this.resultHeap.keySet(), new HashSet(), this.resultEnv, this.resultHeap);
                    return result;
                }

                Iterator<Object> iter = frontier.iterator();
                o = iter.next();
                iter.remove();
            } while(marked.contains(o));

            marked.add(o);
            RefType otype = this.geRefType(o.getClass());
            Obj synthObj = this.getSynthObj(o);
            Map<jminor.Field, Val> synthObjFields = (Map)this.resultHeap.get(synthObj);
            if (synthObjFields == null) {
                synthObjFields = new HashMap();
                this.resultHeap.put(synthObj, synthObjFields);
            }

            Field[] var12;
            int var11 = (var12 = o.getClass().getFields()).length;

            for(int var10 = 0; var10 < var11; ++var10) {
                Field field = var12[var10];
                Class<?> fieldType = field.getType();
                jminor.Field synthField = this.getSynthField(field, otype);
                Object fieldVal = field.get(o);
                if (ReflectionUtils.isObjectRefType(fieldType)) {
                    if (fieldVal == null) {
                        ((Map)synthObjFields).put(synthField, Obj.NULL);
                    } else {
                        frontier.add(fieldVal);
                        Obj synthSucc = this.getSynthObj(fieldVal);
                        ((Map)synthObjFields).put(synthField, synthSucc);
                    }
                } else if (ReflectionUtils.isIntType(fieldType)) {
                    int intVal = (Integer)fieldVal;
                    ((Map)synthObjFields).put(synthField, this.getSynthInt(intVal));
                } else {
                    this.logger.info("Ignoring field type: " + fieldType.getName() + "!");
                }
            }
        }
    }

    private jminor.Field getSynthField(Field field, RefType owner) {
        jminor.Field result = null;
        Class<?> fieldType = field.getType();
        if (fieldType.isPrimitive()) {
            Type jminorFieldType = javaTypeToJminorType(fieldType);
            if (jminorFieldType != null) {
                result = (jminor.Field)this.jfieldToSynthField.get(field);
                if (result == null) {
                    result = new PrimitiveField(field.getName(), owner, jminorFieldType, false);
                    this.jfieldToSynthField.put(field, result);
                }
            } else {
                this.logger.info("Ignoring field type: " + fieldType.getName() + "!");
            }
        } else if (ReflectionUtils.isObjectRefType(fieldType)) {
            RefType dstType = this.geRefType(fieldType);
            result = (jminor.Field)this.jfieldToSynthField.get(field);
            if (result == null) {
                result = new RefField(field.getName(), owner, dstType, false);
                this.jfieldToSynthField.put(field, result);
            }
        }

        return (jminor.Field)result;
    }

    private Obj getSynthObj(Object o) {
        if (o == null) {
            return Obj.NULL;
        } else {
            Obj synthObject = (Obj)this.javaObjectToSynthObj.get(o);
            if (synthObject == null) {
                synthObject = new Obj(this.geRefType(o.getClass()));
                this.javaObjectToSynthObj.put(o, synthObject);
            }

            return synthObject;
        }
    }

    protected void processEnv(Class<? extends JavaEnv> env) throws IllegalArgumentException, IllegalAccessException {
        Field[] var5;
        int var4 = (var5 = env.getClass().getFields()).length;

        for(int var3 = 0; var3 < var4; ++var3) {
            Field field = var5[var3];
            Var var = (Var)this.nameToVar.get(field.getName());

            assert var != null;

            Object val = field.get(env);
            Class<?> fieldType = field.getType();
            if (ReflectionUtils.isObjectRefType(fieldType)) {
                Obj synthObj = this.getSynthObj(val);
                this.resultEnv.put(var, synthObj);
            } else if (ReflectionUtils.isIntType(fieldType)) {
                Val synthVal = this.getSynthInt((Integer)val);
                this.resultEnv.put(var, synthVal);
            } else {
                this.logger.info("Ignoring enviornment field of type: " + fieldType.getName() + "!");
            }
        }

    }

    private Val getSynthInt(Integer val) {
        int intVal = val;
        IntVal result = (IntVal)this.intToSynthInt.get(val);
        if (result == null) {
            result = new IntVal(intVal);
            this.intToSynthInt.put(val, result);
        }

        return result;
    }

    protected RefType geRefType(Class<?> cls) {
        RefType synthType = (RefType)this.clsToRefType.get(cls);
        if (synthType == null) {
            synthType = new RefType(cls.getSimpleName());
            this.clsToRefType.put(cls, synthType);
            Field[] var6;
            int var5 = (var6 = cls.getFields()).length;

            for(int var4 = 0; var4 < var5; ++var4) {
                Field field = var6[var4];
                if (!Modifier.isStatic(field.getModifiers())) {
                    jminor.Field synthField = this.getSynthField(field, synthType);
                    if (synthField != null) {
                        synthType.add(synthField);
                    }
                }
            }
        }

        return synthType;
    }
}
