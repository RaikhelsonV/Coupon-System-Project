package com.example.MyProject.entity;

import javax.persistence.*;

@MappedSuperclass
public abstract class Client {

    static final int NO_ID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }


}
