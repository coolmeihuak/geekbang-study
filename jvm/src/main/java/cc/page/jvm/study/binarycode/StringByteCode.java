package cc.page.jvm.study.binarycode;

public class StringByteCode {

    private final String finalStr = "finalStr";

    private static String staticStr1;

    private static final String staticFinalStr1;

    private static String staticStr2 = "staticStr2";

    private static final String staticFinalStr2 = "staticFinalStr2";

    private final String finalObjStr = new String("finalObjStr");

    private static String staticObjStr1;

    private static final String staticFinalObjStr1;

    private static String staticObjStr2 = new String("staticObjStr2");

    private static final String staticFinalObjStr2 = new String("staticFinalObjStr2");

    static {
        staticStr1 = "staticStr1";
        staticFinalStr1 = "staticFinalStr1";

        staticObjStr1 = new String("staticObjStr1");
        staticFinalObjStr1 = new String("staticFinalObjStr1");
    }

    public static void main(String[] args) {
        String s1 = "s1";
        String s2 = "s2";
        String s3 = s1 + s2;
        String os1 = new String("s1");
        String os2 = new String("s2");
        String os3 = os1 + os2;
        System.out.println(s1);
        System.out.println(s2);
        System.out.println(s3);
        System.out.println(os1);
        System.out.println(os2);
        System.out.println(os3);
    }
}
