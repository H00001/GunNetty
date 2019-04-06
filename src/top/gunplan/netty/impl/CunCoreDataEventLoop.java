package top.gunplan.netty.impl;
import top.gunplan.netty.GunException;
import top.gunplan.netty.GunPilelineInterface;
import top.gunplan.netty.common.GunNettyPropertyManager;
import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * this class used to loop to get the event like accept or read
 *
 * @author dosdrtt
 */
public class CunCoreDataEventLoop extends AbstractGunCoreEventLoop {
    private final GunPilelineInterface pileline;
    private AtomicInteger listionSize = new AtomicInteger(0);
    private boolean runState = true;
    private volatile Thread nowRun = null;

    public void setRunState(boolean runState) {
        this.runState = runState;
    }

    void continueLoop() {
        LockSupport.unpark(nowRun);
    }

    CunCoreDataEventLoop(ExecutorService deal, final GunPilelineInterface pileline) throws IOException {
        super(deal);
        this.pileline = pileline;

    }

    void registerReadKey(SelectableChannel channel) throws IOException {
        channel.configureBlocking(false);
        listionSize.incrementAndGet();
        channel.register(this.bootSelector, SelectionKey.OP_READ);
    }


    @Override
    public synchronized void run() {
        try {
            nowRun = Thread.currentThread();
            nowRun.setName(this.getClass().getSimpleName());
            while (runState) {
                if (listionSize.get() == 0) {
                    LockSupport.park();
                }
                int val = bootSelector.select(GunNettyPropertyManager.getCore().getClientWaitTime());
                if (val > 0) {
                    Iterator<SelectionKey> keyIterator = bootSelector.selectedKeys().iterator();
                    while (keyIterator.hasNext()) {
                        final SelectionKey sk = keyIterator.next();
                        this.dealEvent(sk);
                        keyIterator.remove();
                    }
                }
            }
        } catch (Exception exp) {
            throw new GunException(exp);
        }
    }


    @Override
    public void dealEvent(SelectionKey key) throws IOException {
        key.interestOps(0);
        this.deal.submit(new GunCoreCalculatorWorker(pileline, key, listionSize));
    }


}
