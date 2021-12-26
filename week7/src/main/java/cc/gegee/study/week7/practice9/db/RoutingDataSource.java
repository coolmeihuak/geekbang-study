package cc.gegee.study.week7.practice9.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Component
public class RoutingDataSource extends AbstractRoutingDataSource {

    public RoutingDataSource(DataSource masterDataSource, @Qualifier("slave0DataSource") DataSource slave0DataSource, @Qualifier("slave1DataSource") DataSource slave1DataSource) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DBInstanceEnum.MASTER, masterDataSource);
        targetDataSources.put(DBInstanceEnum.SLAVE0, slave0DataSource);
        targetDataSources.put(DBInstanceEnum.SLAVE1, slave1DataSource);
        this.setDefaultTargetDataSource(masterDataSource);
        this.setTargetDataSources(targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DBContextHolder.get();
    }
}
