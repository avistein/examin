-- MySQL dump 10.13  Distrib 8.0.15, for Win64 (x86_64)
--
-- Host: localhost    Database: examin_db
-- ------------------------------------------------------
-- Server version	8.0.15

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_academic_bg`
--

DROP TABLE IF EXISTS `t_academic_bg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_academic_bg` (
  `v_reg_id` varchar(255) NOT NULL,
  `v_exam_name` varchar(255) NOT NULL,
  `v_percentage_grade` varchar(5) NOT NULL,
  `v_board_univ` varchar(255) NOT NULL,
  `v_year_of_passing` varchar(4) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_exam_name`),
  CONSTRAINT `fk_reg_id` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_academic_bg`
--

LOCK TABLES `t_academic_bg` WRITE;
/*!40000 ALTER TABLE `t_academic_bg` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_academic_bg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_batch_details`
--

DROP TABLE IF EXISTS `t_batch_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_batch_details` (
  `v_batch_id` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  `v_reg_id` varchar(255) NOT NULL,
  `v_semester` varchar(2) NOT NULL,
  PRIMARY KEY (`v_reg_id`),
  KEY `fk_course` (`v_course_id`),
  CONSTRAINT `fk_batch_reg_id` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`),
  CONSTRAINT `fk_course` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_batch_details`
--

LOCK TABLES `t_batch_details` WRITE;
/*!40000 ALTER TABLE `t_batch_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `t_batch_details` ENABLE KEYS */;
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
INSERT INTO `t_course` VALUES ('CSE','B.Tech','1','8','Computer Science'),('CS','B.Sc','2','6','Computer Science');
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
INSERT INTO `t_department` VALUES ('Computer Science','Dennis Ritchie Building');
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
  PRIMARY KEY (`v_reg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_student`
--

LOCK TABLES `t_student` WRITE;
/*!40000 ALTER TABLE `t_student` DISABLE KEYS */;
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

-- Dump completed on 2019-02-19 14:28:43
