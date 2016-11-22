package com.v3ld1n.commands;

import java.util.List;

public class FAQ {
    private String id;
    private String question;
    private List<String> answer;

    public FAQ(String id, String question, List<String> answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public String getId() {
        return this.id;
    }

    public String getQuestion() {
        return this.question;
    }

    public List<String> getAnswer() {
        return this.answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}