package com.questbase.app.domain;

import java.util.List;

public class Category {

    public int id;
    public String name;
    public List<FormIdWrapper> forms;

    @Override
    public String toString() {
        return "id: " + id + "; name: " + name + "; forms list: " + forms;
    }
}
