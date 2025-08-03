CREATE SCHEMA IF NOT EXISTS `HyruleEvents` ;
USE `HyruleEvents` ;

-- -----------------------------------------------------
-- Table `HyruleEvents`.`participant`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HyruleEvents`.`participant` (
  `email` VARCHAR(100) PRIMARY KEY,
  `name` VARCHAR(45) NOT NULL,
  `type` VARCHAR(15) NOT NULL,
  `institution` VARCHAR(150) NOT NULL
);

-- -----------------------------------------------------
-- Table `HyruleEvents`.`event`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HyruleEvents`.`event` (
  `id` VARCHAR(20) PRIMARY KEY,
  `title` VARCHAR(200) NOT NULL,
  `date` DATE NOT NULL,
  `type` VARCHAR(15) NOT NULL,
  `location` VARCHAR(150) NOT NULL,
  `max_capacity` INT NOT NULL
);


-- -----------------------------------------------------
-- Table `HyruleEvents`.`activity`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HyruleEvents`.`activity` (
  `id` VARCHAR(20) PRIMARY KEY,
  `event_id` VARCHAR(20) NOT NULL,
  `title` VARCHAR(200) NOT NULL,
  `type` VARCHAR(15) NOT NULL,
  `speaker_email` VARCHAR(100) NOT NULL,
  `start_time` TIME NOT NULL,
  `end_time` TIME NOT NULL,
  CONSTRAINT `fk_activity_speaker` FOREIGN KEY (`speaker_email`)
    REFERENCES `HyruleEvents`.`participant` (`email`),
  CONSTRAINT `fk_activity_event` FOREIGN KEY (`event_id`)
    REFERENCES `HyruleEvents`.`event` (`id`)
);


-- -----------------------------------------------------
-- Table `HyruleEvents`.`participant_event_registration`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HyruleEvents`.`participant_event_registration` (
  `event_id` VARCHAR(20) NOT NULL,
  `participant_email` VARCHAR(100) NOT NULL,
  `type` VARCHAR(15) NOT NULL,
  `payment_method` VARCHAR(15) NOT NULL,
  `payment_amount` DOUBLE NOT NULL,
  `status` VARCHAR(15) NOT NULL,
  CONSTRAINT `pk_registration` PRIMARY KEY (`event_id`, `participant_email`),
  CONSTRAINT `fk_event_in_registration` FOREIGN KEY (`event_id`)
    REFERENCES `HyruleEvents`.`event` (`id`),
  CONSTRAINT `fk_participant_in_registration` FOREIGN KEY (`participant_email`)
    REFERENCES `HyruleEvents`.`participant` (`email`)
);


-- -----------------------------------------------------
-- Table `HyruleEvents`.`participant_activity_attendance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `HyruleEvents`.`participant_activity_attendance` (
  `activity_id` VARCHAR(20) NOT NULL,
  `participant_email` VARCHAR(100) NOT NULL,
  CONSTRAINT `pk_attendance` PRIMARY KEY (`activity_id`, `participant_email`),
  CONSTRAINT `fk_participant_in_attendance` FOREIGN KEY (`participant_email`)
    REFERENCES `HyruleEvents`.`participant` (`email`),
  CONSTRAINT `fk_activity_in_attendance` FOREIGN KEY (`activity_id`)
    REFERENCES `HyruleEvents`.`activity` (`id`)
);

