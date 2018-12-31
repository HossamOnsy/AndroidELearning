package com.salwa.androidelearning;

import android.os.Parcel;
import android.os.Parcelable;

public class students implements Parcelable {
    private String name ;
    private String teacher;

    public students(String name, String teacher) {
        this.name = name;
        this.teacher = teacher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.teacher);
    }

    protected students(Parcel in) {
        this.name = in.readString();
        this.teacher = in.readString();
    }

    public static final Parcelable.Creator<students> CREATOR = new Parcelable.Creator<students>() {
        @Override
        public students createFromParcel(Parcel source) {
            return new students(source);
        }

        @Override
        public students[] newArray(int size) {
            return new students[size];
        }
    };
}
