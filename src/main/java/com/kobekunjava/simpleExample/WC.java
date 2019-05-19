package com.kobekunjava.simpleExample;

public class WC {

    private String word;
    private int count;

    public WC() {
    }

    public WC(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "WC{" +
                "word='" + word + '\'' +
                ", count=" + count +
                '}';
    }
}
