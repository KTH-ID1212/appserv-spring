package se.kth.id1212.appserv.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.kth.id1212.appserv.bank.domain.Account;

/**
 * Contains all database access concerning accounts.
 * <p>
 * See: https://projects.spring.io/spring-data/ https://projects.spring.io/spring-data-jpa/
 * VIKTIGAST!! https://docs.spring.io/spring-data/jpa/docs/2.0.10.RELEASE/reference/html/#jpa.repositories
 * https://docs.spring.io/spring-data/commons/docs/current/api/
 * https://docs.spring.io/spring-data/data-jpa/docs/current/api/index.html?org/springframework/data/jpa/repository/JpaRepository.html
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

}
