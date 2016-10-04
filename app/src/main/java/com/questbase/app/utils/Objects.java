package com.questbase.app.utils;

import com.annimon.stream.Stream;

public class Objects {

    private static final int INITIAL_HASH_VALUE = 1;
    private static final int HASH_FACTOR = 31;

    public static boolean equal(Object o1, Object o2) {
        return o1 == o2 || o1 != null && o1.equals(o2);
    }

    public static int hash(Object... objects) {
        return Stream.of(objects).filter(object -> object != null)
                .reduce(INITIAL_HASH_VALUE, (hash, object) -> hash + HASH_FACTOR * hashCode(object));
    }

    private static int hashCode(Object object) {
        return (object == null ? 0 : object.hashCode());
    }
}