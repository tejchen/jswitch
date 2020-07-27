package bean;

import com.tejchen.switchclient.JSwitchManager;
import com.tejchen.switchclient.annotation.JSwitch;
import com.tejchen.switchclient.annotation.SpringJSwitch;
import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchcommon.JSwitchServer;
import org.springframework.stereotype.Component;

@Component
@SpringJSwitch
public class ConfigBean {

    @JSwitch(code = "PUSH_FIRST_CONFIG", name = "推送的第一个配置")
    public static String testString = "hi~";

    @JSwitch(code = "PUSH_NUMBER_CONFIG", name = "Number配置")
    public static int PUSH_NUMBER_CONFIG = 0;

}
