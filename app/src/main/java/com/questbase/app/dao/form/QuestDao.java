package com.questbase.app.dao.form;

import com.annimon.stream.Optional;
import com.questbase.app.domain.Form;
import com.questbase.app.net.entity.Category;

import java.util.List;

public interface QuestDao {

    /**
     * Method adds one form to the database
     */
    void create(Form form);

    /**
     * Method return optional of current form from database
     */
    Optional<Form> read(Form form);

    /**
     * Method returns List of forms that belong to category
     */
    List<Form> read(Category category);

    List<Form> readAll();

    void update(Form form);

    void update(Form form, Category category);

    void delete(Form form);
}