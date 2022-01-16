package cc.page.study.netty.io.bio.arrays;

import java.io.*;

public class ArraysStream {

    public static void main(String[] args) {
        String content = "我爱一条柴，阿dasd哈瞌睡的哈圣诞卡开会开会";
        byte[] bytes = content.getBytes();
        byteStreamByte(bytes);
        byteStreamOut(bytes);
        char[] chars = content.toCharArray();
        characterStreamByte(chars);
        characterStreamOut(chars);
    }

    private static void byteStreamByte(byte[] bytes) {
        byte[] bytesRead = new byte[bytes.length];
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
            int read;
            int i = 0;
            while ((read = in.read()) != -1) {
                System.out.println("read = " + (byte)read);
                bytesRead[i] = (byte) read;
                i++;
            }
            String get = new String(bytesRead);
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void byteStreamOut(byte[] bytes) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(bytes); ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length)) {
            int read;
            while (in.available() > 0) {
                read = in.read();
                System.out.println("read = " + (byte)read);
                out.write(read);
            }
            String get = out.toString();
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void characterStreamByte(char[] chars) {
        char[] charsRead = new char[chars.length];
        try (CharArrayReader in = new CharArrayReader(chars)) {
            int read;
            int i = 0;
            while ((read = in.read()) != -1) {
                System.out.println("read = " + (char)read);
                charsRead[i] = (char) read;
                i++;
            }
            String get = new String(charsRead);
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void characterStreamOut(char[] chars) {
        try (CharArrayReader in = new CharArrayReader(chars); CharArrayWriter out = new CharArrayWriter(chars.length)) {
            int read;
            while (in.ready()) {
                read = in.read();
                System.out.println("read = " + (char)read);
                out.write(read);
            }
            String get = out.toString();
            System.out.println(get);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
