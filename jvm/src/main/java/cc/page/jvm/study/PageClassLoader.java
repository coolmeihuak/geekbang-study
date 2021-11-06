package cc.page.jvm.study;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PageClassLoader extends ClassLoader {

    private static final String HELLO_CLASS_NAME = "Hello";

    static class ClassInfo {
        private final String classFileName;

        private final String classTargetMethodName;

        public ClassInfo(String classFileName, String classTargetMethodName) {
            this.classFileName = classFileName;
            this.classTargetMethodName = classTargetMethodName;
        }

        public String getClassFileName() {
            return this.classFileName;
        }

        public String getClassTargetMethodName() {
            return this.classTargetMethodName;
        }
    }

    private static final Map<String, ClassInfo> CLASS_TABLE = new HashMap<>();

    static {
        CLASS_TABLE.put(HELLO_CLASS_NAME, new ClassInfo("/Hello.xlass", "hello"));
    }

    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        PageClassLoader classLoader = new PageClassLoader();
        Class<?> hClass = classLoader.loadClass(HELLO_CLASS_NAME);
        Object hello = hClass.newInstance();
        Method[] hMethods = hClass.getDeclaredMethods();
        // 打印出来 [public void Hello.hello()]，知道方法名是 hello
        System.out.println(Arrays.toString(hMethods));
        Method helloMethod = hClass.getDeclaredMethod(CLASS_TABLE.get(HELLO_CLASS_NAME).getClassTargetMethodName());
        helloMethod.invoke(hello);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        String fileName = CLASS_TABLE.get(name).getClassFileName();
        byte[] bytes = readBytesByFile(fileName);
        byte[] trueBytes = decode(bytes);
        return defineClass(name, trueBytes, 0, trueBytes.length);
    }

    /**
     * 读取文件，返回字节数组
     * 1.8之后，使用 try()可以自动关闭流
     */
    private byte[] readBytesByFile(String fileName) throws ClassNotFoundException {
        try (InputStream is = this.getClass().getResourceAsStream(fileName)) {
            if (Objects.isNull(is)) {
                throw new ClassNotFoundException(fileName);
            }
            // 缓存流
            try (BufferedInputStream bis = new BufferedInputStream(is);) {
                int pos = bis.available();
                byte[] bytes = new byte[pos];
                int length = bis.read(bytes);
                if (length == 0) {
                    System.out.println("file content is empty");
                }
                return bytes;
            } catch (IOException e) {
                throw new ClassNotFoundException(fileName, e);
            }
        } catch (IOException e) {
            throw new ClassNotFoundException(fileName, e);
        }
    }

    /**
     * 解码
     */
    private byte[] decode(byte[] bytes) {
        int length = bytes.length;
        byte[] newBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            newBytes[i] = (byte) (255 - bytes[i]);
        }
        return newBytes;
    }
}
