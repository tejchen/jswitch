package config;

import com.tejchen.switchclient.JSwitchSpringManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean(initMethod = "init")
    public JSwitchSpringManager entitlement() {
        JSwitchSpringManager manager = new JSwitchSpringManager();
        manager.setAppCode("TEST_APP");
        manager.setUrl("http://127.0.0.1:8080");
        manager.setServer("Default");
        return manager;
    }

    @Bean(initMethod = "init")
    public JSwitchSpringManager entitlement1() {
        JSwitchSpringManager manager = new JSwitchSpringManager();
        manager.setAppCode("TEST_APP_1");
        manager.setUrl("http://127.0.0.1:8080");
        manager.setServer("Default");
        manager.setNamespace("namespace");
        return manager;
    }
}
