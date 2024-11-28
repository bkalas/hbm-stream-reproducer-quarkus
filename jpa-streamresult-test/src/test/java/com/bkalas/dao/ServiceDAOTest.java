package com.bkalas.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bkalas.entity.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityGraph;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ServiceDAOTest {


    ServiceDAO serviceDAO;

    static int BATCH_SIZE = 6000;

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("templatePU");
        serviceDAO = new ServiceDAO();
    }

    @AfterEach
    void destroy() {
        entityManagerFactory.close();
    }

    @Test
    public void testServiceWithSubServicesUsingStream() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EntityGraph<?> graph = entityManager.getEntityGraph("Service.withSubServices");

        AtomicBoolean isNext = new AtomicBoolean(true);
        int last = 0;
        AtomicInteger noOfDiff = new AtomicInteger();

        while (isNext.get()) {

            Stream<Service> serviceStream = serviceDAO.findIdRangeWIthSubServiceUsingStream(last, BATCH_SIZE, graph, entityManager);
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
        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println("noOfDiff=" + noOfDiff);
        assertEquals(0, noOfDiff.get());
    }

    @Test
    void testServiceWithSubServicesUsingList() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EntityGraph<?> graph = entityManager.getEntityGraph("Service.withSubServices");

        AtomicBoolean isNext = new AtomicBoolean(true);
        int last = 0;
        AtomicInteger noOfDiff = new AtomicInteger();

        while (isNext.get()) {

            List<Service> serviceStream = serviceDAO.findIdRangeWIthSubServiceUsingList(last, BATCH_SIZE, graph, entityManager);
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
        entityManager.getTransaction().commit();
        entityManager.close();
        System.out.println("noOfDiff=" + noOfDiff);
    }
}
