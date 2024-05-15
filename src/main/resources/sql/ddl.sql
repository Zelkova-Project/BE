CREATE DATABASE IF NOT EXISTS zelkova;
USE zelkova;

CREATE TABLE `accounts`
(
    `account_id` BIGINT       NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `login_id`   VARCHAR(20)  NOT NULL UNIQUE,
    `password`   CHAR(60)     NOT NULL,
    `name`       VARCHAR(10)  NOT NULL,
    `nickname`   VARCHAR(20)  NULL,
    `email`      VARCHAR(255) NOT NULL,
    `created_at` DATETIME     NOT NULL,
    `updated_at` DATETIME     NOT NULL,
    `deleted`    BOOLEAN      NOT NULL DEFAULT FALSE,
    INDEX `idx_login_id` (`login_id`)
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
    `deleted`     BOOLEAN  NOT NULL,
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
    `deleted`         BOOLEAN      NOT NULL DEFAULT FALSE,
    INDEX `idx_account_id` (`account_id`),
    FULLTEXT INDEX idx_ft_title (`title`) WITH PARSER `ngram`,
    FULLTEXT INDEX idx_ft_content (`content`) WITH PARSER `ngram`,
    FOREIGN KEY (`account_id`) REFERENCES `accounts` (`account_id`),
    FOREIGN KEY (`category_code`) REFERENCES `categories` (`category_code`),
    FOREIGN KEY (`visibility_code`) REFERENCES `visibilities` (`visibility_code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `image`
(
    `saved_name`  BINARY(16)   NOT NULL PRIMARY KEY,
    `path`        VARCHAR(50)  NOT NULL,
    `origin_name` VARCHAR(100) NOT NULL,
    `extension`   CHAR(4)      NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `post_images`
(
    `post_id`    BIGINT     NOT NULL,
    `saved_name` BINARY(16) NOT NULL,
    PRIMARY KEY (`post_id`, `saved_name`),
    INDEX `idx_saved_name` (`saved_name`),
    FOREIGN KEY (`post_id`) REFERENCES `posts` (`post_id`),
    FOREIGN KEY (`saved_name`) REFERENCES `image` (`saved_name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `comments`
(
    `comment_id` BIGINT   NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `post_id`    BIGINT   NOT NULL,
    `account_id` BIGINT   NOT NULL,
    `comment`    TEXT     NOT NULL,
    `created_at` DATETIME NOT NULL,
    `updated_at` DATETIME NOT NULL,
    `deleted`    BOOLEAN  NOT NULL,
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
