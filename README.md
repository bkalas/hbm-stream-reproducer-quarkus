# hbm-stream-reproducer
Repository reproduces bug found in `jakarta.persistence.TypedQuery#getResultStream` method.
It was only possible to create reproducer using real database (postgres), not with inmemory db like H2.

Problem is when you are selecting mutliple entities using graph which fetches also subcollection of the main entity.
See entities [Service](./jpa-streamresult-test/src/main/java/com/bkalas/entity/Service.java).
and [SubService](./jpa-streamresult-test/src/main/java/com/bkalas/entity/SubService.java).
If you then access result using ```getResultStrean``` sometimes for some entities you will not get all children from subcollection fetched.

[ServiceDaoTest](./jpa-streamresult-test/src/test/java/com/bkalas/dao/ServiceDAOTest.java) contains 2 test with euivalent functionality, with only difference that one test uses ```getResultStream``` and other test uses ```getResultList```. 
Both tests are selecting entities by set of ids and fetching subcollection using entity graph. They are asserting count of elements in collection. (comparison is done by checking exepected count stored in parent).
Test using stream will fail, test using list will pass.

### How to simulate issue.

Prerequisities: 
* Instance of postgres database with schema ```hbm_reproducer```, user ```hbm_reproducer```, password ```hbm_reproducer```. 
* JDK >= 17

Simulation:
1. execute [import.sql](./jpa-streamresult-test/src/main/resources/import.sql)
2. run tests in jpa-streamresult-test repository by executing ```mvn clean install```

Test using stream will fail, test using list will pass.