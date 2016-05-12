
--
-- Table structure for table `Component`
--

DROP TABLE IF EXISTS `Component`;
CREATE TABLE `Component` (
  `componentGroupId` int(11) NOT NULL,
  `componentNumber` int(11) NOT NULL,
  `barcode` varchar(45) NOT NULL,
  `status` int(11) NOT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`barcode`),
  UNIQUE KEY `componentNumber_UNIQUE` (`componentGroupId`,`componentNumber`),
  UNIQUE KEY `barcode_UNIQUE` (`barcode`),
  KEY `komponentType` (`componentGroupId`),
  CONSTRAINT `komponenter_ibfk_1` FOREIGN KEY (`componentGroupId`) REFERENCES `ComponentGroup` (`componentGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `ComponentGroup`
--

DROP TABLE IF EXISTS `ComponentGroup`;
CREATE TABLE `ComponentGroup` (
  `componentGroupId` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `standardLoanDuration` varchar(45) DEFAULT NULL,
  `status` int(11) NOT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`componentGroupId`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

--
-- Table structure for table `Loan`
--

DROP TABLE IF EXISTS `Loan`;
CREATE TABLE `Loan` (
  `loanId` int(11) NOT NULL AUTO_INCREMENT,
  `barcode` varchar(45) NOT NULL,
  `studentId` varchar(7) NOT NULL,
  `loanDate` datetime NOT NULL,
  `dueDate` datetime NOT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `deliveredTo` varchar(45) DEFAULT NULL,
  `mailCount` int(11) DEFAULT '0',
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`loanId`),
  KEY `komponentId` (`barcode`),
  KEY `fk_Loan_Students1_idx` (`studentId`),
  CONSTRAINT `fk_Loan_Students1` FOREIGN KEY (`studentId`) REFERENCES `Student` (`studentId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `udlån_ibfk_1` FOREIGN KEY (`barcode`) REFERENCES `Component` (`barcode`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=latin1;

--
-- Table structure for table `Student`
--

DROP TABLE IF EXISTS `Student`;
CREATE TABLE `Student` (
  `studentId` varchar(7) NOT NULL,
  `name` varchar(45) NOT NULL,
  `status` int(11) NOT NULL,
  `lastModified` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`studentId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Table structure for table `Token`
--

DROP TABLE IF EXISTS `Token`;
CREATE TABLE `Token` (
  `token` varchar(500) NOT NULL DEFAULT '',
  `creationTime` mediumtext,
  `expirationTime` mediumtext,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
