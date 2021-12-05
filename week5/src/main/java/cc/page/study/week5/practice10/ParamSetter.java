package cc.page.study.week5.practice10;

import java.sql.SQLException;

/**
 * 参数设置、结果集消费
 */
@FunctionalInterface
public interface ParamSetter<T> {

    void set(T value) throws SQLException;
}
