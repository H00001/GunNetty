/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl;

import top.gunplan.netty.ChannelInitHandle;
import top.gunplan.netty.GunNettyTimer;
import top.gunplan.netty.impl.eventloop.*;
import top.gunplan.netty.impl.timeevent.AbstractGunTimeExecutor;
import top.gunplan.netty.impl.timeevent.GunTimeExecutor;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * GunNettyEventLoopManagerImpl
 *
 * @author frank albert
 * @version 0.0.0.1
 * @date 2019-08-13 04:42
 */
final class GunNettyEventLoopManagerImpl implements GunNettyEventLoopManager {
    private volatile GunConnEventLoop dealAccept;
    private volatile GunTimeExecutor timeExecute;
    private volatile GunDataEventLoop<SocketChannel>[] dealData;
    private volatile GunNettyTransfer<SocketChannel> transfer;

    @Override
    public synchronized boolean init(int v1, List<GunNettyTimer> timerList, ExecutorService bossExecutor,
                                     ExecutorService dataExecutor, ChannelInitHandle parentHandle,
                                     ChannelInitHandle childrenHandle, int port) {
        transfer = EventLoopFactory.newGunNettyBaseTransfer();
        transfer.registerManager(this);
        timeExecute = AbstractGunTimeExecutor.create(v1 - 1);
        timeExecute.registerWorker(timerList);
        timeExecute.registerManager(this);
        try {
            dealData = EventLoopFactory.buildDataEventLoop(v1).with(dataExecutor).andRegister(this).build();
            dealAccept = EventLoopFactory.buildConnEventLoop().bindPort(port).with(dataExecutor, parentHandle, childrenHandle).andRegister(this).build();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    @Override
    public GunDataEventLoop<SocketChannel> dealChannelEventLoop(int i) {
        return dealData[i];
    }

    @Override
    public Set<SelectionKey> availableChannel(int i) {
        try {
            return dealData[i].availableSelectionKey();
        } catch (IOException e) {
            return null;
        }

    }


    @Override
    public GunNettyTransfer<SocketChannel> transferEventLoop() {
        return transfer;
    }

    @Override
    public GunConnEventLoop connEventLoop() {
        return dealAccept;
    }

    @Override
    public GunDataEventLoop[] dataEventLoop() {
        return dealData;
    }


    @Override
    public GunTimeExecutor timeEventLoop() {
        return timeExecute;
    }

    @Override
    public void shutDown() {
        this.dealAccept.stopEventLoop();
        this.transfer.stopEventLoop();
        this.timeExecute.shutdown();
        Arrays.stream(dealData).parallel().forEach(GunNettyVariableWorker::stopEventLoop);
    }
}