package com.questbase.app.form;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.questbase.app.utils.Objects;

public class Event {

    public Type type;

    /**
     * A question or a result
     */
    public final JsonObject item;
    /**
     * Observable that emits responses given to this item (if question)
     */
    private final JsonElement response;
    private final double progress;

    public Event(Type type, JsonObject item, JsonElement response, double progress) {
        this.type = type;
        this.item = item;
        this.response = response;
        this.progress = progress;
    }

    public Event(Event event) {
        this.type = event.type;
        this.item = event.item;
        this.response = event.response;
        this.progress = event.progress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, item, response, progress);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Event)) {
            return false;
        }
        Event event = (Event) object;
        return Objects.equal(type, event.type)
                && Objects.equal(item, event.item)
                && Objects.equal(response, event.response)
                && Objects.equal(progress, event.progress);
    }

    @Override
    public String toString() {
        return "Event{" +
                "type=" + type +
                ", item=" + item +
                ", response=" + response +
                ", progress=" + progress +
                '}';
    }

    public enum Type {
        PREPARED, VISIBLE
    }
}
