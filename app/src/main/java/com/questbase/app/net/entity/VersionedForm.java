package com.questbase.app.net.entity;

import com.questbase.app.utils.Objects;

public class VersionedForm {

    public long formId;
    public long version;

    public VersionedForm() {
    }

    public VersionedForm(long formId, long timeStamp) {
        this.formId = formId;
        this.version = timeStamp;
    }

    @Override
    public int hashCode() {
        return Objects.hash(formId, version);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || !(object instanceof VersionedForm)) {
            return false;
        }
        VersionedForm feedElement = (VersionedForm) object;
        return Objects.equal(formId, feedElement.formId)
                && Objects.equal(version, feedElement.version);
    }

    @Override
    public String toString() {
        return "{ formId: " + formId + "; version: " + version + "; }";
    }
}
