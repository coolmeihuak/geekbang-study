package cc.page.study.week5.practice2;

import cc.page.study.week5.practice2.mix.Mix1;
import cc.page.study.week5.practice2.mix.Mix2;
import cc.page.study.week5.practice2.mix.Mix22;
import cc.page.study.week5.practice2.mix.Mix3;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Application {

    public static void main(String[] args) {
        ApplicationContext acXml = new ClassPathXmlApplicationContext("applicationContext.xml");
        Person person1 = (Person)acXml.getBean("person1");
        Cat cat = (Cat)acXml.getBean("cat");
        Dog dog1 = (Dog)acXml.getBean("dog");
        Dog dog2 = (Dog)acXml.getBean("dog91");
        Mix1 mix1 = acXml.getBean(Mix1.class);
        System.out.println(person1);
        System.out.println(cat);
        System.out.println(dog1);
        System.out.println(dog2);
        System.out.println(mix1);

        ApplicationContext acAnnotation = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);
        Dog dog = acAnnotation.getBean(Dog.class);
        Cat cat1 = (Cat)acAnnotation.getBean("cat");
        Mix2 mix2 = acAnnotation.getBean(Mix2.class);
        Mix22 mix22 = acAnnotation.getBean(Mix22.class);
        Mix3 mix3 = acAnnotation.getBean(Mix3.class);
        System.out.println(dog);
        System.out.println(cat1);
        System.out.println(mix2);
        System.out.println(mix22);
        System.out.println(mix3);
    }
}
