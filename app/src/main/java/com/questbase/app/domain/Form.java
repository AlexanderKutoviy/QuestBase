package com.questbase.app.domain;

import com.questbase.app.utils.Objects;
import com.questbase.app.net.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class Form {

    public long formId;
    public String title;
    public List<FormResource> resources;
    public Category category;
    public String libVersion;
    public long version;
    public State state;

    public Form(Builder builder) {
        formId = builder.formId;
        title = builder.title;
        resources = builder.resources;
        category = builder.category;
        libVersion = builder.libVersion;
        version = builder.version;
        state = builder.formState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resources,
                formId,
                title,
                libVersion,
                version,
                state);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof Form)) {
            return false;
        }
        Form form = (Form) object;
        return Objects.equal(formId, form.formId)
                && Objects.equal(title, form.title)
                && Objects.equal(libVersion, form.libVersion)
                && Objects.equal(version, form.version)
                && Objects.equal(state, form.state)
                && Objects.equal(resources, form.resources);
    }

    public static class Builder {
        //required
        private long formId;
        //optional
        private String title;
        private List<FormResource> resources = new ArrayList<>();
        private com.questbase.app.net.entity.Category category;
        private String libVersion;
        private long version;
        private State formState;

        public Builder(long id) {
            this.formId = id;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder resources(List<FormResource> list) {
            resources = list;
            return this;
        }

        public Builder category(Category val) {
            category = val;
            return this;
        }

        public Builder libVersion(String val) {
            libVersion = val;
            return this;
        }

        public Builder version(long val) {
            version = val;
            return this;
        }

        public Builder state(State val) {
            formState = val;
            return this;
        }

        public Form build() {
            return new Form(this);
        }
    }
}