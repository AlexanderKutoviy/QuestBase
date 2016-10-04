package com.questbase.app.form.mvp;

import android.os.Parcel;
import android.os.Parcelable;

import com.annimon.stream.Objects;
import com.questbase.app.flowui.screens.RespoScreen;
import com.questbase.app.obsolete.ScriptManager;

public class FormScreen implements Parcelable, RespoScreen {
    private final long formId;
    private final long formVersion;
    private final String formTitle;
    private final ScriptManager.Model model;

    public long getFormId() {
        return formId;
    }

    public long getFormVersion() {
        return formVersion;
    }

    String getFormTitle() {
        return formTitle;
    }

    public ScriptManager.Model getModel() {
        return model;
    }

    public FormScreen(long formId, long formVersion, String formTitle, ScriptManager.Model model) {
        this.formId = formId;
        this.formVersion = formVersion;
        this.formTitle = formTitle;
        this.model = model;
    }

    protected FormScreen(Parcel in) {
        formId = in.readLong();
        formVersion = in.readLong();
        formTitle = in.readString();
        model = (ScriptManager.Model) in.readValue(ScriptManager.Model.class.getClassLoader());
    }

    public static final Creator<FormScreen> CREATOR = new Creator<FormScreen>() {
        @Override
        public FormScreen createFromParcel(Parcel in) {
            return new FormScreen(in.readLong(), in.readLong(), in.readString(), (ScriptManager.Model) in.readValue(ScriptManager.Model.class.getClassLoader()));
        }

        @Override
        public FormScreen[] newArray(int size) {
            return new FormScreen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(formId);
        dest.writeLong(formVersion);
        dest.writeString(formTitle);
        dest.writeValue(model);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        FormScreen formScreen = (FormScreen) object;

        return Objects.equals(formScreen.formId, ((FormScreen) object).formId)
                && Objects.equals(formScreen.formVersion, ((FormScreen) object).formVersion)
                && Objects.equals(formScreen.formTitle, ((FormScreen) object).formTitle)
                && Objects.equals(formScreen.model, ((FormScreen) object).model);

    }
}
