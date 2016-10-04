package com.questbase.app.controller;

import com.questbase.app.utils.Objects;

public class Event<T> {
    public final Type type;
    public final T data;

    public Event(T data, Type type) {
        this.data = data;
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, data);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Event)) {
            return false;
        }
        Event event = (Event) object;
        return Objects.equal(type, event.type)
                && Objects.equal(data, event.data);
    }

    public enum Type {
        WRITE,
        UPDATE,
        NONE,
        DELETE
    }
}
