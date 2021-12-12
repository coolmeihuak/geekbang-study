create database test
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;
use test;
CREATE TABLE test.`user` (
                        `id` varchar(36) NOT NULL,
                        `created_on` datetime(6) NOT NULL,
                        `updated_on` datetime(6) DEFAULT NULL,
                        `version` smallint(6) DEFAULT NULL,
                        `mobile` varchar(20) DEFAULT NULL,
                        `nick_name` varchar(50) NOT NULL,
                        `password` varbinary(60) DEFAULT NULL,
                        `username` varchar(20) DEFAULT NULL,
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `UK_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
CREATE TABLE test.`commodity` (
                             `id` varchar(36) NOT NULL,
                             `created_on` datetime(6) NOT NULL,
                             `updated_on` datetime(6) DEFAULT NULL,
                             `version` smallint(6) DEFAULT NULL,
                             `name` varchar(50) not null ,
                             `price` decimal(10,2),
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
create table test.`order` (
                                    `id` varchar(36) NOT NULL,
                                    `created_on` datetime(6) NOT NULL,
                                    `updated_on` datetime(6) DEFAULT NULL,
                                    `version` smallint(6) DEFAULT NULL,
                                    `user_id` varchar(36) NOT NULL,
                                    `money` decimal(10,2),
                                    `remark` varchar(200),
                                    PRIMARY KEY (`id`),
                                    KEY `FK_order_user_id` (`user_id`),
                                    CONSTRAINT `FK_order_user_id` FOREIGN KEY (`user_id`) REFERENCES test.`user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;
create table test.`order_item` (
                              `id` varchar(36) NOT NULL,
                              `created_on` datetime(6) NOT NULL,
                              `updated_on` datetime(6) DEFAULT NULL,
                              `version` smallint(6) DEFAULT NULL,
                              `order_id` varchar(36) NOT NULL,
                              `commodity_id` varchar(36) NOT NULL,
                              `count` int,
                              `price` decimal(10,2),
                              PRIMARY KEY (`id`),
                              KEY `FK_order_item_order_id` (`order_id`),
                              KEY `FK_order_item_commodity_id` (`commodity_id`),
                              CONSTRAINT `FK_order_item_commodity_id` FOREIGN KEY (`commodity_id`) REFERENCES test.`commodity` (`id`),
                              CONSTRAINT `FK_order_item_order_id` FOREIGN KEY (`order_id`) REFERENCES test.`order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
;