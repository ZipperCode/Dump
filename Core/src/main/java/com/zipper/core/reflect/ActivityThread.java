package com.zipper.core.reflect;


import android.app.Activity;
import android.app.Application;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ProviderInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.util.ArrayMap;

import java.util.List;

/**
 * Created by Milk on 5/20/21.
 * * ∧＿∧
 * (`･ω･∥
 * 丶　つ０
 * しーＪ
 * 此处无Bug
 */
public class ActivityThread {
    public static final ReflectionWrapper REF = ReflectionWrapper.on("android.app.ActivityThread");

    public static ReflectionWrapper.StaticMethodWrapper<Object> currentActivityThread = REF.staticMethod("currentActivityThread");
    public static ReflectionWrapper.FieldWrapper<Object> mBoundApplication = REF.field("mBoundApplication");
    public static ReflectionWrapper.FieldWrapper<Handler> mH = REF.field("mH");
    public static ReflectionWrapper.FieldWrapper<Application> mInitialApplication = REF.field("mInitialApplication");
    public static ReflectionWrapper.FieldWrapper<ArrayMap<Object, Object>> mProviderMap = REF.field("mProviderMap");
    public static ReflectionWrapper.FieldWrapper<Instrumentation> mInstrumentation = REF.field("mInstrumentation");
    public static ReflectionWrapper.FieldWrapper<IInterface> sPackageManager = REF.field("sPackageManager");
    public static ReflectionWrapper.MethodWrapper<IBinder> getApplicationThread = REF.method("getApplicationThread");
    public static ReflectionWrapper.MethodWrapper<Object> getSystemContext = REF.method("getSystemContext");


    public static class ActivityClientRecord {
        public static final ReflectionWrapper REF = ReflectionWrapper.on("android.app.ActivityThread$ActivityClientRecord");
        public static ReflectionWrapper.FieldWrapper<Activity> activity = REF.field("activity");
        public static ReflectionWrapper.FieldWrapper<ActivityInfo> activityInfo = REF.field("activityInfo");
        public static ReflectionWrapper.FieldWrapper<Intent> intent = REF.field("intent");
    }

    public static class AppBindData {
        public static final ReflectionWrapper REF = ReflectionWrapper.on("android.app.ActivityThread$AppBindData");
        public static ReflectionWrapper.FieldWrapper<ApplicationInfo> appInfo = REF.field("appInfo");
        public static ReflectionWrapper.FieldWrapper<Object> info = REF.field("info");
        public static ReflectionWrapper.FieldWrapper<String> processName = REF.field("processName");
        public static ReflectionWrapper.FieldWrapper<ComponentName> instrumentationName = REF.field("instrumentationName");
        public static ReflectionWrapper.FieldWrapper<List<ProviderInfo>> providers = REF.field("providers");
    }

    public static class H {
        public static final ReflectionWrapper REF = ReflectionWrapper.on("android.app.ActivityThread$H");
        public static ReflectionWrapper.FieldWrapper<Integer> LAUNCH_ACTIVITY = REF.field("LAUNCH_ACTIVITY");
        public static ReflectionWrapper.FieldWrapper<Integer> EXECUTE_TRANSACTION = REF.field("EXECUTE_TRANSACTION");
    }
}
