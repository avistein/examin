-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: examin_db
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_batch`
--

DROP TABLE IF EXISTS `t_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_batch` (
  `v_batch_id` varchar(255) NOT NULL,
  `v_batch_name` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  PRIMARY KEY (`v_batch_id`),
  KEY `fk_course_id_idx` (`v_course_id`),
  CONSTRAINT `fk_coursesId` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_batch`
--

LOCK TABLES `t_batch` WRITE;
/*!40000 ALTER TABLE `t_batch` DISABLE KEYS */;
INSERT INTO `t_batch` VALUES ('1','2015-2019','1'),('10','2017-2021','4'),('11','2015-2019','9'),('2','2015-2018','2'),('3','2015-2019','3'),('4','2016-2020','1'),('5','2015-2019','4'),('6','2015-2019','8'),('7','2019-2021','5'),('8','2017-2020','6'),('9','2015-2018','7');
/*!40000 ALTER TABLE `t_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_classroom`
--

DROP TABLE IF EXISTS `t_classroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_classroom` (
  `v_course_id` varchar(255) NOT NULL,
  `v_room_no` varchar(10) NOT NULL,
  `v_capacity` varchar(5) NOT NULL,
  PRIMARY KEY (`v_course_id`,`v_room_no`),
  CONSTRAINT `fk_course_id` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_classroom`
--

LOCK TABLES `t_classroom` WRITE;
/*!40000 ALTER TABLE `t_classroom` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_classroom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_course`
--

DROP TABLE IF EXISTS `t_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_course` (
  `v_discipline` varchar(255) NOT NULL,
  `v_degree` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  `v_duration` varchar(2) NOT NULL,
  `v_dept_name` varchar(255) NOT NULL,
  PRIMARY KEY (`v_course_id`),
  KEY `fk_dept` (`v_dept_name`),
  CONSTRAINT `fk_dept` FOREIGN KEY (`v_dept_name`) REFERENCES `t_department` (`v_dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_course`
--

LOCK TABLES `t_course` WRITE;
/*!40000 ALTER TABLE `t_course` DISABLE KEYS */;
INSERT INTO `t_course` VALUES ('CSE','B.Tech','1','8','Computer Science'),('CS','B.Sc','2','6','Computer Science'),('CE','B.Tech','3','8','Civil'),('ME','B.Tech','4','8','Mechanical'),('ECE','M.Tech','5','4','Electronics'),('Math','B.Sc','6','6','Maths'),('Hindi','B.A','7','6','Humanities'),('EE','B.Tech','8','8','Electrical'),('IT','B.Tech','9','8','Computer Science');
/*!40000 ALTER TABLE `t_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_department`
--

DROP TABLE IF EXISTS `t_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_department` (
  `v_dept_name` varchar(255) NOT NULL,
  `v_building_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`v_dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_department`
--

LOCK TABLES `t_department` WRITE;
/*!40000 ALTER TABLE `t_department` DISABLE KEYS */;
INSERT INTO `t_department` VALUES ('Civil','C'),('Computer Science','Dennis Ritchie Building'),('Electrical','A'),('Electronics','B'),('Humanities','F'),('Maths','E'),('Mechanical','D');
/*!40000 ALTER TABLE `t_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_exam_cell_member`
--

DROP TABLE IF EXISTS `t_exam_cell_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_exam_cell_member` (
  `v_mem_id` varchar(255) NOT NULL,
  `v_first_name` varchar(255) NOT NULL,
  `v_middle_name` varchar(255) DEFAULT NULL,
  `v_last_name` varchar(255) DEFAULT NULL,
  `v_address` varchar(255) NOT NULL,
  `v_contact_no` varchar(20) NOT NULL,
  `v_email_id` varchar(255) NOT NULL,
  `d_date_of_joining` date NOT NULL,
  `d_dob` date NOT NULL,
  PRIMARY KEY (`v_mem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_exam_cell_member`
--

LOCK TABLES `t_exam_cell_member` WRITE;
/*!40000 ALTER TABLE `t_exam_cell_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_exam_cell_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_exam_details`
--

DROP TABLE IF EXISTS `t_exam_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_exam_details` (
  `v_exam_id` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  `v_semester` varchar(2) NOT NULL,
  `v_exam_time` varchar(255) NOT NULL,
  PRIMARY KEY (`v_exam_id`),
  KEY `fk_courseId` (`v_course_id`),
  CONSTRAINT `fk_courseId` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_exam_details`
--

LOCK TABLES `t_exam_details` WRITE;
/*!40000 ALTER TABLE `t_exam_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_exam_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_login_details`
--

DROP TABLE IF EXISTS `t_login_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_login_details` (
  `v_user_id` varchar(255) NOT NULL,
  `v_pass` varchar(255) NOT NULL,
  `v_hash_algo` varchar(255) NOT NULL,
  `v_gid` varchar(3) NOT NULL,
  PRIMARY KEY (`v_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_login_details`
--

LOCK TABLES `t_login_details` WRITE;
/*!40000 ALTER TABLE `t_login_details` DISABLE KEYS */;
INSERT INTO `t_login_details` VALUES ('1234','$2a$10$1D6h/1GE/LbiDWUQDOF1.u4Th0zLr7I7Q.1o5XoluHNnuBMwXa8Om','bcrypt','1');
/*!40000 ALTER TABLE `t_login_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_office_member`
--

DROP TABLE IF EXISTS `t_office_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_office_member` (
  `v_mem_id` varchar(255) NOT NULL,
  `v_first_name` varchar(255) NOT NULL,
  `v_middle_name` varchar(255) DEFAULT NULL,
  `v_last_name` varchar(255) DEFAULT NULL,
  `v_address` varchar(255) NOT NULL,
  `v_contact_no` varchar(20) NOT NULL,
  `v_email_id` varchar(255) NOT NULL,
  `d_date_of_joining` date NOT NULL,
  `d_dob` date NOT NULL,
  PRIMARY KEY (`v_mem_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_office_member`
--

LOCK TABLES `t_office_member` WRITE;
/*!40000 ALTER TABLE `t_office_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_office_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_prof_dept`
--

DROP TABLE IF EXISTS `t_prof_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_prof_dept` (
  `v_prof_id` varchar(255) NOT NULL,
  `v_dept_name` varchar(255) NOT NULL,
  `v_hod` varchar(255) NOT NULL,
  PRIMARY KEY (`v_prof_id`,`v_dept_name`),
  KEY `fk_dept_name` (`v_dept_name`),
  CONSTRAINT `fk_dept_name` FOREIGN KEY (`v_dept_name`) REFERENCES `t_department` (`v_dept_name`),
  CONSTRAINT `fk_prof` FOREIGN KEY (`v_prof_id`) REFERENCES `t_professor` (`v_prof_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_prof_dept`
--

LOCK TABLES `t_prof_dept` WRITE;
/*!40000 ALTER TABLE `t_prof_dept` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_prof_dept` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_professor`
--

DROP TABLE IF EXISTS `t_professor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_professor` (
  `v_prof_id` varchar(255) NOT NULL,
  `v_first_name` varchar(255) NOT NULL,
  `v_middle_name` varchar(255) DEFAULT NULL,
  `v_last_name` varchar(255) DEFAULT NULL,
  `d_dob` date NOT NULL,
  `v_contact_no` varchar(20) NOT NULL,
  `v_address` varchar(255) NOT NULL,
  `v_email_id` varchar(255) NOT NULL,
  `d_date_of_joining` date NOT NULL,
  PRIMARY KEY (`v_prof_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_professor`
--

LOCK TABLES `t_professor` WRITE;
/*!40000 ALTER TABLE `t_professor` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_professor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student`
--

DROP TABLE IF EXISTS `t_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student` (
  `v_first_name` varchar(255) NOT NULL,
  `v_middle_name` varchar(255) DEFAULT NULL,
  `v_last_name` varchar(255) DEFAULT NULL,
  `v_reg_id` varchar(255) NOT NULL,
  `v_roll_no` varchar(255) NOT NULL,
  `d_dob` date NOT NULL,
  `v_mother_name` varchar(255) DEFAULT NULL,
  `v_reg_year` varchar(4) NOT NULL,
  `v_contact_no` varchar(20) NOT NULL,
  `v_guardian_contact_no` varchar(20) NOT NULL,
  `v_email_id` varchar(255) NOT NULL,
  `v_address` varchar(255) NOT NULL,
  `v_father_guardian_name` varchar(255) NOT NULL,
  `v_gender` varchar(255) NOT NULL,
  PRIMARY KEY (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student`
--

LOCK TABLES `t_student` WRITE;
/*!40000 ALTER TABLE `t_student` DISABLE KEYS */;
INSERT INTO `t_student` VALUES ('Babu','','Sarkar','101','12','1996-01-01','Dipa Sarkar','2015','7026123689','7523145986','babu33@gmail.com','Barasat',' Sunil Sarkar','Male'),('Debu','nath','Basak','102','32','1998-02-02','Tania Basak','2016','7032569845','7021425632','debu94@gmail.com','Fulia','Tapan Basak','Male'),('Tania','','Debnath','105','56','1998-03-03','Papia Debnath','2015','7526963254','7023698514','tania94@gmail.com','Dumdum','Goutam Debnath','Female'),('Sunita ','','Ghosh','109','51','1994-05-05','fuli Ghosh','2015','7032564585','7524698745','Sghosh45@gmail.com','santipur','BIBHASH GHOSH','Female'),('Sunil','','Biswas','125','33','1996-03-03','supu biswas','2015','7894561258','9874563214','fh34@fnail.cm','kalyani','SUNIL BISWAS','Male'),('Ganesh ','','Basak','126','55','1995-04-02','srija basak','2015','7412589658','9874156874','dgjkfer@gfmail.com','barasat','SOUVIK BASAK','Male'),('Mohan','chandra','Roy','145','22','1998-02-06','priya Roy','2015','8563214563','7456321458','th45@gmail.com','madanpur','DEBU ROY','Male'),('Dipa','','Ghosh','456','44','1995-06-01','anuja ghosh','2017','7563214785','9874563214','dp12@gmail.com','barackpur','RAJA GHOSH','Female'),('Ananta','','Sarkar','525','15','1995-09-09','kali Sarkar','2016','8965214563','4569874569','pbask@gmail.com','habra','SANJAY SARKAR','Male'),('Ram','','Basak','562','17','1997-06-02',' mou basak','2017','8456321456','7421569874','sg@gmail.com','ranaghat','RANJIT BASAK','Male'),('Avik',NULL,'Sarkar','CS101','CS10101','1996-10-03','Madhurima Sarkar','2015','1234567890','1234567891','xyzz@gmail.com','Kolkata','Amitava Sarkar',''),('Sourav',NULL,'Debnath','CS102','CS10102','1998-03-01','XYZ Debnath','2015','2345678901','7383829290','abc@gmail.com','Habra','MNO Debnath',''),('Pronab','Kanti','Mondal','CS103','CS10103','1996-10-21','ABC Mondal','2015','1374834394','4578345384','efg@gmail.com','Habra','ABCD Mondal','');
/*!40000 ALTER TABLE `t_student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_attend_exam`
--

DROP TABLE IF EXISTS `t_student_attend_exam`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student_attend_exam` (
  `v_reg_id` varchar(255) NOT NULL,
  `v_exam_id` varchar(255) NOT NULL,
  `v_exam_status` varchar(255) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_exam_id`),
  KEY `fk_exam_id` (`v_exam_id`),
  CONSTRAINT `fk_exam_id` FOREIGN KEY (`v_exam_id`) REFERENCES `t_exam_details` (`v_exam_id`),
  CONSTRAINT `fk_exam_reg_id` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_attend_exam`
--

LOCK TABLES `t_student_attend_exam` WRITE;
/*!40000 ALTER TABLE `t_student_attend_exam` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_student_attend_exam` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_enrollment_details`
--

DROP TABLE IF EXISTS `t_student_enrollment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student_enrollment_details` (
  `v_batch_id` varchar(255) NOT NULL,
  `v_reg_id` varchar(255) NOT NULL,
  `v_curr_semester` varchar(2) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_batch_id`),
  KEY `fk_batch_id_idx` (`v_batch_id`),
  CONSTRAINT `fk_batch_id` FOREIGN KEY (`v_batch_id`) REFERENCES `t_batch` (`v_batch_id`),
  CONSTRAINT `fk_reg_id` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_enrollment_details`
--

LOCK TABLES `t_student_enrollment_details` WRITE;
/*!40000 ALTER TABLE `t_student_enrollment_details` DISABLE KEYS */;
INSERT INTO `t_student_enrollment_details` VALUES ('3','101','7'),('4','102','3'),('1','105','6'),('5','109','4'),('6','125','7'),('11','126','7'),('9','145','3'),('10','456','3'),('7','525','3'),('8','562','3'),('1','CS101','7'),('1','CS102','7'),('2','CS103','6');
/*!40000 ALTER TABLE `t_student_enrollment_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_student_marks`
--

DROP TABLE IF EXISTS `t_student_marks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student_marks` (
  `v_reg_id` varchar(255) NOT NULL,
  `v_sub_id` varchar(255) NOT NULL,
  `v_semester` varchar(255) NOT NULL,
  `v_obtained_marks` varchar(3) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_sub_id`),
  CONSTRAINT `fk_marks` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student_marks`
--

LOCK TABLES `t_student_marks` WRITE;
/*!40000 ALTER TABLE `t_student_marks` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_student_marks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_subject`
--

DROP TABLE IF EXISTS `t_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_subject` (
  `v_course_id` varchar(255) NOT NULL,
  `v_sub_id` varchar(255) NOT NULL,
  `v_sub_name` varchar(255) NOT NULL,
  `v_credit` varchar(255) NOT NULL,
  `v_semester` varchar(255) NOT NULL,
  `v_opt_status` varchar(1) NOT NULL,
  `v_full_marks` varchar(5) NOT NULL,
  `v_sub_type` varchar(255) NOT NULL,
  `v_prof_id` varchar(255) NOT NULL,
  PRIMARY KEY (`v_course_id`,`v_sub_id`),
  KEY `fk_prof_id` (`v_prof_id`),
  CONSTRAINT `fk_course_id_sub` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`),
  CONSTRAINT `fk_prof_id` FOREIGN KEY (`v_prof_id`) REFERENCES `t_professor` (`v_prof_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_subject`
--

LOCK TABLES `t_subject` WRITE;
/*!40000 ALTER TABLE `t_subject` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_subject` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-26 23:08:46
