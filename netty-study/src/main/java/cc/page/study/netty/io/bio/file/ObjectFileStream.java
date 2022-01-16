package cc.page.study.netty.io.bio.file;

import lombok.Builder;
import lombok.Data;
import lombok.val;

import java.io.*;

public class ObjectFileStream {

    public static void main(String[] args) {
        val from = "/Users/xuqingbin/Downloads/person.txt";
        String to = "/Users/xuqingbin/Downloads/person.txt";
        byteStream(from, to, Person.builder().name("page").build());
    }

    private static void byteStream(String fileFrom, String fileTo, Person person) {
        File from = new File(fileFrom);
        File to = new File(fileTo);
        // 必须先创建 ObjectOutputStream 否则 EOFException
        try (ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(to));ObjectInputStream fis = new ObjectInputStream(new FileInputStream(from))) {
            fos.writeObject(person);
            fos.flush();
            Person p = (Person)fis.readObject();
            System.out.println(p);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Data
    @Builder
    private static class Person implements Serializable {
        private String name;
    }
}
