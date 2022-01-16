package cc.page.study.netty.io.bio.string;

import java.io.*;

public class StringStream {

    public static void main(String[] args) {
        String content = "我爱一条柴，阿dasd哈瞌睡的哈圣诞卡开会开会";
        characterStreamString(content);
        characterStreamOut(content);
    }

    private static void characterStreamString(String str) {
        char[] strRead = new char[str.length()];
        try (StringReader in = new StringReader(str)) {
            int read = in.read(strRead);
            String get = new String(strRead);
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void characterStreamOut(String str) {
        try (StringReader in = new StringReader(str); StringWriter out = new StringWriter(str.length())) {
            int read;
            while ((read = in.read()) != -1) {
                out.write(read);
            }
            out.write(read);
            String get = out.toString();
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
