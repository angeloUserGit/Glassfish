package com.sun.s1asdev.ejb.ejb30.persistence.eem_1slsb_2sfsbs;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

@Entity
@Table(name = "EJB30_PERSISTENCE_EEM_1SLSB_2SFSBS_PERSON")
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

    public Person(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    @Override 
    public String toString() {
        return "Person: (name=" + name + "; data= " + data + ")";
    }
}
