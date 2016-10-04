package com.questbase.app.form;

import com.google.gson.JsonObject;
import com.questbase.app.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

public class QuestionUtils {

    public static Map<JsonObject, JsonObject> generateQuestions() {
        String question1 =
                "{\"id\": \"q-1\",\n" +
                        "            \"type\": \"RadioGroupImaged\",\n" +
                        "            \"text\": \"Наскільки цей ролик доносить наступну інформацію?\",\n" +
                        "            \"paramTitles\": [\n" +
                        "                \"Зовсім не доносить\",\n" +
                        "                \"Частково\",\n" +
                        "                \"Повністю доносить\"\n" +
                        "            ],\n" +
                        "            \"items\": [\n" +
                        "                {id: 0, text: \"Рекламуються нові тарифи\", img: \"31-1\"},\n" +
                        "                {id: 1, text: \"Оператор пропонує отримати більше послуг\", img: \"32-2\"},\n" +
                        "                {id: 2, text: \"Пакет послуг включає дзвінки та інтернет\", img: \"31-3\"},\n" +
                        "                {id: 3, text: \"Пакет послуг включає безліміт 1\", img: \"31-4\"},\n" +
                        "                {id: 4, text: \"Пакет послуг включає безліміт 2\", img: \"31-5\"},\n" +
                        "                {id: 5, text: \"Пакет послуг включає безліміт 3\", img: \"31-6\"},\n" +
                        "                {id: 6, text: \"Пакет послуг включає безліміт 4\", img: \"31-7\"},\n" +
                        "                {id: 7, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 8, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 9, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 10, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 11, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 12, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 13, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 14, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 14, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 14, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 14, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 15, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 16, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 17, text: \"Пакет послуг включає безліміт 6\", img: \"31-9\"}\n" +
                        "            ]}";


        String question2 = "{\"id\": \"q-2\",\n" +
                "            \"type\": \"BulkRadio\",\n" +
                "            \"text\": \"Наскільки цей ролик доносить наступну інформацію?\",\n" +
                "            \"paramTitles\": [\n" +
                "                \"Зовсім не доносить\",\n" +
                "                \"Частково доносить\",\n" +
                "                \"Доносить\",\n" +
                "                \"Частково доносить\",\n" +
                "                \"Повністю доносить\"\n" +
                "            ],\n" +
                "            \"items\": [\n" +
                "                {id: 0, text: \"Рекламуються нові тарифи\", img: \"31-1\"},\n" +
                "                {id: 1, text: \"Оператор пропонує отримати більше послуг\", img: \"32-2\"},\n" +
                "                {id: 2, text: \"Пакет послуг включає дзвінки та інтернет\", img: \"31-3\"},\n" +
                "                {id: 3, text: \"Пакет послуг включає безліміт 1\", img: \"31-4\"},\n" +
                "                {id: 4, text: \"Пакет послуг включає безліміт 2\", img: \"31-5\"}\n" +
                "            ]}";

        String question3 = "{\"id\": \"q-3\",\n" +
                "            \"type\": \"BulkRadioImaged\",\n" +
                "            \"text\": \"Наскільки цей ролик доносить наступну інформацію?\",\n" +
                "            \"paramTitles\": [\n" +
                "                \"Зовсім не доносить\",\n" +
                "                \"Частково\",\n" +
                "                \"Повністю доносить\"\n" +
                "            ],\n" +
                "            \"items\": [\n" +
                "                {id: 0, text: \"Рекламуються нові тарифи\", img: \"31-1\"},\n" +
                "                {id: 1, text: \"Оператор пропонує отримати більше послуг\", img: \"32-2\"},\n" +
                "                {id: 2, text: \"Пакет послуг включає дзвінки та інтернет\", img: \"31-3\"},\n" +
                "                {id: 3, text: \"Пакет послуг включає безліміт 1\", img: \"31-4\"},\n" +
                "                {id: 4, text: \"Пакет послуг включає безліміт 2\", img: \"31-5\"},\n" +
                "                {id: 5, text: \"Пакет послуг включає безліміт 3\", img: \"31-6\"},\n" +
                "                {id: 6, text: \"Пакет послуг включає безліміт 4\", img: \"31-7\"},\n" +
                "                {id: 7, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                "                {id: 8, text: \"Пакет послуг включає безліміт 6\", img: \"31-9\"}\n" +
                "            ]}";

        String question4 = "{\n" +
                "                    \"id\": \"q-4\",\n" +
                "                    \"type\": \"RadioGroup\",\n" +
                "                    \"text\": \"Після вдало виконаної місії ти нарешті можеш відпочити. Куди поїдеш?\\r\\n\",\n" +
                "                    \"items\": [\n" +
                "                        \"Повернуся додому і проведу весь вільний час з близькими\",\n" +
                "                        \"Відвідаю найкрутіший курорт та порозважаюсь\",\n" +
                "                        \"Повернуся до теоретичних справ: винайду новий код чи пристрій\",\n" +
                "                        \"Одразу візьмусь за нову справу\"\n" +
                "                    ]\n" +
                "                }";

        String question5 = "{\n" +
                "                    \"id\": \"q-5\",\n" +
                "                    \"type\": \"CheckGroup\",\n" +
                "                    \"text\": \"Після вдало виконаної місії ти нарешті можеш відпочити. Куди поїдеш?\\r\\n\",\n" +
                "                    \"items\": [\n" +
                "                        \"Повернуся додому і проведу весь вільний час з близькими\",\n" +
                "                        \"Відвідаю найкрутіший курорт та порозважаюсь\",\n" +
                "                        \"Повернуся до теоретичних справ: винайду новий код чи пристрій\",\n" +
                "                        \"Одразу візьмусь за нову справу\"\n" +
                "                    ]\n" +
                "                }";

        String question6 = " {\"id\": \"q-6\",\n" +
                "           \"type\": \"InputText\",\n" +
                "           \"text\": \"По Вашему мнению, что отличает компанию «Фокстрот?\" }";

        final Map<JsonObject, JsonObject> map = new HashMap<>();
        map.put(JsonUtils.toJsonObject("{q-0:null}"), JsonUtils.toJsonObject(question1));
        map.put(JsonUtils.toJsonObject("{q-1:null}"), JsonUtils.toJsonObject(question2));
        map.put(JsonUtils.toJsonObject("{q-2:null}"), JsonUtils.toJsonObject(question3));
        map.put(JsonUtils.toJsonObject("{q-3:null}"), JsonUtils.toJsonObject(question4));
        map.put(JsonUtils.toJsonObject("{q-4:null}"), JsonUtils.toJsonObject(question5));
        map.put(JsonUtils.toJsonObject("{q-5:null}"), JsonUtils.toJsonObject(question6));
        return map;
    }

    public static JsonObject getFirstQuestion() {
        return JsonUtils.toJsonObject(
                "{\"id\": \"q-0\",\n" +
                        "            \"type\": \"BulkRadioImaged\",\n" +
                        "            \"text\": \"Наскільки цей ролик доносить наступну інформацію?\",\n" +
                        "            \"paramTitles\": [\n" +
                        "                \"Зовсім не доносить\",\n" +
                        "                \"Частково\",\n" +
                        "                \"Повністю доносить\"\n" +
                        "            ],\n" +
                        "            \"items\": [\n" +
                        "                {id: 0, text: \"Рекламуються нові тарифи\", img: \"31-1\"},\n" +
                        "                {id: 1, text: \"Оператор пропонує отримати більше послуг\", img: \"32-2\"},\n" +
                        "                {id: 2, text: \"Пакет послуг включає дзвінки та інтернет\", img: \"31-3\"},\n" +
                        "                {id: 3, text: \"Пакет послуг включає безліміт 1\", img: \"31-4\"},\n" +
                        "                {id: 4, text: \"Пакет послуг включає безліміт 2\", img: \"31-5\"},\n" +
                        "                {id: 5, text: \"Пакет послуг включає безліміт 3\", img: \"31-6\"},\n" +
                        "                {id: 6, text: \"Пакет послуг включає безліміт 4\", img: \"31-7\"},\n" +
                        "                {id: 7, text: \"Пакет послуг включає безліміт 5\", img: \"31-8\"},\n" +
                        "                {id: 8, text: \"Пакет послуг включає безліміт 6\", img: \"31-9\"}\n" +
                        "            ]}");
    }
}