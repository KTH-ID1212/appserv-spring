# appserv-spring

Code examples used in the Network Programming (ID1212) course at KTH. This repository contains the code example used in the section on application servers with Spring Web MVC. The example is a very simple bank application, where it is possible to create and search for accounts, and to deposit and withdraw money.

## To run with mariadb in docker.

1. Start the mariadb container.
...docker run --name bank-mariadb -d -e MYSQL_ROOT_PASSWORD=jpa mariadb:10.3

2. Start the mysql client against the mariadb container.
...docker run --link bank-mariadb:mysql -it --rm mariadb sh -c 'exec mysql -h${MYSQL_PORT_3306_TCP_ADDR} -P${MYSQL_PORT_3306_TCP_PORT} -uroot -p'

......1. Create the database
.........create database appservspringbank;
......2. Create the tables
.........use appservspringbank;
.........<run the provided script>

3. Start the bank server
...docker run --link bank-mariadb:mysql -p8080:8080 -e spring.datasource.url='jdbc:mariadb://${MYSQL_PORT_3306_TCP_ADDR}:${MYSQL_PORT_3306_TCP_PORT}/appservspringbank?serverTimezone=UTC' -e spring.datasource.username=root -e spring.datasource.password=jpa se.kth.id1212/appserv-spring:2.0
