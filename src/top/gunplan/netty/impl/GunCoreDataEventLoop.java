package top.gunplan.netty.impl;

import top.gunplan.netty.GunException;
import top.gunplan.netty.GunPipeline;

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
public class GunCoreDataEventLoop extends AbstractGunCoreEventLoop {
    private final GunPipeline pipeline;
    private AtomicInteger listionSize = new AtomicInteger(0);
    private boolean runState = true;
    private volatile Thread nowRun = null;

    public void setRunState(boolean runState) {
        this.runState = runState;
    }

    public void continueLoop() {
        LockSupport.unpark(nowRun);
    }

    public void incrAndContinueLoop() {
        listionSize.incrementAndGet();
        LockSupport.unpark(nowRun);
    }

    GunCoreDataEventLoop(ExecutorService deal, final GunPipeline pileline) throws IOException {
        super(deal);
        this.pipeline = pileline;

    }

    void registerReadKey(SelectableChannel channel) throws IOException {
        channel.configureBlocking(false);
        channel.register(this.bootSelector, SelectionKey.OP_READ, this);
    }


    @Override
    public synchronized void run() {
        try {
            nowRun = Thread.currentThread();
            while (runState) {
                if (listionSize.get() == 0) {
                    LockSupport.park();
                }
                assert coreProperty != null;
                int val = coreProperty.getClientWaitTime() == -1 ? bootSelector.select() : bootSelector.select(coreProperty.getClientWaitTime());
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
    public void dealEvent(SelectionKey key) {
        key.interestOps(0);
        listionSize.decrementAndGet();
        this.deal.submit(new GunCoreCalculatorWorker(pipeline, key, listionSize));
    }

}