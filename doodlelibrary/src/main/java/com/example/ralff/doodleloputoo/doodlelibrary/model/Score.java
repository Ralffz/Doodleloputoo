package com.example.ralff.doodleloputoo.doodlelibrary.model;


import java.io.Serializable;
import java.util.Date;



public class Score implements Serializable {
    
    private long id;

    
    private String name;

    
    private int score;

    
    private Date date;

    public Score() {
        this("", 0);
    }

    public Score(String name, int score) {
        this(0, name, score);
    }

    public Score(String name, int score, Date date) {
        this(0, name, score, date);
    }

    public Score(long id, String name, int score) {
        this(id, name, score, new Date());
    }

    public Score(long id, String name, int score, Date date) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        
        this.name = name.trim();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
