package com.abc.multiplechoicetest;

public class Category {
    public static final int PROGRAMMING=1;
    public static final int DESCRITEMATH=2;
    public static final int CURRENTFFAIR=3;

    private int id;
    private String name;



    public Category(){}

    public Category(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

   // @androidx.annotation.NonNull
    @Override
    public String toString() {
        return getName();
    }
}
