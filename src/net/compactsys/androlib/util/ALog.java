
/*
 * Copyright 2012 Ivan Masmitjà
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.compactsys.androlib.util;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

/**
 * Advanced Log.
 * Based on a Jeffrey Blattman post
 * http://zerocredibility.wordpress.com/2012/03/08/android-advanced-logger/
 * <p/>
 * Initialize log level:
 * <p/>
 * //Extend Application class and initialize log level
 * public class MyApplication extends Application {
 *
 * @Override public void onCreate() {
 * ALog.setTag("ApplicationName");
 * ALog.setLevel(ALog.Level.D);
 * }
 * }
 * <p/>
 * Usage:
 * ALog.w("oh noes, a problem occurred when i %s and then also at %s", msg1, msg2);
 */
public final class ALog {
    public static final int TRACE_DEEP = 5;
    private static String tag = "<tag unset>";
    private static Level level = Level.V;

    private ALog() {

    }

    public static void setLevel(Level l) {
        level = l;
    }

    public static void setTag(String t) {
        tag = t;
    }

    private static LogContext getContext() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        StackTraceElement element = trace[TRACE_DEEP]; // frame below us; the caller
        LogContext context = new LogContext(element);
        return context;
    }

    private static String getMessage(String s, Object... args) {
        s = String.format(s, args);
        LogContext c = getContext();
        String msg = c.simpleClassName + "." + c.methodName + "@"
                + c.lineNumber + ": " + s;
        return msg;
    }

    public static void v(String format, Object... args) {
        if (level.getValue() > Level.V.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.v(tag, msg);
    }

    public static void d(String format, Object... args) {
        if (level.getValue() > Level.D.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.d(tag, msg);
    }

    public static void i(String format, Object... args) {
        if (level.getValue() > Level.I.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.i(tag, msg);
    }

    public static void w(String format, Object... args) {
        if (level.getValue() > Level.W.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.w(tag, msg);
    }

    public static void w(String format, Throwable t, Object... args) {
        if (level.getValue() > Level.W.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.w(tag, msg, t);
    }

    public static void e(String format, Object... args) {
        if (level.getValue() > Level.E.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.e(tag, msg);
    }

    public static void e(String format, Throwable t, Object... args) {
        if (level.getValue() > Level.E.getValue()) {
            return;
        }

        String msg = getMessage(format, args);
        Log.e(tag, msg, t);
    }

    public static void trace() {
        try {
            throw new Throwable("dumping stack trace ...");
        } catch (Throwable t) {
            ALog.e("trace:", t);
        }
    }

    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    public enum Level {
        V(1), D(2), I(3), W(4), E(5);
        private int value;

        private Level(int value) {
            this.value = value;
        }

        int getValue() {
            return value;
        }
    }

    private static class LogContext {
        String simpleClassName;
        String methodName;
        int lineNumber;

        LogContext(StackTraceElement element) {
            this.simpleClassName = getSimpleClassName(element.getClassName());
            this.methodName = element.getMethodName();
            this.lineNumber = element.getLineNumber();
        }

        private static String getSimpleClassName(String className) {
            int i = className.lastIndexOf(".");
            if (i == -1) {
                return className;
            }
            return className.substring(i + 1);
        }
    }
}
