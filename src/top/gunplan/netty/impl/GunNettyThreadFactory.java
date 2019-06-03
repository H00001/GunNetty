package top.gunplan.netty.impl;

import java.util.concurrent.ThreadFactory;

public class GunNettyThreadFactory implements ThreadFactory {
    private String poolName;

    public GunNettyThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r, poolName);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        t.setPriority(9);
        return t;
    }
}