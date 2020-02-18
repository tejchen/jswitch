import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.JSwitchNamespace;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JSwitchNamespace("abc")
public class BaseServiceTestConfig {

    @JSwitch
    public static String testString = "hi~";
    @JSwitch
    public static int testInt = 1;
    @JSwitch
    public static long testLong = 2;
    @JSwitch
    public static BigDecimal testBigDecimal = new BigDecimal("0.1");
    @JSwitch
    public static boolean testBool = false;
    @JSwitch
    public static List<String> testList = new ArrayList<String>(){{
        add("list item 1");
    }};
    @JSwitch
    public static List<BaseServiceTestConfigItem> testListObject = new ArrayList<BaseServiceTestConfigItem>(){{
        add(new BaseServiceTestConfigItem("list obj 1"));
    }};
    @JSwitch
    public static List<Map<String, List<String>>> testListMap  = new ArrayList<Map<String, List<String>>>(){{
        add(new HashMap<String, List<String>>(){{
            put("mapKey", new ArrayList<String>() {{
                add("value");
            }});
        }});
    }};
    @JSwitch
    public static Map<String, String> testMap = new HashMap<String, String>(){{
        put("mapKey", "value");
    }};
    @JSwitch
    public static Map<String, BaseServiceTestConfigItem> testMapObject = new HashMap<String, BaseServiceTestConfigItem>(){{
        put("mapKey", new BaseServiceTestConfigItem("map obj 1"));
    }};
    @JSwitch
    public static BaseServiceTestConfigItem testObj = new BaseServiceTestConfigItem("obj 1");
}