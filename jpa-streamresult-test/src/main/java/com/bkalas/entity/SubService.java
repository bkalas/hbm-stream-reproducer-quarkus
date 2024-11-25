package com.bkalas.entity;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.*;

@Entity
@Table(name = "sub_service")
public class SubService {
    
    @Serial
    private static final long serialVersionUID = -4484848484848484L;
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;

    /**
     * String ID of entity
     */
    @Id
    @Column(name = "GUID")
    private String id = UUID.randomUUID().toString();
    /**
     * Gets the GUID.
     *
     * @return the GUID.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the GUID.
     *
     * @param id the new GUID.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * {@inheritDoc }
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) {
//            return true;
//        }
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        SubService other = (SubService) obj;
//        Object guid = getId();
//        Object otherGuid = other.getId();
//
//        if (guid == null) {
//            if (otherGuid != null) {
//                return false;
//            } else {
//                return super.equals(obj);
//            }
//        } else
//            return guid.equals(otherGuid);
//    }
//
//    /**
//     * {@inheritDoc }
//     *
//     * @see java.lang.Object#hashCode()
//     */
//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + Objects.hashCode(getId());
//        return result;
//    }

    /**
     * Overwrite the {@code toString} method for the logger.
     *
     * @return the className:ID
     */
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":" + getId();
    }
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