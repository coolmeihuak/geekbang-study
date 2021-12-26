## 参考
* [数据源配置](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/spring-boot-starter/data-source/)
* [读写分离](https://shardingsphere.apache.org/document/current/cn/user-manual/shardingsphere-jdbc/spring-boot-starter/rules/readwrite-splitting/)
* 工程测试配置在【practice10-readwrite】下

## 过程
#### java.util.NoSuchElementException: No value bound
* yml必须要配置：spring.shardingsphere.datasource.common。
* 各个数据源共同属性可以配置

