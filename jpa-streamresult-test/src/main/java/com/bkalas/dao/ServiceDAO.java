package com.bkalas.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bkalas.entity.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@ Transactional(Transactional.TxType.SUPPORTS)
@Slf4j
public class ServiceDAO {

    @Inject
    EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRED)
    public Stream<Service> findIdRangeWIthSubServiceUsingStream(int offset, int limit, EntityGraph<?> entityGraph) {
        List<Object> serviceIds = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        for (int i = offset; i < offset+limit; i++) {
            serviceIds.add(i);
        }

        return findAllWithSubServicesAsStream(serviceIds, entityGraph);
    }

    @Transactional(Transactional.TxType.REQUIRED)
    public List<Service> findIdRangeWIthSubServiceUsingList(int offset, int limit, EntityGraph<?> entityGraph) {
        List<Object> serviceIds = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        for (int i = offset; i < offset+limit; i++) {
            serviceIds.add(i);
        }



        return findAllWithSubServicesAsList(serviceIds, entityGraph);
    }
    
    public Stream<Service> findAllWithSubServicesAsStream(List<Object> serviceIds, EntityGraph<?> entityGraph) {
        CriteriaQuery<Service> cQuery = entityManager.getCriteriaBuilder().createQuery(Service.class);
        cQuery = cQuery.where(cQuery.from(Service.class).get("businessId").in(serviceIds));
        
        TypedQuery<Service> typedQuery = entityManager.createQuery(cQuery);
        if (entityGraph != null) {
            typedQuery.setHint("jakarta.persistence.loadgraph", entityGraph);
        }
        return typedQuery.getResultStream();


    }

    public List<Service> findAllWithSubServicesAsList(List<Object> serviceIds, EntityGraph<?> entityGraph) {
        CriteriaQuery<Service> cQuery = entityManager.getCriteriaBuilder().createQuery(Service.class);
        cQuery = cQuery.where(cQuery.from(Service.class).get("businessId").in(serviceIds));

        TypedQuery<Service> typedQuery = entityManager.createQuery(cQuery);
        if (entityGraph != null) {
            typedQuery.setHint("jakarta.persistence.loadgraph", entityGraph);
        }
        return typedQuery.getResultList();

    }
}