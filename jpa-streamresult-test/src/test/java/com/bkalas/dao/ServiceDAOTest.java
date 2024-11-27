package com.bkalas.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import com.bkalas.entity.Service;
import com.bkalas.entity.SubService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityGraph;
import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
class ServiceDAOTest {

    @Inject
    EntityManager entityManager;

    @Inject
    ServiceDAO serviceDAO;

    static int BATCH_SIZE = 6000;


    @Test
    @Transactional(Transactional.TxType.REQUIRED)
    void testServiceWithSubServicesUsingStream() {
        EntityGraph<?> graph = entityManager.getEntityGraph("Service.withSubServices");

        AtomicBoolean isNext = new AtomicBoolean(true);
        int last = 0;
        AtomicInteger noOfDiff = new AtomicInteger();

        while (isNext.get()) {

            Stream<Service> serviceStream = serviceDAO.findIdRangeWIthSubServiceUsingStream(last, BATCH_SIZE, graph);
            last += BATCH_SIZE;

            isNext.set(false);
            serviceStream.forEach((service) -> {
                isNext.set(true);
                if (service.getExpectedSubServiceCount() != service.getSubServices().size()) {
                    System.out.println("id " + service.getBusinessId() + " expected sub services count " + service.getExpectedSubServiceCount() + " but got sub services count " + service.getSubServices().size());
                    noOfDiff.getAndIncrement();
                }
            });
        }
        System.out.println("noOfDiff=" + noOfDiff);
        assertEquals(0, noOfDiff.get());
    }

    @Test
    @Transactional(Transactional.TxType.REQUIRED)
    void testServiceWithSubServicesUsingList() {
        EntityGraph<?> graph = entityManager.getEntityGraph("Service.withSubServices");

        AtomicBoolean isNext = new AtomicBoolean(true);
        int last = 0;
        AtomicInteger noOfDiff = new AtomicInteger();

        while (isNext.get()) {

            List<Service> serviceStream = serviceDAO.findIdRangeWIthSubServiceUsingList(last, BATCH_SIZE, graph);
            last += BATCH_SIZE;
            //System.out.println("----serviceStream I=" + last + ", size=" + serviceStream.size());

            isNext.set(false);
            serviceStream.forEach((service) -> {
                isNext.set(true);
                if (service.getExpectedSubServiceCount() != service.getSubServices().size()) {
                    System.out.println("id " + service.getBusinessId() + " expected sub services count " + service.getExpectedSubServiceCount() + " but got sub services count " + service.getSubServices().size());
                    noOfDiff.getAndIncrement();
                }
            });
            assertEquals(0, noOfDiff.get());
        }
        System.out.println("noOfDiff=" + noOfDiff);
    }
}
