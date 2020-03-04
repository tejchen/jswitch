import com.tejchen.switchclient.model.JSwitchContext;
import com.tejchen.switchclient.JSwitchManager;
import com.tejchen.switchcommon.JSwitchServer;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class ZookeeperTest {

    @Test
    public void testConn() throws Exception {
        TestingServer test = new TestingServer(9999);
        test.start();
        JSwitchContext ctx = new JSwitchContext("127.0.0.1:9999", JSwitchServer.ZooKeeper);
        JSwitchManager manager = JSwitchManager.newSwitchManager(ctx);
        manager.init("switch", BaseServiceTestConfig.class);


        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:9999", retryPolicy);
        client.start();
        client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath("/com/tejchen/jswitch/switch/abc_BaseServiceTestConfig#testBigDecimal", "9.1".getBytes(StandardCharsets.UTF_8));
        Thread.sleep(3000);
    }
}
