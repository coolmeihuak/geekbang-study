create database test
    character set utf8mb4
    collate utf8mb4_0900_ai_ci;

create table person (
    id varchar(36) not null primary key,
    name varchar(100),
    age int
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci