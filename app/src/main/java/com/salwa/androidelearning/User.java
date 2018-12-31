package com.salwa.androidelearning;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    public String name;
    public String email;
    public String age;
    public String gender;
    public String role;
    public User() {
    }

    public User(String name, String email, String age, String gender, String role) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.email);
        dest.writeString(this.age);
        dest.writeString(this.gender);
        dest.writeString(this.role);
    }

    protected User(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.age = in.readString();
        this.gender = in.readString();
        this.role = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
