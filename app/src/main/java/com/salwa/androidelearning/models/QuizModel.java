package com.salwa.androidelearning.models;

import android.os.Parcel;
import android.os.Parcelable;

public class QuizModel implements Parcelable {



    String question;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String correctAnswer;

    public QuizModel() {
    }

    public QuizModel(String question, String answer1, String answer2, String answer3, String answer4, String correctAnswer) {
        this.question = question;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.correctAnswer = correctAnswer;
    }


    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.question);
        dest.writeString(this.answer1);
        dest.writeString(this.answer2);
        dest.writeString(this.answer3);
        dest.writeString(this.answer4);
        dest.writeString(this.correctAnswer);
    }

    protected QuizModel(Parcel in) {
        this.question = in.readString();
        this.answer1 = in.readString();
        this.answer2 = in.readString();
        this.answer3 = in.readString();
        this.answer4 = in.readString();
        this.correctAnswer = in.readString();
    }

    public static final Parcelable.Creator<QuizModel> CREATOR = new Parcelable.Creator<QuizModel>() {
        @Override
        public QuizModel createFromParcel(Parcel source) {
            return new QuizModel(source);
        }

        @Override
        public QuizModel[] newArray(int size) {
            return new QuizModel[size];
        }
    };

    @Override
    public String toString() {
        return "QuizModel{" +
                "question='" + question + '\'' +
                '}';
    }
}
