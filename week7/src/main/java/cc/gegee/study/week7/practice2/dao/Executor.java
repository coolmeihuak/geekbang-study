package cc.gegee.study.week7.practice2.dao;

import java.io.IOException;
import java.sql.Connection;

@FunctionalInterface
public interface Executor {

    Connection execute() throws IOException;
}
