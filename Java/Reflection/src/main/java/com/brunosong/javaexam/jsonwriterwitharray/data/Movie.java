package com.brunosong.javaexam.jsonwriterwitharray.data;

public class Movie {
    private String name;
    private float rating;
    private String[] categories;
    private Actor[] actors;

    public Movie(String name, float rating, String[] categories, Actor[] actors) {
        this.name = name;
        this.rating = rating;
        this.categories = categories;
        this.actors = actors;
    }
}
