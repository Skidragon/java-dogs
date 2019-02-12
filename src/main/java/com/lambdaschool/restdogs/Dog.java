package com.lambdaschool.restdogs;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data // creates getters, setters, toString
@Entity
public class Dog {
    private @Id
    @GeneratedValue Long id; //primary key automatically populated
    private String name;
    private int avgWeight;
    private boolean isForApartment;

    //needed for JPA
    public Dog() {

    }

    public Dog(String name, int avgWeight, boolean isForApartment) {
        this.name = name;
        this.avgWeight = avgWeight;
        this.isForApartment = isForApartment;
    }
}
