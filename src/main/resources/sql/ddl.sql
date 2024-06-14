CREATE DATABASE IF NOT EXISTS zelkova;
USE zelkova;

CREATE TABLE `accounts`
(
    `account_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`       VARCHAR(10)  NOT NULL,
    `nickname`   VARCHAR(20)  NULL,
    `email`      VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `normal_accounts`
(
    `account_id` BIGINT,
    `login_id`   VARCHAR(20) NOT NULL UNIQUE,
    `password`   CHAR(60)    NOT NULL,
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    INDEX `idx_login_id` (`login_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `socials`
(
    `social_code` VARCHAR(20) NOT NULL PRIMARY KEY
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `social_accounts`
(
    `account_id`  BIGINT,
    `social_code` VARCHAR(30) NOT NULL,
    `social_id`   VARCHAR(30) NOT NULL,
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`social_code`) REFERENCES `socials` (`social_code`),
    INDEX `idx_social_id` (`social_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `roles`
(
    `role_code` VARCHAR(20) NOT NULL PRIMARY KEY
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `account_roles`
(
    `account_id` BIGINT      NOT NULL,
    `role_code`  VARCHAR(20) NOT NULL,
    PRIMARY KEY (`account_id`, `role_code`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`role_code`) REFERENCES `roles` (`role_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `chatrooms`
(
    `chatroom_id` BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `created_at`  DATETIME NOT NULL,
    `updated_at`  DATETIME NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `chatroom_accounts`
(
    `account_id`  BIGINT   NOT NULL,
    `chatroom_id` BIGINT   NOT NULL,
    `created_at`  DATETIME NOT NULL,
    `updated_at`  DATETIME NOT NULL,
    PRIMARY KEY (`account_id`, `chatroom_id`),
    INDEX `idx_chatroom_id` (`chatroom_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`chatroom_id`) REFERENCES `chatrooms` (`chatroom_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `chats`
(
    `chat_id`     BIGINT   NOT NULL AUTO_INCREMENT,
    `chatroom_id` BIGINT   NOT NULL,
    `account_id`  BIGINT   NOT NULL,
    `content`     TEXT     NOT NULL,
    `created_at`  DATETIME NOT NULL,
    `updated_at`  DATETIME NOT NULL,
    PRIMARY KEY (`chat_id`, `created_at`),
    INDEX `idx_chatroom_id` (`chatroom_id`),
    INDEX `idx_account_id` (`account_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

ALTER TABLE `chats` PARTITION BY RANGE (TO_DAYS(`created_at`)) (
    PARTITION `p_max` VALUES LESS THAN MAXVALUE
    );

CREATE TABLE `categories`
(
    `category_code` VARCHAR(30) NOT NULL PRIMARY KEY
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `visibilities`
(
    `visibility_code` VARCHAR(20) NOT NULL PRIMARY KEY
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `posts`
(
    `post_id`         BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `account_id`      BIGINT       NOT NULL,
    `category_code`   VARCHAR(30)  NOT NULL,
    `visibility_code` VARCHAR(20)  NOT NULL,
    `title`           VARCHAR(255) NOT NULL,
    `content`         TEXT         NOT NULL,
    `created_at`      DATETIME     NOT NULL,
    `updated_at`      DATETIME     NOT NULL,
    INDEX `idx_account_id` (`account_id`),
    FULLTEXT INDEX idx_ft_title (`title`) WITH PARSER `ngram`,
    FULLTEXT INDEX idx_ft_content (`content`) WITH PARSER `ngram`,
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`category_code`) REFERENCES `categories` (`category_code`),
    FOREIGN KEY (`visibility_code`) REFERENCES `visibilities` (`visibility_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `comments`
(
    `comment_id` BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `post_id`    BIGINT   NOT NULL,
    `account_id` BIGINT   NOT NULL,
    `content`    TEXT     NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    INDEX `idx_post_id` (`post_id`),
    INDEX `idx_account_id` (`account_id`),
    FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO roles
VALUES ('ADMIN'),
       ('MANAGER');

INSERT INTO categories
VALUES ('HOME_COMMUNICATION'),
       ('RECRUIT'),
       ('NOTICE'),
       ('BOARD');

INSERT INTO visibilities
VALUES ('PUBLIC'),
       ('PRIVATE');

INSERT INTO socials
VALUES ('KAKAO');

DELIMITER $$

DROP PROCEDURE IF EXISTS `partition_delete`$$
CREATE PROCEDURE `partition_delete`(
    input_date DATE
)
BEGIN
    DECLARE partition_names TEXT;
    SET partition_names = (SELECT GROUP_CONCAT(PARTITION_NAME)
                           FROM INFORMATION_SCHEMA.PARTITIONS
                           WHERE TABLE_SCHEMA = DATABASE()
                             AND TABLE_NAME = 'chats'
                             AND PARTITION_NAME < CONCAT('p_', DATE_FORMAT(input_date, '%Y%m%d')));

    IF LENGTH(partition_names) > 0 THEN
        SET @drop_stmt = CONCAT('ALTER TABLE chats DROP PARTITION ', partition_names, ';');
        PREPARE drop_partitions FROM @drop_stmt;
        EXECUTE drop_partitions;
        DEALLOCATE PREPARE drop_partitions;
    END IF;
END $$

DROP PROCEDURE IF EXISTS `partition_add`$$
CREATE PROCEDURE partition_add(
    input_date DATE
)
BEGIN
    SET @add_stmt = CONCAT(
            'ALTER TABLE `chats` REORGANIZE PARTITION p_max INTO (',
            'PARTITION p_', DATE_FORMAT(input_date, '%Y%m%d'), ' VALUES LESS THAN (TO_DAYS("', input_date, '")),',
            'PARTITION p_max VALUES LESS THAN (MAXVALUE)',
            ');'
        );

    PREPARE add_partitions FROM @add_stmt;
    EXECUTE add_partitions;
    DEALLOCATE PREPARE add_partitions;
END $$

SET GLOBAL event_scheduler = ON;

DROP EVENT IF EXISTS `ev_daily_partition`$$
CREATE EVENT `ev_daily_partition`
    ON SCHEDULE EVERY 1 DAY
        STARTS TIMESTAMPADD(HOUR, 4, CURDATE())
    ON COMPLETION NOT PRESERVE ENABLE
    DO
    BEGIN
        DECLARE today DATE;

        SET today = CURDATE();

        CALL partition_delete(DATE_SUB(today, INTERVAL 30 DAY));
        CALL partition_add(DATE_ADD(today, INTERVAL 1 DAY));
    END $$

DELIMITER ;
