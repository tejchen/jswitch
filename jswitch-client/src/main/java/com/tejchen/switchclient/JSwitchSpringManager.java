package com.tejchen.switchclient;

import com.tejchen.switchclient.annotation.JSwitchClass;
import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchcommon.JSwitchException;
import com.tejchen.switchcommon.JSwitchServer;
import lombok.Setter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;


public class JSwitchSpringManager implements ApplicationContextAware {

    @Setter
    private String                      url;

    @Setter
    private String                      server = "";

    @Setter
    private String                      appCode = "";

    @Setter
    private String                      namespace = "";

    private ApplicationContext          context;

    public JSwitchSpringManager() {
    }

    public JSwitchSpringManager(String url, String server) {
        this.url = url;
        this.server = server;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        this.context = context;
    }

    public void init(){
        if ("".equals(appCode)) {
            throw new JSwitchException("JSwitchSpringManager init fail! because appCode is empty.");
        }
        if ("".equals(url)) {
            throw new JSwitchException("JSwitchSpringManager init fail! because url is empty.");
        }
        JSwitchContext ctx = new JSwitchContext(url, "".equals(server) ? JSwitchServer.Default: JSwitchServer.valueOf(server));
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        Map<String, Object> target = context.getBeansWithAnnotation(JSwitchClass.class);
        if (target == null || target.isEmpty()){
            throw new JSwitchException("JSwitchSpringManager init fail! because config is empty.");
        }
        Class[] classList = target.values().stream()
                .filter(x->{
                    Class xClass = x.getClass();
                    JSwitchClass switchClass = (JSwitchClass) xClass.getAnnotation(JSwitchClass.class);
                    return switchClass.group().equals(namespace);
                }).map(Object::getClass).toArray(Class[]::new);
        if (classList.length == 0){
            throw new JSwitchException(String.format("JSwitchSpringManager init fail! because this namespace[%s] config is empty.", namespace));
        }
        manager.init(appCode, classList);
    }
}
