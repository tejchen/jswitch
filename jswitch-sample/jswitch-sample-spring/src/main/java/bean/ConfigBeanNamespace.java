package bean;

import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.SpringJSwitch;
import org.springframework.stereotype.Component;

@Component
@SpringJSwitch(namespace = "namespace")
public class ConfigBeanNamespace {

    @JSwitch(code = "jzt_test", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "first_config", name = "Number配置")
    public static String PUSH_NUMBER_CONFIG = "";

}
