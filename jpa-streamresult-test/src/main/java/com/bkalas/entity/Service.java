package com.bkalas.entity;

import jakarta.persistence.*;

import java.io.Serial;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@NamedEntityGraph(
    name = "Service.withSubServices",
    attributeNodes = @NamedAttributeNode("subServices")
)
public class Service extends AbstractTraceableEntity<Long> {
    @Serial
    private static final long serialVersionUID = -4484848484848484L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "business_id")
    private Long businessId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "description")
    private String description;

    @Column(name = "expected_sub_service_count")
    private int expectedSubServiceCount;

    @Column(name = "GUID")
    private String guid = UUID.randomUUID().toString();
    
    @JoinColumn(name = "service_id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SubService> subServices;
    
    // Constructors
    public Service() {}
    
    public Service(String name, String description, int expectedSubServiceCount ) {
        this.name = name;
        this.description = description;
        this.expectedSubServiceCount = expectedSubServiceCount;
    }
    
    // Getters and setters
    public Long getBusinessId() {
        return businessId;
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
    
    public Set<SubService> getSubServices() {
        return subServices;
    }
    
    public void addSubService(SubService subService) {
        if (subServices == null) {
            subServices = new HashSet<>();
        }
        subServices.add(subService);
    }

    public int getExpectedSubServiceCount() {
        return expectedSubServiceCount;
    }   

    public void setExpectedSubServiceCount(int expectedSubServiceCount) {
        this.expectedSubServiceCount = expectedSubServiceCount;
    }   

    public String getGuid() {
        return guid;
    } 

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Service service = (Service) o;
        return guid.equals(service.guid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guid);
    }
} 

