package cc.page.study.week5.practice2.mix;

import cc.page.study.week5.practice2.Cat;
import cc.page.study.week5.practice2.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Mix22 {

    @Resource
    private Cat cat;

    @Resource
    private Dog dog;
}
