package cc.page.study.week5.practice2.mix;

import cc.page.study.week5.practice2.Cat;
import cc.page.study.week5.practice2.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Mix2 {

    @Autowired
    private Cat cat;

    @Autowired
    private Dog dog;
}
