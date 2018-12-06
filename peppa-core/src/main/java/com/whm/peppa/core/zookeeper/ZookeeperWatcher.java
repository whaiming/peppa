package com.whm.peppa.core.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 连接管理
 * @author maozhu
 * @date 18/11/20
 */
public class ZookeeperWatcher implements Watcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperWatcher.class);

    // 10 秒会话时间 ，避免频繁的session expired
    private static final int SESSION_TIMEOUT = 10000;

    // 3秒
    private static final int CONNECT_TIMEOUT = 3000;

    // 每次重试超时时间
    public static final int RETRY_PERIOD_SECONDS = 2;

    protected ZooKeeper zk;
    private CountDownLatch connectedSignal = new CountDownLatch(1);

    private static String CONNECT_ADDRESS = "";

    // 是否调试状态
    protected boolean debug = false;

    //
    protected boolean toWatch = true;

    /**
     * @param debug
     */
    public ZookeeperWatcher(boolean debug) {
        this.debug = debug;
    }


    public void connect(String hosts) throws IOException, InterruptedException {
        CONNECT_ADDRESS = hosts;
        zk = new ZooKeeper(CONNECT_ADDRESS, SESSION_TIMEOUT, this);

        // 连接有超时哦
        connectedSignal.await(CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);

        LOGGER.info(">>>>>>>>zookeeper: " + hosts + " , connected.");
    }

    /**
     * 当连接成功时调用的
     */
    public void process(WatchedEvent event) {
        //节点状态
        Event.KeeperState keeperState = event.getState();
        // 事件類型
        Event.EventType eventType = event.getType();
        // 节点名称
        String path = event.getPath();

        if (keeperState == Event.KeeperState.SyncConnected) {

            // 连接类型
            if (Event.EventType.None == eventType) {
                // 建立zk连接
                LOGGER.info(">>>>zk syncConnected");
                connectedSignal.countDown();
            }
            // 创建类型
            if (Event.EventType.NodeCreated == eventType) {
                LOGGER.info(">>>>node created ,path={}",path);
            }
            // 修改類型
            if (Event.EventType.NodeDataChanged == eventType) {
                LOGGER.info(">>>>node changed ,path={}",path);
            }
            // 删除类型
            if (Event.EventType.NodeDeleted == eventType) {
                LOGGER.info(">>>>node deleted ,path={}",path);
            }

        } else if (keeperState.equals(Event.KeeperState.Disconnected)) {

            // 这时收到断开连接的消息，这里其实无能为力，因为这时已经和ZK断开连接了，只能等ZK再次开启了
            LOGGER.warn(">>>>zk disconnected");

        } else if (keeperState.equals(Event.KeeperState.Expired)) {

            if (!debug) {

                // 这时收到这个信息，表示，ZK已经重新连接上了，但是会话丢失了，这时需要重新建立会话。
                LOGGER.error(">>>>zk expired");

                // just reconnect forever
                reconnect();
            } else {
                LOGGER.info(">>>>zk expired");
            }

        } else if (keeperState.equals(Event.KeeperState.AuthFailed)) {

            LOGGER.error(">>>>zk auth failed");
        }
    }

    /**
     * 含有重试机制的retry，加锁, 一直尝试连接，直至成功
     */
    public synchronized void reconnect() {

        LOGGER.info("start to reconnect....");

        int retries = 0;
        while (true) {

            try {
                if (!zk.getState().equals(ZooKeeper.States.CLOSED)) {
                    break;
                }

                LOGGER.warn(">>>>>>>zookeeper lost connection, reconnect");

                close();

                connect(CONNECT_ADDRESS);


            } catch (Exception e) {
                retries ++;
                LOGGER.error(">>>>> reconnect times = {},exception = {} " ,retries , e.getMessage());

                // sleep then retry
                try {
                    int sec = RETRY_PERIOD_SECONDS;
                    LOGGER.warn(">>>>> sleep " , sec);
                    TimeUnit.SECONDS.sleep(sec);
                } catch (InterruptedException e1) {
                    LOGGER.error(">>>>>>zookeeper reconnect interruptedException ", e1);
                }
            }
        }
    }

    public void close(){
        try {
            if (zk != null)
                zk.close();
        } catch (Exception e) {
            LOGGER.error(">>>>>>close zookeeper exception ", e);
        }
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }
}
