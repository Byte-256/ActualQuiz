package io.github.byte256.actualgame.utils;

public class Quiz {

    private final String question;
    private final String[] options;
    private final String correctAnswer;

    public Quiz(String question, String[] options, String correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

}
