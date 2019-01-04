package com.salwa.androidelearning.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NameIdModel implements Parcelable {
    private  String name;
    private String ID;
    private String Progress;

    public NameIdModel() {
    }

    public NameIdModel(String name, String ID, String progress) {
        this.name = name;
        this.ID = ID;
        Progress = progress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.ID);
        dest.writeString(this.Progress);
    }

    protected NameIdModel(Parcel in) {
        this.name = in.readString();
        this.ID = in.readString();
        this.Progress = in.readString();
    }

    public static final Parcelable.Creator<NameIdModel> CREATOR = new Parcelable.Creator<NameIdModel>() {
        @Override
        public NameIdModel createFromParcel(Parcel source) {
            return new NameIdModel(source);
        }

        @Override
        public NameIdModel[] newArray(int size) {
            return new NameIdModel[size];
        }
    };
}
