import org.junit.Test;

import java.io.File;

public class SmallTest extends TestBase{

    @Override
    String XML() {
        return new File("src/test/resources/small.xml").getAbsolutePath();
    }

    @Test
    public void test1() {
        doTestOK("G (JUMP -> (F D))");
    }

    @Test
    public void test2() {
        doTestCycle("G (A -> (F B))");
    }

    @Test
    public void test3() {
        doTestCycle("G (C -> (F (A || B)))");
    }
}
