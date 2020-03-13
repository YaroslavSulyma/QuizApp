package com.example.quizzapp;

import com.google.firebase.firestore.DocumentId;

public class QuizListModel {

    @DocumentId
    private String uiz_id;
    private String name, description, image, level, visibility;
    private long questions;

    public QuizListModel() {
    }

    public QuizListModel(String uiz_id, String name, String description, String image, String level, String visibility, long questions) {
        this.uiz_id = uiz_id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.level = level;
        this.visibility = visibility;
        this.questions = questions;
    }

    public String getUiz_id() {
        return uiz_id;
    }

    public void setUiz_id(String uiz_id) {
        this.uiz_id = uiz_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public long getQuestions() {
        return questions;
    }

    public void setQuestions(long questions) {
        this.questions = questions;
    }
}
