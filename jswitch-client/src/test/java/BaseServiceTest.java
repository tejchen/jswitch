import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchclient.JSwitchManager;
import com.tejchen.switchcommon.JSwitchServer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BaseServiceTest {

    @Test
    public void TestClientBaseService() throws InterruptedException {
        System.out.println("默认配置：");
        JSwitchContext ctx = new JSwitchContext("http://127.0.0.1:8080", JSwitchServer.Default);
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        manager.init("switch", BaseServiceTestConfig.class);
        System.out.println("初始化配置：");
        List list = new ArrayList<>();
        list.add(BaseServiceTestConfig.testString);
        list.add(BaseServiceTestConfig.testBool);
        list.add(BaseServiceTestConfig.testInt);
        list.add(BaseServiceTestConfig.testLong);
        list.add(BaseServiceTestConfig.testList);
        list.add(BaseServiceTestConfig.testListMap);
        list.add(BaseServiceTestConfig.testListObject);
        list.add(BaseServiceTestConfig.testMap);
        list.add(BaseServiceTestConfig.testMapObject);
        String str = (String) list
                .stream()
                .map(x->x.toString())
                .reduce((x, y)->((String) x).concat((String) y)).get();
        System.out.println(str);
        System.out.println(list);
        for (;;){
            Thread.sleep(1000);
        }
    }
}
