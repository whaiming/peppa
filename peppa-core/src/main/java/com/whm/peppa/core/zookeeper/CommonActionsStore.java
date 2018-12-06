package com.whm.peppa.core.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * zk的一些通用的方法：增删改查
 * @author maozhu
 * @date 18/11/20
 */
public class CommonActionsStore extends ZookeeperWatcher {

    protected static final Logger LOGGER = LoggerFactory.getLogger(CommonActionsStore.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    // 最大重试次数
    public static final int MAX_RETRIES = 3;

    /**
     * @param debug
     */
    public CommonActionsStore(boolean debug) {
        super(debug);
    }

    /**
     * 创建节点
     * @param path
     * @param value
     * @throws InterruptedException
     * @throws KeeperException
     */
    public void createNode(String path, String value) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, toWatch);

                if (stat == null) {

                    zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

                } else {

                    zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                }

                break;

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {
                retries ++;

                LOGGER.warn("write connect lost... will retry " + retries + "\t" + e.toString());

                if (retries == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     *创建临时节点
     * @param path
     * @param value
     * @param createMode
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String createEphemeralNode(String path, String value, CreateMode createMode)
            throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, toWatch);

                if (stat == null) {

                    return zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);

                } else {

                    if (value != null) {
                        zk.setData(path, value.getBytes(CHARSET), stat.getVersion());
                    }
                }

                return path;

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn("createEphemeralNode connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    throw e;
                }
                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 判断是否存在
     *
     * @param path
     *
     * @return
     *
     * @throws InterruptedException
     * @throws KeeperException
     */
    public boolean exists(String path) throws InterruptedException, KeeperException {

        int retries = 0;
        while (true) {

            try {

                Stat stat = zk.exists(path, toWatch);

                if (stat == null) {

                    return false;

                } else {

                    return true;
                }

            } catch (KeeperException.SessionExpiredException e) {

                throw e;

            } catch (KeeperException e) {

                LOGGER.warn("exists connect lost... will retry " + retries + "\t" + e.toString());

                if (retries++ == MAX_RETRIES) {
                    LOGGER.error("connect final failed");
                    throw e;
                }

                // sleep then retry
                int sec = RETRY_PERIOD_SECONDS * retries;
                LOGGER.warn("sleep " + sec);
                TimeUnit.SECONDS.sleep(sec);
            }
        }
    }

    /**
     * 获取数据
     * @param path
     * @param watcher
     * @param stat
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    public String getData(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        byte[] data = zk.getData(path, watcher, stat);
        return new String(data, CHARSET);
    }

    /**
     * 获取根节点下所有的孩子
     * @return List<String>
     */
    public List<String> getRootChildren() {

        List<String> children = new ArrayList<String>();
        try {
            children = zk.getChildren("/", toWatch);
        } catch (KeeperException e) {
            LOGGER.error(e.toString());
        } catch (InterruptedException e) {
            LOGGER.error(e.toString());
        }

        return children;
    }

    /**
     * 删除结点
     * @param path
     * @return void
     */
    public void deleteNode(String path) {

        try {

            zk.delete(path, -1);

        } catch (KeeperException.NoNodeException e) {

            LOGGER.error("cannot delete path: " + path, e);

        } catch (InterruptedException e) {

            LOGGER.warn(e.toString());

        } catch (KeeperException e) {

            LOGGER.error("cannot delete path: " + path, e);
        }
    }

    /**
     * 修改节点
     * @param path
     * @param data
     */
    public void updateNode(String path, String data) {
        try {
            zk.setData(path, data.getBytes(), -1);
        } catch (KeeperException.NoNodeException e) {

            LOGGER.error("cannot update path: " , path, e);

        } catch (InterruptedException e) {

            LOGGER.warn(e.toString());

        } catch (KeeperException e) {

            LOGGER.error("cannot delete path:: " , path, e);
        }
    }
}
