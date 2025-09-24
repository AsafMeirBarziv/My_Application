package com.example.myapplication;

public class Score {
    String name;
    int score;

    public Score() {
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "" +
                "name='" + name + '\'' +
                ", (" + score +
                ')';
    }

    public Score(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
