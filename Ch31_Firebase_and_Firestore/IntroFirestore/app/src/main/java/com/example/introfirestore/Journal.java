package com.example.introfirestore;

public class Journal {
    private String title;
    private String thought;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    // We must have an empty constructor for Firestore
    public Journal() {

    }

    public Journal(String title, String thought) {
        this.title = title;
        this.thought = thought;
    }

}
