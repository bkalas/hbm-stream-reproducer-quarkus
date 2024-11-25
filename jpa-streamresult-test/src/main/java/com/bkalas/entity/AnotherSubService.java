package com.bkalas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serial;

@Entity
@Table(name = "another_sub_service")
public class AnotherSubService extends TraceableEntity {

    @Serial
    private static final long serialVersionUID = -4484848484848484L;
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    // Constructors
    public AnotherSubService() {}

    public AnotherSubService(String name, String description) {
        this.name = name;
        this.description = description;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}