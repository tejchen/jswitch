import com.tejchen.switchclient.annotation.SwitchApp;
import com.tejchen.switchclient.annotation.SwitchItem;
import com.tejchen.switchclient.annotation.SwitchNamespace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SwitchApp("switch")
@SwitchNamespace("abc")
public class BaseServiceTestConfig {

    @SwitchItem
    public static String testString = "hi~";
    @SwitchItem
    public static int testInt = 1;
    @SwitchItem
    public static long testLong = 2;
    @SwitchItem
    public static BigDecimal testBigDecimal = new BigDecimal("0.1");
    @SwitchItem
    public static boolean testBool = false;
    @SwitchItem
    public static List<String> testList = new ArrayList<String>(){{
        add("list item 1");
    }};
    @SwitchItem
    public static List<BaseServiceTestConfigItem> testListObject = new ArrayList<BaseServiceTestConfigItem>(){{
        add(new BaseServiceTestConfigItem("list obj 1"));
    }};
    @SwitchItem
    public static List<Map<String, List<String>>> testListMap  = new ArrayList<Map<String, List<String>>>(){{
        add(new HashMap<String, List<String>>(){{
            put("mapKey", new ArrayList<String>() {{
                add("value");
            }});
        }});
    }};
    @SwitchItem
    public static Map<String, String> testMap = new HashMap<String, String>(){{
        put("mapKey", "value");
    }};
    @SwitchItem
    public static Map<String, BaseServiceTestConfigItem> testMapObject = new HashMap<String, BaseServiceTestConfigItem>(){{
        put("mapKey", new BaseServiceTestConfigItem("map obj 1"));
    }};
    @SwitchItem
    public static BaseServiceTestConfigItem testObj = new BaseServiceTestConfigItem("obj 1");
}