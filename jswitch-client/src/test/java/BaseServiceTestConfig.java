import com.tejchen.switchclient.annotation.JSwitch;

public class BaseServiceTestConfig {

    @JSwitch(code = "PUSH_FIRST_CONFIG", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "first_config")
    public static String testString1 = "hi~";
}