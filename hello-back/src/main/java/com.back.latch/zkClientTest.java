package com.back.latch;

import org.I0Itec.zkclient.ZkClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * 随便测试下Zookeeper
 */
public class zkClientTest {

    private volatile static ZkClient zkClient = null;
    public static void main(String[] args) throws UnknownHostException, InterruptedException {

        if(zkClient == null){
            synchronized (zkClientTest.class){
                if(zkClient == null){
                    zkClient = new ZkClient("127.0.0.1", 2181);
                    InetAddress localHost = InetAddress.getLocalHost();
                    String hostAddress = localHost.getHostAddress();
                    String root = "/back";
                    if(!zkClient.exists(root)){
                        zkClient.createPersistent(root);
                    }
                    if(!zkClient.exists(root + "/" + hostAddress)){
                        zkClient.createEphemeral(root + "/" + hostAddress);
                    }
                }
            }
        }

        TimeUnit.SECONDS.sleep(100);
    }
}
