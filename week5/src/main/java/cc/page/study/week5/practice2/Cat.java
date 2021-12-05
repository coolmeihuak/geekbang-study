package cc.page.study.week5.practice2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Cat {

    private String name;

    private int age;
}
