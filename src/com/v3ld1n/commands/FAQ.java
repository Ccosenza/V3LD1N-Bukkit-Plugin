package com.v3ld1n.commands;

public class FAQ {
    private int id;
    private String question;
    private String answer;

    public FAQ(int id, String name, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public FAQ(int id, String name, String question, String answer, String nameColor, String questionColor, String answerColor) {
        this(id, name, question, answer);
    }

    public int getId() {
        return this.id;
    }

    public String getQuestion() {
        return this.question;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}