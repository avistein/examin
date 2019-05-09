CREATE DATABASE  IF NOT EXISTS `examin_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */;
USE `examin_db`;
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
  CONSTRAINT `fk_coursesId` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_classroom`
--

DROP TABLE IF EXISTS `t_classroom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_classroom` (
  `int_room_no` int(11) NOT NULL,
  `int_capacity` int(11) NOT NULL,
  `int_rows` int(11) NOT NULL,
  `int_cols` int(11) NOT NULL,
  PRIMARY KEY (`int_room_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `int_duration` int(11) NOT NULL,
  `v_dept_name` varchar(255) NOT NULL,
  PRIMARY KEY (`v_course_id`),
  KEY `fk_dept` (`v_dept_name`),
  CONSTRAINT `fk_dept` FOREIGN KEY (`v_dept_name`) REFERENCES `t_department` (`v_dept_name`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_degree_completed_student`
--

DROP TABLE IF EXISTS `t_degree_completed_student`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_degree_completed_student` (
  `v_reg_id` varchar(255) NOT NULL,
  `v_batch_id` varchar(255) NOT NULL,
  PRIMARY KEY (`v_reg_id`),
  KEY `BATCHID_idx` (`v_batch_id`),
  CONSTRAINT `BATCHID` FOREIGN KEY (`v_batch_id`) REFERENCES `t_batch` (`v_batch_id`) ON DELETE CASCADE,
  CONSTRAINT `REG_ID` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_department`
--

DROP TABLE IF EXISTS `t_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_department` (
  `v_dept_name` varchar(255) NOT NULL,
  `v_building_name` varchar(255) NOT NULL,
  PRIMARY KEY (`v_dept_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_exam_cell_member`
--

DROP TABLE IF EXISTS `t_exam_cell_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_exam_cell_member` (
  `v_emp_id` varchar(255) NOT NULL,
  `v_first_name` varchar(255) NOT NULL,
  `v_middle_name` varchar(255) DEFAULT NULL,
  `v_last_name` varchar(255) DEFAULT NULL,
  `d_date_of_joining` date NOT NULL,
  `d_dob` date NOT NULL,
  `v_profile_picture_location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`v_emp_id`),
  CONSTRAINT `empId` FOREIGN KEY (`v_emp_id`) REFERENCES `t_login_details` (`v_user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_exam_details`
--

DROP TABLE IF EXISTS `t_exam_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_exam_details` (
  `v_exam_details_id` varchar(255) NOT NULL,
  `v_semester_type` varchar(10) NOT NULL,
  `d_start_date` date NOT NULL,
  `d_end_date` date DEFAULT NULL,
  `t_start_time` time NOT NULL,
  `t_end_time` time NOT NULL,
  `int_is_exam_on_saturday` tinyint(1) NOT NULL,
  `v_exam_type` varchar(45) NOT NULL,
  `v_academic_year` varchar(255) NOT NULL,
  PRIMARY KEY (`v_exam_details_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_exam_time_table`
--

DROP TABLE IF EXISTS `t_exam_time_table`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_exam_time_table` (
  `int_exam_id` int(11) NOT NULL AUTO_INCREMENT,
  `v_course_id` varchar(255) NOT NULL,
  `v_sub_id` varchar(255) NOT NULL,
  `d_exam_date` date NOT NULL,
  `v_exam_details_id` varchar(255) NOT NULL,
  PRIMARY KEY (`int_exam_id`),
  KEY `subCourseId_idx` (`v_course_id`,`v_sub_id`),
  KEY `examDetailsId_idx` (`v_exam_details_id`),
  CONSTRAINT `examDetailsId` FOREIGN KEY (`v_exam_details_id`) REFERENCES `t_exam_details` (`v_exam_details_id`) ON DELETE CASCADE,
  CONSTRAINT `subCourseId` FOREIGN KEY (`v_course_id`, `v_sub_id`) REFERENCES `t_subject` (`v_course_id`, `v_sub_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2531 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_holiday_details`
--

DROP TABLE IF EXISTS `t_holiday_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_holiday_details` (
  `int_holiday_id` int(11) NOT NULL,
  `v_holiday_name` varchar(255) NOT NULL,
  `d_start_date` date NOT NULL,
  `d_end_date` date NOT NULL,
  PRIMARY KEY (`int_holiday_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_invigilation_duty`
--

DROP TABLE IF EXISTS `t_invigilation_duty`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_invigilation_duty` (
  `int_room_no` int(11) NOT NULL,
  `d_exam_date` date NOT NULL,
  `v_prof_id` varchar(255) NOT NULL,
  `v_exam_details_id` varchar(255) NOT NULL,
  PRIMARY KEY (`d_exam_date`,`v_prof_id`),
  KEY `professorId_idx` (`v_prof_id`),
  KEY `roomno_idx` (`int_room_no`),
  KEY `v_exam_details_id_idx` (`v_exam_details_id`),
  CONSTRAINT `professorId` FOREIGN KEY (`v_prof_id`) REFERENCES `t_professor` (`v_prof_id`) ON DELETE CASCADE,
  CONSTRAINT `v_exam_details_id` FOREIGN KEY (`v_exam_details_id`) REFERENCES `t_exam_details` (`v_exam_details_id`) ON DELETE CASCADE,
  CONSTRAINT `v_roomno` FOREIGN KEY (`int_room_no`) REFERENCES `t_classroom` (`int_room_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_login_details`
--

DROP TABLE IF EXISTS `t_login_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_login_details` (
  `v_user_id` varchar(255) NOT NULL,
  `int_gid` int(11) NOT NULL,
  `ts_login_timestamp` timestamp NULL DEFAULT NULL,
  `v_pass` varchar(255) NOT NULL,
  `v_hash_algo` varchar(255) NOT NULL,
  PRIMARY KEY (`v_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_prof_dept`
--

DROP TABLE IF EXISTS `t_prof_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_prof_dept` (
  `v_prof_id` varchar(255) NOT NULL,
  `v_dept_name` varchar(255) NOT NULL,
  `int_hod` tinyint(1) NOT NULL,
  PRIMARY KEY (`v_prof_id`,`v_dept_name`),
  KEY `fk_dept_name` (`v_dept_name`),
  CONSTRAINT `fk_dept_name` FOREIGN KEY (`v_dept_name`) REFERENCES `t_department` (`v_dept_name`),
  CONSTRAINT `fk_prof` FOREIGN KEY (`v_prof_id`) REFERENCES `t_professor` (`v_prof_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_prof_sub`
--

DROP TABLE IF EXISTS `t_prof_sub`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_prof_sub` (
  `v_prof_id` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  `v_sub_id` varchar(255) NOT NULL,
  PRIMARY KEY (`v_prof_id`,`v_course_id`,`v_sub_id`),
  KEY `v_coursesubid_idx` (`v_course_id`,`v_sub_id`),
  CONSTRAINT `v_coursesubid` FOREIGN KEY (`v_course_id`, `v_sub_id`) REFERENCES `t_subject` (`v_course_id`, `v_sub_id`) ON DELETE CASCADE,
  CONSTRAINT `v_prof_id` FOREIGN KEY (`v_prof_id`) REFERENCES `t_professor` (`v_prof_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `d_date_of_joining` date NOT NULL,
  `v_highest_qualification` varchar(255) NOT NULL,
  `v_profile_picture_location` varchar(255) DEFAULT NULL,
  `v_designation` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`v_prof_id`),
  CONSTRAINT `profId` FOREIGN KEY (`v_prof_id`) REFERENCES `t_login_details` (`v_user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_room_allocation`
--

DROP TABLE IF EXISTS `t_room_allocation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_room_allocation` (
  `int_room_no` int(11) NOT NULL,
  `v_exam_details_id` varchar(255) NOT NULL,
  `v_reg_id` varchar(255) NOT NULL,
  `int_ralloc_id` int(11) NOT NULL,
  PRIMARY KEY (`v_exam_details_id`,`v_reg_id`),
  KEY `roomNo_idx` (`int_room_no`),
  KEY `v_examDetailsId_idx` (`v_exam_details_id`),
  KEY `endRegId_idx` (`v_reg_id`),
  CONSTRAINT `regId` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`) ON DELETE CASCADE,
  CONSTRAINT `roomNo` FOREIGN KEY (`int_room_no`) REFERENCES `t_classroom` (`int_room_no`),
  CONSTRAINT `v_examDetailsId` FOREIGN KEY (`v_exam_details_id`) REFERENCES `t_exam_details` (`v_exam_details_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `v_profile_picture_location` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`v_reg_id`),
  UNIQUE KEY `v_roll_no_UNIQUE` (`v_roll_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_student_enrollment_details`
--

DROP TABLE IF EXISTS `t_student_enrollment_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student_enrollment_details` (
  `v_batch_id` varchar(255) NOT NULL,
  `v_reg_id` varchar(255) NOT NULL,
  `int_curr_semester` int(11) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_batch_id`),
  KEY `fk_batch_id_idx` (`v_batch_id`),
  CONSTRAINT `fk_batch_id` FOREIGN KEY (`v_batch_id`) REFERENCES `t_batch` (`v_batch_id`) ON UPDATE CASCADE,
  CONSTRAINT `fk_reg_id` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_student_marks`
--

DROP TABLE IF EXISTS `t_student_marks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_student_marks` (
  `v_reg_id` varchar(255) NOT NULL,
  `v_course_id` varchar(255) NOT NULL,
  `v_sub_id` varchar(255) NOT NULL,
  `v_obtained_marks` varchar(255) DEFAULT NULL,
  `int_semester` int(11) NOT NULL,
  PRIMARY KEY (`v_reg_id`,`v_sub_id`,`v_course_id`),
  KEY `subCourseID_idx` (`v_course_id`,`v_sub_id`),
  CONSTRAINT `studentregID` FOREIGN KEY (`v_reg_id`) REFERENCES `t_student` (`v_reg_id`) ON DELETE CASCADE,
  CONSTRAINT `subIdCourseID` FOREIGN KEY (`v_course_id`, `v_sub_id`) REFERENCES `t_subject` (`v_course_id`, `v_sub_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
  `int_semester` int(11) NOT NULL,
  `v_full_marks` varchar(5) NOT NULL,
  `v_sub_type` varchar(255) NOT NULL,
  PRIMARY KEY (`v_course_id`,`v_sub_id`),
  CONSTRAINT `fk_course_id_sub` FOREIGN KEY (`v_course_id`) REFERENCES `t_course` (`v_course_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_user_contact_details`
--

DROP TABLE IF EXISTS `t_user_contact_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_user_contact_details` (
  `v_user_id` varchar(255) NOT NULL,
  `v_address` varchar(255) NOT NULL,
  `v_contact_no` varchar(255) NOT NULL,
  `v_email_id` varchar(255) NOT NULL,
  `v_first_name` varchar(255) NOT NULL,
  PRIMARY KEY (`v_user_id`),
  CONSTRAINT `userid` FOREIGN KEY (`v_user_id`) REFERENCES `t_login_details` (`v_user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-05-09 23:20:52
