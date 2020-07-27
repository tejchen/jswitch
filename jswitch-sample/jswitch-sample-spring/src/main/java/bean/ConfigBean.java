package bean;

import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.JSwitchClass;
import org.springframework.stereotype.Component;

@Component
@JSwitchClass
public class ConfigBean {

    @JSwitch(code = "PUSH_FIRST_CONFIG", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "PUSH_NUMBER_CONFIG", name = "Number配置")
    public static int PUSH_NUMBER_CONFIG = 0;

}
