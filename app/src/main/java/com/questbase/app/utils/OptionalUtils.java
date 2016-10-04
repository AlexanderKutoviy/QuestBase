package com.questbase.app.utils;

import com.annimon.stream.Optional;

import java.util.Collection;
import java.util.Collections;

public class OptionalUtils {

    public static <T> Optional<T> fromCollection(Collection<T> collection) {
        Preconditions.check(collection.size() <= 1);
        if (collection.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.ofNullable(collection.iterator().next());
        }
    }

    /**
     * Returns iterable of 1 size for non-empty optional.
     * Returns iterable of 0 size for empty optional.
     * Used when you need to convert a single optional to iterable (in order to stay aligned to a certain interface)
     */
    public static <T> Iterable<T> toIterable(Optional<T> optional) {
        if (optional.isPresent()) {
            return Collections.singletonList(optional.get());
        } else {
            return Collections.emptyList();
        }
    }
}
