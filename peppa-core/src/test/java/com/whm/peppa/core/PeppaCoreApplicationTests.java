package com.whm.peppa.core;

import com.whm.peppa.core.zookeeper.CommonActionsStore;
import org.apache.zookeeper.KeeperException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PeppaCoreApplicationTests {

    @Test
    public void contextLoads() throws IOException, InterruptedException, KeeperException {
        CommonActionsStore zkWatcher = new CommonActionsStore(true);
        zkWatcher.connect("192.168.84.2:2181");
        zkWatcher.createNode("/config","111");
        String path="/config";
        System.out.println(zkWatcher.getData(path,zkWatcher,null));

        zkWatcher.updateNode(path, "6666");
        //zkWatcher.deleteNode(path);
        zkWatcher.close();

    }

}
