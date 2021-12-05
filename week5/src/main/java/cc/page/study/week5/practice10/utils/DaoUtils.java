package cc.page.study.week5.practice10.utils;

import cc.page.study.week5.practice10.ParamSetter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DaoUtils {

    public static void add(String sql, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, transactions);
    }

    public static void edit(String sql, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, transactions);
    }

    public static void delete(String sql, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, transactions);
    }

    public static void select(String sql, ParamSetter<ResultSet> consumerResultSet) throws SQLException {
        StatementUtils.executeQuery(sql, consumerResultSet, false);
    }

    public static void addPrepared(String sql, ParamSetter<PreparedStatement> consumer, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, consumer, transactions);
    }

    public static void editPrepared(String sql, ParamSetter<PreparedStatement> consumer, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, consumer, transactions);
    }

    public static void deletePrepared(String sql, ParamSetter<PreparedStatement> consumer, boolean transactions) throws SQLException {
        StatementUtils.execute(sql, consumer, transactions);
    }

    public static void selectPrepared(String sql, ParamSetter<PreparedStatement> consumer, ParamSetter<ResultSet> consumerResultSet) throws SQLException {
        StatementUtils.executeQuery(sql, consumer, consumerResultSet, false);
    }
}
