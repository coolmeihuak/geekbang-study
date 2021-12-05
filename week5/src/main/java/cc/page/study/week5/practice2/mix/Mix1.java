package cc.page.study.week5.practice2.mix;

import cc.page.study.week5.practice2.Cat;
import cc.page.study.week5.practice2.Dog;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Mix1 {

    private Cat cat;

    private Dog dog;
}
