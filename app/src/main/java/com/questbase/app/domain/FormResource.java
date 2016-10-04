package com.questbase.app.domain;

import com.questbase.app.dao.resource.ResourceModel;
import com.questbase.app.utils.Objects;

public class FormResource {

    public String resId;
    public String version;
    public State state;
    public Form form;

    public FormResource(String resId, String version, Form form) {
        this.resId = resId;
        this.version = version;
        this.form = form;
    }

    public FormResource(String resId, String version, State state, Form form) {
        this.resId = resId;
        this.version = version;
        this.state = state;
        this.form = form;
    }

    public FormResource(ResourceModel resourceModel) {
        this(resourceModel.resId, resourceModel.version, State.valueOf(resourceModel.state), new Form.Builder(resourceModel.formId).build());
    }

    @Override
    public int hashCode() {
        return Objects.hash(resId, version);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof FormResource)) {
            return false;
        }
        FormResource formResource = (FormResource) object;
        return Objects.equal(resId, formResource.resId)
                && Objects.equal(version, formResource.version);
    }

    @Override
    public String toString() {
        return "{resId: " + resId + "; version: " + version + "; state: " + state + ";}";
    }
}