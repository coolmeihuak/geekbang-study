package cc.page.jvm.study;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.TimeZone;

public class Test {

    public static void main(String[] args) {
        DateTimeFormatter sDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime time = LocalDateTime.parse("2021-11-05 14:32:45", sDateFormat);
        System.out.println(sDateFormat.format(time));
        TimeZone timeZone = TimeZone.getDefault();
        System.out.println(timeZone);
        System.out.println(Arrays.toString(TimeZone.getAvailableIDs()));
        System.out.println(LocalDateTime.now());
        System.out.println(LocalDateTime.now().atZone(timeZone.toZoneId()).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).withZoneSameInstant(ZoneId.of("GMT")).toLocalDateTime());
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).withZoneSameLocal(ZoneId.of("UTC")).toLocalDateTime());
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("Asia/Shanghai")).withZoneSameLocal(ZoneId.of("GMT")).toLocalDateTime());

        System.out.println(LocalDateTime.now().atZone(ZoneId.of("UTC")).withZoneSameInstant(timeZone.toZoneId()).toLocalDateTime());
        System.out.println(LocalDateTime.now().atZone(ZoneId.of("GMT")).withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toLocalDateTime());
        System.out.println(sDateFormat.format(LocalDateTime.now()));
    }
}
