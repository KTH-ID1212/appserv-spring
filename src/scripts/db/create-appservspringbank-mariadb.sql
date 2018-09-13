/*
 * Assumes the database already exists. The spring app uses the database name `appservspringbank`,
 * create such a database before running this script. Run the script with the command
 * mysql appservspringbank < src/scripts/db/create-appservspringbank-mariadb.sql -p
 */

--
-- Table structure for table `HOLDER`
--
DROP TABLE IF EXISTS `HOLDER`;

CREATE TABLE `HOLDER` (
  `HLD_ID` bigint(20) NOT NULL,
  `HLD_NO` bigint(20) NOT NULL,
  `HLD_NAME` varchar(50) NOT NULL,
  `HLD_OPTLOCK_VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`HLD_ID`),
  UNIQUE KEY `HLD_NO` (`HLD_NO`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ACCOUNT`
--
DROP TABLE IF EXISTS `ACCOUNT`;

CREATE TABLE `ACCOUNT` (
  `ACCT_ID` bigint(20) NOT NULL,
  `FK_ACCOUNT_HOLDER` bigint(20) NOT NULL,
  `ACCT_NO` bigint(20) NOT NULL,
  `ACCT_BALANCE` int(11) NOT NULL,
  `ACCT_OPTLOCK_VERSION` int(11) DEFAULT NULL,
  PRIMARY KEY (`ACCT_ID`),
  UNIQUE KEY `ACCT_NO` (`ACCT_NO`),
  KEY `FK_ACCOUNT_HOLDER` (`FK_ACCOUNT_HOLDER`),
  CONSTRAINT `FK_ACCOUNT_HOLDER` FOREIGN KEY (`FK_ACCOUNT_HOLDER`) REFERENCES `HOLDER` (`HLD_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `BANK_SEQUENCE`
--
DROP TABLE IF EXISTS `BANK_SEQUENCE`;
CREATE TABLE `BANK_SEQUENCE` (
  `next_not_cached_value` bigint(21) NOT NULL,
  `minimum_value` bigint(21) NOT NULL,
  `maximum_value` bigint(21) NOT NULL,
  `start_value` bigint(21) NOT NULL COMMENT 'start value when sequences is created or value if RESTART is used',
  `increment` bigint(21) NOT NULL COMMENT 'increment value',
  `cache_size` bigint(21) unsigned NOT NULL,
  `cycle_option` tinyint(1) unsigned NOT NULL COMMENT '0 if no cycles are allowed, 1 if the sequence should begin a new cycle when maximum_value is passed',
  `cycle_count` bigint(21) NOT NULL COMMENT 'How many cycles have been done'
) ENGINE=InnoDB SEQUENCE=1;
