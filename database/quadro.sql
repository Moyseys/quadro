-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema quadro
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema quadro
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `quadro` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `quadro` ;

-- -----------------------------------------------------
-- Table `quadro`.`team`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`team` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(500) NOT NULL,
  `value` DECIMAL(8,4) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`tag`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`tag` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `theme` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`balance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`balance` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` DECIMAL(15,4) NOT NULL,
  `team_id` INT NOT NULL,
  `created_at` DATE NOT NULL,
  `tag_id` INT NOT NULL,
  PRIMARY KEY (`id`, `tag_id`),
  UNIQUE INDEX `unique_team` (`team_id` ASC) VISIBLE,
  CONSTRAINT `balance_ibfk_1`
    FOREIGN KEY (`team_id`)
    REFERENCES `quadro`.`team` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `tag_fk_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `quadro`.`tag` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 5
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`user` (
  `id_user` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `last_name` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE INDEX `email` (`email` ASC) VISIBLE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`fixed_income`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`fixed_income` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `value` DECIMAL(10,4) NOT NULL,
  `period` INT NOT NULL,
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fixed_income_ibfk_1`
    FOREIGN KEY (`user_id`)
    REFERENCES `quadro`.`user` (`id_user`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`goal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`goal` (
  `name` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(500) NOT NULL,
  `value` DECIMAL(10,4) NOT NULL,
  `team_id` INT NOT NULL,
  INDEX `team_id` (`team_id` ASC) VISIBLE,
  CONSTRAINT `team_id`
    FOREIGN KEY (`team_id`)
    REFERENCES `quadro`.`team` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`invite`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`invite` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `team_id` INT NULL DEFAULT NULL,
  `sender_user_id` INT NULL DEFAULT NULL,
  `recipient_user_id` INT NULL DEFAULT NULL,
  `status` VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `team_id` (`team_id` ASC) VISIBLE,
  INDEX `sender_user_id` (`sender_user_id` ASC) VISIBLE,
  INDEX `recipient_user_id` (`recipient_user_id` ASC) VISIBLE,
  CONSTRAINT `invite_ibfk_1`
    FOREIGN KEY (`team_id`)
    REFERENCES `quadro`.`team` (`id`),
  CONSTRAINT `invite_ibfk_2`
    FOREIGN KEY (`sender_user_id`)
    REFERENCES `quadro`.`user` (`id_user`),
  CONSTRAINT `invite_ibfk_3`
    FOREIGN KEY (`recipient_user_id`)
    REFERENCES `quadro`.`user` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `quadro`.`team_member`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quadro`.`team_member` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `team_id` INT NULL DEFAULT NULL,
  `user_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `team_id` (`team_id` ASC) VISIBLE,
  INDEX `user_id` (`user_id` ASC) VISIBLE,
  CONSTRAINT `team_member_ibfk_1`
    FOREIGN KEY (`team_id`)
    REFERENCES `quadro`.`team` (`id`),
  CONSTRAINT `team_member_ibfk_2`
    FOREIGN KEY (`user_id`)
    REFERENCES `quadro`.`user` (`id_user`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
