package com.salwa.androidelearning.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentModel implements Parcelable {

    private String name;
    private String email;
    private String age;
    private String gender;
    private String role;
    private String teacher;
    private String myclass;
    private String ID;
    private String contactNumber;

    public StudentModel() {
    }

    public StudentModel( String ID,String name, String email, String age, String gender, String role, String teacher, String contactNumber) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender = gender;
        this.role = role;
        this.teacher = teacher;
        this.ID = ID;
        this.contactNumber = contactNumber;
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

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getMyclass() {
        return myclass;
    }

    public void setMyclass(String myclass) {
        this.myclass = myclass;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
        dest.writeString(this.teacher);
        dest.writeString(this.myclass);
        dest.writeString(this.ID);
        dest.writeString(this.contactNumber);
    }

    protected StudentModel(Parcel in) {
        this.name = in.readString();
        this.email = in.readString();
        this.age = in.readString();
        this.gender = in.readString();
        this.role = in.readString();
        this.teacher = in.readString();
        this.myclass = in.readString();
        this.ID = in.readString();
        this.contactNumber = in.readString();
    }

    public static final Parcelable.Creator<StudentModel> CREATOR = new Parcelable.Creator<StudentModel>() {
        @Override
        public StudentModel createFromParcel(Parcel source) {
            return new StudentModel(source);
        }

        @Override
        public StudentModel[] newArray(int size) {
            return new StudentModel[size];
        }
    };
}
