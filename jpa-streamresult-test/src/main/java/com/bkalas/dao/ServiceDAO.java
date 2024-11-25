package com.bkalas.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.function.Function;

import com.bkalas.entity.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
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

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Map<Long, Service> findAllWithSubServicesAsMap(int offset, int limit, EntityGraph<?> entityGraph) {
        List<Object> serviceIds = new ArrayList<>();
      
        for (int i = offset; i < limit; i++) {
            serviceIds.add(i);
        }
        Stream<Service> serviceStream = findAllWithSubServicesAsStream(serviceIds, entityGraph);
                Map<Long, Service> serviceMap = new HashMap<>();
        serviceStream.forEach(service -> {
            serviceMap.put(service.getBusinessId(), service);
        });
        return serviceMap;
    }

    
    public Stream<Service> findAllWithSubServicesAsStream(List<Object> serviceIds, EntityGraph<?> entityGraph) {

        log.info("serviceIds: {}", serviceIds.size());
        CriteriaQuery<Service> cQuery = entityManager.getCriteriaBuilder().createQuery(Service.class);
        cQuery = cQuery.where(cQuery.from(Service.class).get("businessId").in(serviceIds));
        
        TypedQuery<Service> typedQuery = entityManager.createQuery(cQuery);
        if (entityGraph != null) {
            typedQuery.setHint("jakarta.persistence.loadgraph", entityGraph);
        }
        return typedQuery.getResultStream();

        // return entityManager.createQuery("SELECT s FROM Service s WHERE s.id IN
        // :serviceIds", Service.class)
        // .setHint("jakarta.persistence.loadgraph", graph)
        // .setParameter("serviceIds", serviceIds)

        // .getResultStream();

    }
}