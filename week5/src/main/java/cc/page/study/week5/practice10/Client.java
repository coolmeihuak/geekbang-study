package cc.page.study.week5.practice10;

import cc.page.study.week5.practice10.person.Person;
import cc.page.study.week5.practice10.person.PersonDao;
import lombok.val;

import java.sql.SQLException;

public class Client {

    public static void main(String[] args) throws SQLException {

        PersonDao personDao = new PersonDao();

        // add
        val person = Person.builder()
                .id("1")
                .name("page")
                .age(13)
                .build();
        personDao.add(person);

        // edit
        val personEdit = Person.builder()
                .id("1")
                .name("gege")
                .age(45)
                .build();
        personDao.edit("1", personEdit);

        // select by id
        val personSelect = personDao.selectById("1");
        System.out.println(personSelect);

        // add again
        val person1 = Person.builder()
                .id("2")
                .name("rrrr")
                .age(34)
                .build();
        personDao.add(person1);

        // select all
        val personSelectAll = personDao.selectAll();
        System.out.println(personSelectAll);

        // delete
        personDao.delete("1");
        personDao.delete("2");
    }
}
