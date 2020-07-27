import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringDemo {

    public static void main(String[] args) throws InterruptedException {
        //创建 spring 容器
        ApplicationContext ctx = new AnnotationConfigApplicationContext("bean", "config");
        for (;;){
            Thread.sleep(1000);
        }
    }


}
