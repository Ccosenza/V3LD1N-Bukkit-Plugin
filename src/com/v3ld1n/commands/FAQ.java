package com.v3ld1n.commands;

public class FAQ {
    int id;
    String name;
    String question;
    String answer;
    String nameColor;
    String questionColor;
    String answerColor;

    public FAQ(int id, String name, String question, String answer) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.answer = answer;
        this.nameColor = "white";
        this.questionColor = "white";
        this.answerColor = "white";
    }

    public FAQ(int id, String name, String question, String answer, String nameColor, String questionColor, String answerColor) {
        this.id = id;
        this.name = name;
        this.question = question;
        this.answer = answer;
        this.nameColor = nameColor;
        this.questionColor = questionColor;
        this.answerColor = answerColor;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public String getNameColor() {
        return this.nameColor;
    }

    public String getQuestionColor() {
        return this.questionColor;
    }

    public String getAnswerColor() {
        return this.answerColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setNameColor(String nameColor) {
        this.nameColor = nameColor;
    }

    public void setQuestionColor(String questionColor) {
        this.questionColor = questionColor;
    }

    public void setAnswerColor(String answerColor) {
        this.answerColor = answerColor;
    }
}