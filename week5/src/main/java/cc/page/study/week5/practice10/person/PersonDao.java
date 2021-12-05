package cc.page.study.week5.practice10.person;

import cc.page.study.week5.practice10.ParamSetter;
import cc.page.study.week5.practice10.utils.DaoUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonDao {

    public void add(Person person) throws SQLException {
        String add = "insert into person(id, name, age) values (?, ?, ?)";
        ParamSetter<PreparedStatement> consumer = ps -> {
            ps.setString(1, person.getId());
            ps.setString(2, person.getName());
            ps.setInt(3, person.getAge());
        };
        DaoUtils.addPrepared(add, consumer, true);
    }

    public void edit(String id, Person person) throws SQLException {
        String edit = "update person set name = ?, age = ? where id = ?";
        ParamSetter<PreparedStatement> consumer = ps -> {
            ps.setString(1, person.getName());
            ps.setInt(2, person.getAge());
            ps.setString(3, id);
        };
        DaoUtils.editPrepared(edit, consumer, true);
    }

    public void delete(String id) throws SQLException {
        String delete = "delete from person where id = ?";
        ParamSetter<PreparedStatement> consumer = ps -> {
            ps.setString(1, id);
        };
        DaoUtils.deletePrepared(delete, consumer, true);
    }

    public Person selectById(String id) throws SQLException {
        String select = "select id, name, age from person where id = ?";
        ParamSetter<PreparedStatement> consumer = ps -> {
            ps.setString(1, id);
        };
        Person person = new Person();
        ParamSetter<ResultSet> consumerResultSet = rs -> {
            while (rs.next()) {
                person.setId(rs.getString("id"));
                person.setName(rs.getString("name"));
                person.setAge(rs.getInt("age"));
            }
        };
        DaoUtils.selectPrepared(select, consumer, consumerResultSet);
        return person;
    }

    public List<Person> selectAll() throws SQLException {
        String select = "select id, name, age from person";
        List<Person> persons = new ArrayList<>();
        ParamSetter<ResultSet> consumerResultSet = rs -> {
            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getString("id"));
                person.setName(rs.getString("name"));
                person.setAge(rs.getInt("age"));
                persons.add(person);
            }
        };
        DaoUtils.selectPrepared(select, null, consumerResultSet);
        return persons;
    }
}
