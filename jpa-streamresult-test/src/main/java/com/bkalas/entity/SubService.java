package com.bkalas.entity;

import java.io.Serial;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_service")
public class SubService extends TraceableEntity {
    
    @Serial
    private static final long serialVersionUID = -4484848484848484L;
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;
    
        
    // Constructors
    public SubService() {}
    
    public SubService(String name, String description) {
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