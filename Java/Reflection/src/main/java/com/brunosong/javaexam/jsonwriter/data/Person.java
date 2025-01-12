package com.brunosong.javaexam.jsonwriter.data;

public class Person {
    private String name;
    private int age;
    private boolean employed;
    private float salary;
    private Address address;

    public Person(String name, int age, boolean employed, float salary, Address address) {
        this.name = name;
        this.age = age;
        this.employed = employed;
        this.salary = salary;
        this.address = address;
    }
}
