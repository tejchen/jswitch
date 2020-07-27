import com.tejchen.switchclient.JSwitchManager;
import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchcommon.JSwitchServer;

public class MainDemo {

    @JSwitch(code = "PUSH_FIRST_CONFIG", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "PUSH_NUMBER_CONFIG", name = "Number配置")
    public static int PUSH_NUMBER_CONFIG = 0;

    public static void main(String[] args) throws InterruptedException {
        JSwitchContext ctx = new JSwitchContext("http://127.0.0.1:8080", JSwitchServer.Default);
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        manager.init("TEST_APP", MainDemo.class);
        for (;;){
            Thread.sleep(1000);
        }
    }
}
