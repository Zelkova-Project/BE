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
    `name`        VARCHAR(30),
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
    `chat_id`     BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `chatroom_id` BIGINT   NOT NULL,
    `account_id`  BIGINT   NOT NULL,
    `content`     TEXT     NOT NULL,
    `created_at`  DATETIME NOT NULL,
    `updated_at`  DATETIME NOT NULL,
    INDEX `idx_chatroom_id` (`chatroom_id`),
    INDEX `idx_account_id` (`account_id`),
    FOREIGN KEY (`chatroom_id`) REFERENCES `chatrooms` (`chatroom_id`),
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

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
    `post_id`    BIGINT   NULL,
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
