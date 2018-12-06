package com.whm.peppa.core.zookeeper;

import com.whm.peppa.core.utils.MachineUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author maozhu
 * @date 18/11/22
 */
public class ZookeeperMgr {
    protected static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperMgr.class);

    private CommonActionsStore store;

    private String curHost = "";
    private String curDefaultPrefixString = "";

    private ZookeeperMgr() {

    }

    /**
     * 定义一个私有的内部类，在第一次用这个嵌套类时，会创建一个实例。而类型为SingletonHolder的类，
     * 只有在Singleton.getInstance()中调用，
     * 由于私有的属性，他人无法使用SingleHolder，不调用Singleton.getInstance()就不会创建实例
     */
    private static class SingletonHolder {
        private static ZookeeperMgr instance = new ZookeeperMgr();
    }

    public static ZookeeperMgr getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * @return void
     * @throws Exception
     * @Description: 初始化
     */
    public void init(String host, String defaultPrefixString, boolean debug) throws Exception {

        try {

            initInternal(host, defaultPrefixString, debug);

            LOGGER.debug("ZookeeperMgr init.");

        } catch (Exception e) {

            throw new Exception("zookeeper init failed. ", e);
        }
    }



    /**
     * 重新连接
     */
    public void reconnect() {
        store.reconnect();
    }

    /**
     * @return void
     * @throws IOException
     * @throws InterruptedException
     * @Description: 初始化
     */
    private void initInternal(String hosts, String defaultPrefixString, boolean debug)
            throws IOException, InterruptedException {

        curHost = hosts;
        curDefaultPrefixString = defaultPrefixString;

        store = new CommonActionsStore(debug);
        store.connect(hosts);

        LOGGER.info("zoo prefix: " + defaultPrefixString);

        // 新建父目录
        try {
            makeDir(defaultPrefixString, MachineUtils.getHostIp());
        } catch (Exception e) {
            LOGGER.error(">>>>>>>>>>>cannot get host ip",e);
        }
    }

    /**
     * Zoo的新建目录
     *
     * @param dir
     */
    public void makeDir(String dir, String data) {

        try {

            boolean deafult_path_exist = store.exists(dir);
            if (!deafult_path_exist) {
                LOGGER.info("create: " + dir);
                this.createPersistentUrl(dir, data);
            } else {
            }

        } catch (KeeperException e) {

            LOGGER.error("cannot create path: " + dir, e);

        } catch (Exception e) {

            LOGGER.error("cannot create path: " + dir, e);
        }
    }

    /**
     * @Description: 应用程序必须调用它来释放zookeeper资源
     */
    public void release() throws InterruptedException {

        store.close();
    }

    /**
     * @Description: 获取子孩子 列表
     */
    public List<String> getRootChildren() {

        return store.getRootChildren();
    }

    /**
     * @return List<String>
     * @Description: 写持久化结点, 没有则新建, 存在则进行更新
     */
    public void createPersistentUrl(String url, String value) throws Exception {

        store.createNode(url, value);
    }

    /**
     * @return List<String>
     * @Description: 读结点数据
     */
    public String getData(String url, Watcher watcher) throws Exception {

        return store.getData(url, watcher, null);
    }

    /*
     * 返回zk
     */
    public ZooKeeper getZk() {

        return store.getZk();
    }

    /*
     * 路径是否存在
     */
    public boolean exists(String path) throws Exception {

        return store.exists(path);
    }

    /*
     * 生成一个临时结点
     */
    public String createEphemeralNode(String path, String value, CreateMode createMode) throws Exception {

        return store.createEphemeralNode(path, value, createMode);
    }

    /**
     * @return String
     * @throws InterruptedException
     * @throws KeeperException
     * @Description: 带状态信息的读取数据
     */
    public String getData(String path, Watcher watcher, Stat stat) throws InterruptedException, KeeperException {

        return store.getData(path, watcher, stat);
    }

    /**
     * @param path
     * @return void
     * @Description: 删除结点
     */
    public void deleteNode(String path) {

        store.deleteNode(path);
    }
}