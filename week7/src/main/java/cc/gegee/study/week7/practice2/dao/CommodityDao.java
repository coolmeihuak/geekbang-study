package cc.gegee.study.week7.practice2.dao;

import cc.gegee.study.week7.practice9.domain.Commodity;
import lombok.val;

import java.sql.Connection;

public final class CommodityDao {

    public static void add(Connection connection, Commodity commodity) {
        val sql = "insert into commodity(`name`, price) values ('"
                + commodity.getName() + "', "
                + commodity.getPrice() + ")";
        Dao.execute(connection, sql);
    }
}
