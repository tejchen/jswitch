import com.tejchen.switchclient.model.SwitchContext;
import com.tejchen.switchclient.SwitchManager;
import com.tejchen.switchcommon.ServerEnum;
import org.junit.Test;

public class BaseServiceTest {

    @Test
    public void TestClientBaseService(){
        System.out.println("默认配置：");
        System.out.println(BaseServiceTestConfig.testString);
        System.out.println(BaseServiceTestConfig.testBool);
        System.out.println(BaseServiceTestConfig.testInt);
        System.out.println(BaseServiceTestConfig.testBigDecimal);
        System.out.println(BaseServiceTestConfig.testLong);
        System.out.println(BaseServiceTestConfig.testList);
        System.out.println(BaseServiceTestConfig.testListMap);
        System.out.println(BaseServiceTestConfig.testListObject);
        System.out.println(BaseServiceTestConfig.testMap);
        System.out.println(BaseServiceTestConfig.testMapObject);
        SwitchContext ctx = new SwitchContext("127.0.0.1:8080", ServerEnum.Default);
        SwitchManager manager = SwitchManager.newSwitchManager(ctx);
        manager.init(BaseServiceTestConfig.class);
        System.out.println("初始化配置：");
        System.out.println(BaseServiceTestConfig.testString);
        System.out.println(BaseServiceTestConfig.testBool);
        System.out.println(BaseServiceTestConfig.testInt);
        System.out.println(BaseServiceTestConfig.testLong);
        System.out.println(BaseServiceTestConfig.testList);
        System.out.println(BaseServiceTestConfig.testListMap);
        System.out.println(BaseServiceTestConfig.testListObject);
        System.out.println(BaseServiceTestConfig.testMap);
        System.out.println(BaseServiceTestConfig.testMapObject);

    }
}
