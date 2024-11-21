package com.bkalas.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bkalas.entity.Service;
import com.bkalas.entity.SubService;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityGraph;
import jakarta.transaction.Transactional;

@QuarkusTest
class ServiceDAOTest {

	@Inject
	EntityManager entityManager;

	@Inject
	ServiceDAO serviceDAO;

	List<Object> serviceIds = new ArrayList<>();

	List<Object> diiffServiceIds = new ArrayList<>();

	//@BeforeEach
	//@Transactional(Transactional.TxType.REQUIRES_NEW)
	void setUp() throws FileNotFoundException {

		StringBuilder sql = new StringBuilder();
		Service service1 = new Service("Main Service 1", "Primary service offering", 2);
		Service service2 = new Service("Main Service 2", "Secondary service offering", 6);
		Service service3 = new Service("Main Service 3", "Third service offering", 0);
		Service service4 = new Service("Main Service 4", "Fourth service offering", 6);
		Service service5 = new Service("Main Service 5", "5TH service offering", 6);
		Service service6 = new Service("Main Service 6", "6TH service offering", 6);
		Service service7 = new Service("Main Service 7", "7TH service offering", 6);

		List<Service> additionalServices = new ArrayList<>();
		for (int i = 0; i < 4000; i++) {
			int childCount = Math.abs(new Random().nextInt(6));
			additionalServices.add(new Service("Main Service " + (i + 8), "Additional service offering", childCount));
			for (int j = 0; j < childCount; j++) {
				SubService sub = new SubService("Sub " + j, "Sub " + j + " description");
				additionalServices.get(i).addSubService(sub);
			}
			entityManager.persist(additionalServices.get(i));
			serviceIds.add(additionalServices.get(i).getBusinessId());
			sql.append("INSERT INTO service (business_id, name, description, expected_sub_service_count,optlock	) VALUES (")
					.append(additionalServices.get(i).getBusinessId()).append(", '")
					.append(additionalServices.get(i).getName()).append("', '")
					.append(additionalServices.get(i).getDescription()).append("', ")
					.append(additionalServices.get(i).getExpectedSubServiceCount()).append(", 0);");
			sql.append("\n");
			if (additionalServices.get(i).getSubServices() != null) {
				for (SubService sub : additionalServices.get(i).getSubServices()) {
					sql.append("INSERT INTO sub_service (guid, name, description, service_id,optlock) VALUES (")
							.append("'").append(sub.getId()).append("', '")
							.append(sub.getName()).append("', '")
							.append(sub.getDescription()).append("', ")
							.append(additionalServices.get(i).getBusinessId()).append(", 0);");
							
					sql.append("\n");
				}
			}

		}
		System.out.println(sql);
		File file = new File("sql.sql");
		try (PrintWriter out = new PrintWriter(file)) {
			out.println(sql);
		}

		SubService sub1 = new SubService("Sub 1", "First sub-service");
		SubService sub2 = new SubService("Sub 2", "Second sub-service");
		SubService sub3 = new SubService("Sub 3", "Third sub-service");

		service1.addSubService(sub1);
		service1.addSubService(sub2);
		service2.addSubService(sub3);

		for (int i = 1; i < service2.getExpectedSubServiceCount(); i++) {
			SubService sub = new SubService("Sub " + i, "Sub " + i + " description");
			service2.addSubService(sub);
		}
		for (int i = 0; i < service4.getExpectedSubServiceCount(); i++) {
			SubService sub = new SubService("Sub " + i, "Sub " + i + " description");
			service4.addSubService(sub);
		}
		for (int i = 0; i < service5.getExpectedSubServiceCount(); i++) {
			SubService sub = new SubService("Sub " + i, "Sub " + i + " description");
			service5.addSubService(sub);
		}
		for (int i = 0; i < service6.getExpectedSubServiceCount(); i++) {
			SubService sub = new SubService("Sub " + i, "Sub " + i + " description");
			service6.addSubService(sub);
		}
		for (int i = 0; i < service7.getExpectedSubServiceCount(); i++) {
			SubService sub = new SubService("Sub " + i, "Sub " + i + " description");
			service7.addSubService(sub);
		}

		entityManager.persist(service1);
		entityManager.persist(service2);
		entityManager.persist(service3);
		entityManager.persist(service4);
		entityManager.persist(service5);
		entityManager.persist(service6);
		entityManager.persist(service7);
		serviceIds.add(service1.getBusinessId());
		serviceIds.add(service2.getBusinessId());
		serviceIds.add(service3.getBusinessId());
		serviceIds.add(service4.getBusinessId());
		serviceIds.add(service5.getBusinessId());
		serviceIds.add(service6.getBusinessId());
		serviceIds.add(service7.getBusinessId());

		// Now test the DAO
		// entityManager.getTransaction().begin();

		diiffServiceIds = new ArrayList<>();

		diiffServiceIds.addAll(serviceIds.subList(300, 400));

	}

	@Test
	@Transactional(Transactional.TxType.SUPPORTS)
	void testServiceWithSubServices() {
		// First, create and persist some test data

		EntityGraph<?> graph = entityManager.getEntityGraph("Service.withSubServices");

		for (int i = 0; i < 10; i++) {
			diiffServiceIds = new ArrayList<>();
		
			Map<Long, Service> serviceMap = serviceDAO.findAllWithSubServicesAsMap(i * 100, (i + 1) * 100, graph);

			serviceMap.forEach((key, value) -> {
				//System.out.println(key + " -> " + value.getName());
				assertEquals(value.getExpectedSubServiceCount(), value.getSubServices().size());
			});
		}

		//
	}
}
