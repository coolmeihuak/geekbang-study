package cc.page.study.week13;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Message {

    private String id;

    private String content;

    private Date time;
}
