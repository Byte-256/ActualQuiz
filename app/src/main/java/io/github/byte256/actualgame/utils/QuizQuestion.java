package io.github.byte256.actualgame.utils;

public class QuizQuestion {
    private String question;
    private String answer;

    public QuizQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }
}