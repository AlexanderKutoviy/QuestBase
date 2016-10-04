package com.questbase.app.personalresult;

import android.os.Parcel;
import android.os.Parcelable;

import com.questbase.app.flowui.screens.RespoScreen;

public class PersonalResultScreen implements Parcelable, RespoScreen {

    public final long formId;

    public long getFormId() {
        return formId;
    }

    public PersonalResultScreen(long formId) {
        this.formId = formId;
    }

    protected PersonalResultScreen(Parcel in) {
        formId = Long.parseLong(in.readString());
    }

    public static final Creator<PersonalResultScreen> CREATOR = new Creator<PersonalResultScreen>() {
        @Override
        public PersonalResultScreen createFromParcel(Parcel in) {
            return new PersonalResultScreen(Long.parseLong(in.readString()));
        }

        @Override
        public PersonalResultScreen[] newArray(int size) {
            return new PersonalResultScreen[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(formId));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PersonalResultScreen that = (PersonalResultScreen) o;

        return formId == that.formId;
    }

    @Override
    public int hashCode() {
        return String.valueOf(formId).hashCode();
    }
}