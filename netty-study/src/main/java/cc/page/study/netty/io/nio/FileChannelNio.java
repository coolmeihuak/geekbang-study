package cc.page.study.netty.io.nio;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelNio {

    public static void main(String[] args) {
        String file = "/Users/xuqingbin/Downloads/111.txt";
        write(file, "hello file channel nio");
        read(file);
    }

    private static void write(String file, String str) {
        File f = new File(file);
        try (FileOutputStream fos = new FileOutputStream(f)) {
            FileChannel fc = fos.getChannel();
            ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
            fc.write(buffer);
            fc.force(true);
            fc.close();
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void read(String file) {
        File f = new File(file);
        try (FileInputStream fis = new FileInputStream(f)) {
            FileChannel fc = fis.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(10);
            while (fc.read(buffer) != -1) {
                buffer.flip();
                for (int i = 0; i < buffer.limit(); i++) {
                    System.out.print((char)buffer.get(i));
                }
            }
            System.out.println();
            System.out.println(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
