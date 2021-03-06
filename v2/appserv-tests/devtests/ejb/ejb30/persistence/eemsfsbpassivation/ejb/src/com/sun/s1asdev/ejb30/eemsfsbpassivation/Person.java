package com.sun.s1asdev.ejb30.eemsfsbpassivation;

import javax.persistence.Entity;

import javax.persistence.Table;

import javax.persistence.Id;

@Entity
@Table(name = "EJB30_PERSISTENCE_EEMPASSIVATION_PERSON")
public class Person implements java.io.Serializable {

    @Id
    String name;

    String data;

    public Person() {

    }

    public Person(String name) {

        this.name = name;

        this.data = "data: " + name;

    }

    @Override
    public String toString() {

        return "Person: (name=" + name + "; data= " + data + ")";

    }

}

