/**
 * @author: jalon2015
 * @date: 2021/3/11 18:45
 */

public class TestA extends TestB {

    public TestA(int a) {
        super();
        this.a = a;
    }

    public static void main(String[] args) {
        TestA t = new TestA(1);
    }
}

class TestB{
    int a;

    public TestB(int a) {
        this.a = a;
        System.out.println("b");
    }

    public TestB() {

    }
}
