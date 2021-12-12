package cc.page.jvm.study.binarycode;

public class ObjectByteCode extends ObjectClass1 {

    public static int value1 = 123;
//    public static final int value2 = 123;

    private ObjectClass o1;

    private ObjectClass o11 = new ObjectClass();

    private final ObjectClass o2 = new ObjectClass();

    private final static ObjectClass o3 = new ObjectClass();

    private final static ObjectClass o4;

    static {
        o4 = new ObjectClass();
    }

    public static void main(String[] args) {
        ObjectClass ooooo1 = new ObjectClass();
        final ObjectClass ooooooo2 = new ObjectClass();
        System.out.println(ooooo1);
        System.out.println(ooooooo2);
        System.out.println(ooooooo2.k.ii);
    }
}

final class ObjectClass {

    public ObjectClass1 ko = new ObjectClass1();

    public final ObjectClass1 k = new ObjectClass1();

    public final static ObjectClass1 kk = new ObjectClass1();

    public final int i = 2;
}

class ObjectClass1 {

    public final int i = 2;

    public final static int ii = 2;
}
