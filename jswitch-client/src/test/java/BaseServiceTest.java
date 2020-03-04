import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchclient.JSwitchManager;
import com.tejchen.switchcommon.JSwitchServer;
import org.junit.Test;

public class BaseServiceTest {

    @Test
    public void TestClientBaseService() throws InterruptedException {
        JSwitchContext ctx = new JSwitchContext("http://127.0.0.1:8080", JSwitchServer.Default);
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        manager.init("TEST_APP", BaseServiceTestConfig.class);
        for (;;){
            Thread.sleep(1000);
        }
    }
}
