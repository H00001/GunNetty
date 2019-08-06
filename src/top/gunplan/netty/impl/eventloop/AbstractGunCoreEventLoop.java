/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.gunplan.netty.impl.eventloop;

import top.gunplan.netty.GunCoreEventLoop;
import top.gunplan.netty.GunException;
import top.gunplan.netty.GunNettyPipeline;
import top.gunplan.netty.impl.GunNettyCoreThreadManager;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ExecutorService;

/**
 * AbstractGunCoreEventLoop
 *
 * @author dosdrtt
 * @see GunCoreDataEventLoopImpl
 * @see GunCoreConnectionEventLoopImpl
 */
abstract class AbstractGunCoreEventLoop implements GunCoreEventLoop {
    volatile ExecutorService deal;
    volatile Selector bootSelector;
    /**
     * pipe line, executor chain
     */
    volatile GunNettyPipeline pipeline;
    private volatile boolean running;
    volatile Thread workThread;

    GunNettyCoreThreadManager manager;

    AbstractGunCoreEventLoop() {

    }

    @Override
    public int init(final ExecutorService deal, final GunNettyPipeline pipeline) throws IOException {
        this.deal = deal;
        this.pipeline = pipeline;
        bootSelector = SelectorProvider.provider().openSelector();
        // Selector.open();
        return 0;
    }

    @Override
    public AbstractGunCoreEventLoop registerManager(GunNettyCoreThreadManager manager) {
        this.manager = manager;
        return this;
    }


    /**
     * dealEvent
     *
     * @param key SelectionKey
     * @throws Exception unKnown Exception
     */
    @Override
    public abstract void dealEvent(SelectionKey key) throws Exception;


    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void startEventLoop() {
        workThread = Thread.currentThread();
        this.running = true;
    }

    @Override
    public void stopEventLoop() {
        this.running = false;
    }

    /**
     * how to next deal
     */
    public abstract void nextDeal();


    @Override
    public void loop() {
        for (; isLoopNext(); ) {
            nextDeal();
        }
        try {
            whenHaltDeal();
        } catch (IOException e) {
            throw new GunException(e);
        }
    }

    /**
     * when halt the deal
     */
    void whenHaltDeal() throws IOException {
        bootSelector.close();
    }


    @Override
    public boolean isLoopNext() {
        return isRunning();
    }


}



