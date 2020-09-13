BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS `user` (
	`id`	INTEGER,
	`first_name`	TEXT,
	`last_name`	TEXT,
	`email`	TEXT,
	`phone_number`	TEXT,
	`password`	TEXT,
	`gender`	TEXT,
	`birthdate`	TEXT,
	`doctor`	TEXT,
	PRIMARY KEY(`id`)
);
INSERT INTO `user` VALUES (1,'Kanita', 'Dervić', 'kdervic@faks.com', '062/111/222', 'Test123', 'F', '23-1-1999','admin');
INSERT INTO `user` VALUES (2,'Sara', 'Sarić', 'ssaric@faks.com', '062/225/883', 'Test123', 'F', '10-10-2003','user');
INSERT INTO `user` VALUES (3,'Himzo', 'Himzić', 'hhimzic@faks.com', '062/555/222', 'Test123', 'M', '14-6-2009','user');
CREATE TABLE IF NOT EXISTS `appointment` (
	`appointment_id`	INTEGER,
	`doctor_id`	INTEGER,
	`patient_id`	INTEGER,
	`appointment_date`	TEXT,
	`disease_id`	INTEGER,
	PRIMARY KEY(`appointment_id`)
);
INSERT INTO `appointment` VALUES (1,1,2,'3-9-2021',1);
INSERT INTO `appointment` VALUES (2,1,3,'9-9-2021',2);
INSERT INTO `appointment` VALUES (3,1,2,'23-1-2021',3);
CREATE TABLE IF NOT EXISTS `disease` (
	`disease_id`	INTEGER,
	`patient_id`	INTEGER,
	`disease_name`	TEXT,
	PRIMARY KEY(`disease_id`)
);
INSERT INTO `disease` VALUES (1,2,'Depression');
INSERT INTO `disease` VALUES (2,2,'Common cold');
INSERT INTO `disease` VALUES (3,3,'Back pain');
COMMIT;
