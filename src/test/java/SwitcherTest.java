import org.junit.Test;

import java.io.File;

public class SwitcherTest extends TestBase {
    @Override
    String XML() {
        return new File("src/test/resources/switcher.xml").getAbsolutePath();
    }

    @Test
    public void test1() {
        doTestOK("G (PRESTART -> (PRESTART U POWER_ON))");
    }

    @Test
    public void test2() {
        doTestOK("G (hal_init -> (F tim4_enable))");
    }

    @Test
    public void test3() {
        doTestOK("G (pin_reset_s2 -> X pin_reset_s3)");
    }

    @Test
    public void test4() {
        doTestOK("G (pin_reset_s1 -> X pin_reset_s2)");
    }

    @Test
    public void test5() {
        doTestOK("G (SLEEP -> F POWER_ON)");
    }

    @Test
    public void test6() {
        doTestCycle("G (F POWER_ON)");
    }

}
