package testing.src;

import gitlet.Main;
import org.junit.Test;

public class Testlog {
    @Test
    public void Test(){
        Main.main(new String[]{"init"});
        Main.main(new String[]{"log"});
        Main.main(new String[]{"status"});
    }
}
