package com.zipper.api.module.util;

import android.os.Build;
import android.util.Log;
import android.util.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public final class ReflectUtils {

    private static Method getDeclaredMethod;

    static {
        try {
            getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);
            HiddenApi.setHiddenApi();
            HiddenApi.exemptAll();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射的反射获取Class
     *
     * 用户反射隐藏类
     * @param name          隐藏类
     * @return              找到的类
     * @throws Exception    异常
     */
    public synchronized static Class<?> loadHideForName(String name) throws Exception {
        Method declaredMethod = Class.class.getDeclaredMethod("forName", String.class);
        return (Class<?>) declaredMethod.invoke(null, name);
    }

    /**
     * 反射的反射获取Class
     *
     * 用户反射隐藏类
     * @param targetClass   目标类
     * @param params        参数类型
     * @return              构造方法
     * @throws Exception    异常
     */
    public static Constructor<?> loadHideConstructor(Class<?> targetClass, Class<?>... params)
            throws Exception {
        Method getDeclaredConstructor = Class.class.getDeclaredMethod("getDeclaredConstructor", Class[].class);
        Constructor<?> constructor = (Constructor<?>) getDeclaredConstructor.invoke(targetClass, (Object) params);
        assert constructor != null;
        constructor.setAccessible(true);
        return constructor;
    }

    /**
     * 查找指定类实例
     *
     * @param targetClass       指定类
     * @param params            参数
     * @param <T>               实例类型
     * @return                  实例化对象
     * @throws Exception        异常
     */
    public static <T> T findInstance(Class<T> targetClass, Object... params) throws Exception {
        Class<?>[] paramClass = new Class[params.length];
        for (int i = 0; i < paramClass.length; i++) {
            paramClass[i] = params[i].getClass();
        }
        Constructor<T> constructor = targetClass.getConstructor(paramClass);
        return constructor.newInstance(params);
    }


    public static Method findMethod(Class<?> targetClass, String name, Class<?>... params)
            throws Exception {
        Method method = targetClass.getDeclaredMethod(name, params);
        method.setAccessible(true);
        return method;
    }

    public static Method deepFindMethod(Class<?> targetClass, String name, Class<?>... params) {
        Method method = null;
        boolean isFind = false;
        while (!isFind && targetClass != null) {
            try {
                method = targetClass.getDeclaredMethod(name, params);
                method.setAccessible(true);
                isFind = true;
            } catch (NoSuchMethodException e) {
                targetClass = targetClass.getSuperclass();
            }
        }
        return method;
    }

    public static Method loadHideMethod(Class<?> targetClass, String name, Class<?>... params)
            throws Exception {
        Method method  = (Method) getDeclaredMethod.invoke(targetClass, name, (Object) params);
        assert method != null;
        method.setAccessible(true);
        return method;
    }

    public static void printMethod(Class<?> targetClass) throws Exception {
        Method getDeclaredMethods = Class.class.getDeclaredMethod("getDeclaredMethods");
        Method getMethods = Class.class.getDeclaredMethod("getMethods");
        Method[] methods = (Method[]) getMethods.invoke(targetClass);
        assert methods != null;
        for (Method method : methods) {
            Log.e("getMethods", ">>> name = " + method.getName());
        }
        Method[] declaredMethods = (Method[]) getDeclaredMethods.invoke(targetClass);

        for (int i = 0; i < methods.length; i++) {
            Log.e("getDeclaredMethods",">>> name = " + declaredMethods[i].getName());
        }
    }

    public static Method loadHideMethod(Object object, String name, Class<?>... params)
            throws Exception {
        Method method = (Method) getDeclaredMethod.invoke(object.getClass(), name, (Object) params);
        assert method != null;
        method.setAccessible(true);
        return method;
    }

    public static Method findMethod(Object target, String name, Class<?> params) throws Exception {
        Method method = target.getClass().getDeclaredMethod(name, params);
        method.setAccessible(true);
        return method;
    }

    public static Method deepFindMethod(Object target, String name, Class<?>... params) {
        Method method = null;
        Class<?> targetClass = target.getClass();
        boolean isFind = false;
        while (!isFind && targetClass != null) {
            try {
                method = targetClass.getDeclaredMethod(name, params);
                method.setAccessible(true);
                isFind = true;
            } catch (NoSuchMethodException e) {
                targetClass = targetClass.getSuperclass();
            }
        }
        return method;
    }

    public static Object findInvokeMethod(Object target, String name, List<Pair<Class<?>, Object>> params)
            throws Exception {
        Class<?>[] paramClass = new Class[params.size()];
        Object[] paramObj = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            Pair<Class<?>, Object> pair = params.get(i);
            paramClass[i] = pair.first;
            paramObj[i] = pair.second;
        }
        Method method = target.getClass().getDeclaredMethod(name, paramClass);
        method.setAccessible(true);
        Object result = method.invoke(target, paramObj);
        if (!Modifier.isPublic(method.getModifiers())) {
            method.setAccessible(false);
        }
        return result;
    }

    public static Object deepFindInvokeMethod(Object target,
                                              String name,
                                              List<Pair<Class<?>,
                                                      Object>> params) throws Exception {
        Object result = null;
        Class<?> targetClass = target.getClass();
        Class<?>[] paramClass = new Class[params.size()];
        Object[] paramObj = new Object[params.size()];
        for (int i = 0; i < params.size(); i++) {
            Pair<Class<?>, Object> pair = params.get(i);
            paramClass[i] = pair.first;
            paramObj[i] = pair.second;
        }
        boolean isFind = false;
        List<Throwable> exps = new ArrayList<>();
        while (!isFind && targetClass != null) {
            try {
                Method method = targetClass.getDeclaredMethod(name, paramClass);
                method.setAccessible(true);
                result = method.invoke(target, paramObj);
                if (!Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(false);
                }
                isFind = true;
            } catch (Exception e) {
                exps.add(e);
                targetClass = targetClass.getSuperclass();
            }
        }
        if (exps.size() > 0) {
            throw new Exception("");
        }
        return result;
    }

    public static Object deepFindInvokeMethod(Object target,
                                              String name,
                                              Object... params) throws Exception {
        Object result = null;
        Class<?> targetClass = target.getClass();
        Class<?>[] paramClass = new Class[params.length];
        for (int i = 0; i < paramClass.length; i++) {
            paramClass[i] = params[i] != null ? params[i].getClass() : null;
        }
        boolean isFind = false;
        List<Throwable> exps = new ArrayList<>();
        while (!isFind && targetClass != null) {
            try {
                Method method = targetClass.getDeclaredMethod(name, paramClass);
                method.setAccessible(true);
                result = method.invoke(target, params);
                if (!Modifier.isPublic(method.getModifiers())) {
                    method.setAccessible(false);
                }
                isFind = true;
                exps.clear();
            } catch (Exception e) {
                exps.add(e);
                targetClass = targetClass.getSuperclass();
            }
        }
        if (exps.size() > 0) {
            throw new Exception("deepFindInvokeMethod find Method error !");
        }
        return result;
    }

    public static Field findField(Class<?> targetClass, String name) throws Exception {
        Field field = targetClass.getDeclaredField(name);
        field.setAccessible(true);
        return field;
    }

    public static Field loadHideField(Class<?> targetClass, String name) throws Exception {
        Method getDeclaredField = Class.class.getDeclaredMethod("getDeclaredField", String.class);
        Field field = (Field) getDeclaredField.invoke(targetClass,name);
        assert field != null;
        field.setAccessible(true);
        return field;
    }

    public static Field deepFindField(Class<?> targetClass, String name) {
        Field field = null;
        boolean isFind = false;
        while (!isFind && targetClass != null) {
            try {
                field = targetClass.getDeclaredField(name);
                field.setAccessible(true);
                isFind = true;
            } catch (NoSuchFieldException e) {
                targetClass = targetClass.getSuperclass();
            }
        }
        return field;
    }


    public static Object fieldGet(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    public static Object deepFieldGet(Object target, String name) {
        Object result = null;
        Field field = deepFindField(target.getClass(), name);
        if (field != null) {
            try {
                result = field.get(target);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static Object loadHideFieldValue(Object target, String name) throws Exception{
        Field hideField = loadHideField(target.getClass(),name);
        return hideField.get(target);
    }

    public static void fieldSet(Object target, String name, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(target, value);
            if (!Modifier.isPublic(field.getModifiers())) {
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deepFieldSet(Object target, String name, Object value) {
        Field field = deepFindField(target.getClass(), name);
        if (field != null) {
            try {
                field.set(target, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 网络代码，设置手机隐藏api功能
     * 用途待测试
     */
    public static class HiddenApi{

        private static String TAG = HiddenApi.class.getSimpleName();
        private static Object sVmRuntime;
        private static Method setHiddenApiExemptions;

        public static void setHiddenApi(){
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                return;
            }
            try {
                Method forName = Class.class.getDeclaredMethod("forName", String.class);
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class<?> vmRuntimeClass = (Class<?>) forName.invoke(null, "dalvik.system.VMRuntime");
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "getRuntime", null);
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, "setHiddenApiExemptions", new Class[]{String[].class});
                assert getRuntime != null;
                sVmRuntime = getRuntime.invoke(null);
            } catch (Throwable e) {
                Log.w(TAG, "reflect bootstrap failed:", e);
            }
        }

        /**
         * make the method exempted from hidden API check.
         *
         * @param method the method signature prefix.
         * @return true if success.
         */
        public static boolean exempt(String method) {
            return exempt(new String[]{method});
        }

        /**
         * make specific methods exempted from hidden API check.
         *
         * @param methods the method signature prefix, such as "Ldalvik/system", "Landroid" or even "L"
         * @return true if success
         */
        public static boolean exempt(String... methods) {
            if (sVmRuntime == null || setHiddenApiExemptions == null) {
                return false;
            }

            try {
                setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
                return true;
            } catch (Throwable e) {
                return false;
            }
        }

        /**
         * Make all hidden API exempted.
         *
         * @return true if success.
         */
        public static boolean exemptAll() {
            return exempt(new String[]{"L"});
        }
    }
}
