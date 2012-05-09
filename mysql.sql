delimiter $$

CREATE TABLE `Task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) DEFAULT NULL,
  `datamd5` varchar(32) DEFAULT NULL,
  `returndatamd5` varchar(32) DEFAULT NULL,
  `datatype` varchar(255) DEFAULT NULL,
  `returndata` longblob,
  `returndatatype` varchar(255) DEFAULT NULL,
  `origin` longtext,
  `name` longtext,
  `dispatchtime` bigint(20) DEFAULT NULL,
  `routingkey` longtext,
  `filename` longtext,
  `data` longblob,
  `returncode` longtext,
  `state` int(11) DEFAULT NULL,
  `timetowait` int(11) DEFAULT NULL,
  `notask` tinyint(1) NOT NULL,
  `commandarguments` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=latin1$$

