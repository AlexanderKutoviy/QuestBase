package com.questbase.app.utils;

public class Preconditions {
    public static <T> T checkNotNull(T reference) {
        if (reference == null) {
            throw new NullPointerException();
        }
        return reference;
    }

    public static <T> T checkNotNull(T reference, Object errorMessage) {
        if (reference == null) {
            throw new NullPointerException(String.valueOf(errorMessage));
        }
        return reference;
    }

    public static void check(boolean expression) {
        if (!expression) {
            throw new IllegalStateException();
        }
    }

    public static void check(boolean expression, String pattern, Object ... args) {
        if (!expression) {
            throw new IllegalStateException(String.format(pattern, args));
        }
    }
}
