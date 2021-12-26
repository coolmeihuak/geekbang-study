drop database if exists test;

create database test
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;
use test;
CREATE TABLE test.`user` (
                             `id` int auto_increment NOT NULL,
                             `mobile` varchar(20) DEFAULT NULL,
                             `username` varchar(20) DEFAULT NULL,
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
CREATE TABLE test.`commodity` (
                                  `id` int auto_increment NOT NULL,
                                  `name` varchar(50) not null ,
                                  `price` decimal(10,2),
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
create table test.`order` (
                              `id` int auto_increment NOT NULL,
                              `user_id` int NOT NULL,
                              `status` int not null ,
                              `money` decimal(10,2),
                              PRIMARY KEY (`id`),
                              KEY `FK_order_user_id` (`user_id`),
                              CONSTRAINT `FK_order_user_id` FOREIGN KEY (`user_id`) REFERENCES test.`user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
create table test.`order_item` (
                                   `id` int auto_increment NOT NULL,
                                   `order_id` int NOT NULL,
                                   `commodity_id` int NOT NULL,
                                   `count` int,
                                   `price` decimal(10,2),
                                   PRIMARY KEY (`id`),
                                   KEY `FK_order_item_order_id` (`order_id`),
                                   KEY `FK_order_item_commodity_id` (`commodity_id`),
                                   CONSTRAINT `FK_order_item_commodity_id` FOREIGN KEY (`commodity_id`) REFERENCES test.`commodity` (`id`),
                                   CONSTRAINT `FK_order_item_order_id` FOREIGN KEY (`order_id`) REFERENCES test.`order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;