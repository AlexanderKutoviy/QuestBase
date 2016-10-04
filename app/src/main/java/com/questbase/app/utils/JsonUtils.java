package com.questbase.app.utils;

import com.annimon.stream.Stream;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import java.util.Collection;

public class JsonUtils {

    public static class Option {
        public final String id;
        public final String text;

        public Option(String id, String text) {
            this.id = id;
            this.text = text;
        }
    }

    public static JsonElement toJsonElement(Object object) {
        return new JsonParser().parse(new Gson().toJson(object));
    }

    public static JsonElement toJsonElement(String string) {
        return new JsonPrimitive(string);
    }

    public static JsonObject toJsonObject(Object object) {
        if (object instanceof String) {
            return (JsonObject) new JsonParser().parse((String) object);
        }
        return (JsonObject) new JsonParser().parse(new Gson().toJson(object));
    }

    public static JsonArray toJsonArray(Collection<?> collection) {
        JsonArray jsonArray = new JsonArray();
        Stream.of(collection).forEach(object -> jsonArray.add(toJsonElement(object)));
        return jsonArray;
    }
}
