-- Assumes the database already exists. The spring app uses the database name `appservspringbank`,
-- create such a database before running this script. Run the script with the command
-- mysql appservspringbank < src/scripts/db/create-appservspringbank-mariadb.sql -p


--
-- Drop all tables;
--
DROP TABLE IF EXISTS `BANK_SEQUENCE`;
DROP TABLE IF EXISTS `ACCOUNT`;
DROP TABLE IF EXISTS `HOLDER`;

--
-- Create for table `HOLDER`
--
CREATE TABLE `HOLDER` (
  `HLD_ID` bigint(20) NOT NULL,
  `HLD_NO` bigint(20) NOT NULL,
  `HLD_NAME` varchar(50) NOT NULL,
  `HLD_OPTLOCK_VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`HLD_ID`),
  UNIQUE KEY `HLD_NO` (`HLD_NO`)
);

--
-- Create table `ACCOUNT`
--
CREATE TABLE `ACCOUNT` (
  `ACCT_ID` bigint NOT NULL,
  `ACCT_NO` bigint UNIQUE NOT NULL,
  `ACCT_BALANCE` integer NOT NULL,
  `ACCT_OPTLOCK_VERSION` integer,
  `FK_ACCOUNT_HOLDER` bigint not null,
  PRIMARY KEY (`ACCT_ID`));

ALTER TABLE `ACCOUNT`
  ADD CONSTRAINT `FK_ACCOUNT_HOLDER` FOREIGN KEY (`FK_ACCOUNT_HOLDER`) references `HOLDER` (`HLD_ID`);

--
-- Create table `BANK_SEQUENCE`
--
CREATE TABLE `BANK_SEQUENCE` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
);
