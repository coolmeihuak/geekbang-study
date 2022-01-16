package cc.page.study.netty.io.bio.file;

import lombok.val;

import java.io.*;

public class FileStream {

    public static void main(String[] args) {
        val from = "/Users/xuqingbin/Downloads/test.bmp";
        String to = "/Users/xuqingbin/Downloads/copy.png";
        byteStream(from, to, 10);
    }

    private static void byteStream(String fileFrom, String fileTo, int size) {
        File from = new File(fileFrom);
        File to = new File(fileTo);
        try (FileInputStream fis = new FileInputStream(from); FileOutputStream fos = new FileOutputStream(to)) {
            int read;
            int hasRead = 0;
            int headSize = 8 * 12;
            while ((read = fis.read()) != -1) {
                hasRead++;
                if(hasRead>headSize){
                    fos.write(-read);
                }else{
                    fos.write(read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
