package bean;

import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.JSwitchClass;
import org.springframework.stereotype.Component;

@Component
@JSwitchClass(group = "group")
public class ConfigBeanGroup {

    @JSwitch(code = "jzt_test", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "first_config", name = "Number配置")
    public static String PUSH_NUMBER_CONFIG = "";

}
