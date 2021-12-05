package cc.page.study.week5.autoimport;


import cc.page.study.week5.autoimport.bean.Klass;
import cc.page.study.week5.autoimport.bean.School;
import cc.page.study.week5.autoimport.bean.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
@Builder
@NoArgsConstructor
public class BeanTest {

    private Klass klass;

    private School school;

    private Student student;

    @Autowired
    public BeanTest(Klass klass, School school, Student student) {
        this.klass = klass;
        this.school = school;
        this.student = student;
    }
}
