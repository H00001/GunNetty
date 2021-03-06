/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.eventloop;


import top.gunplan.netty.GunCoreEventLoop;
import top.gunplan.netty.GunException;
import top.gunplan.netty.GunExceptionMode;
import top.gunplan.netty.impl.GunNettyChannelTransfer;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * GunNettyTransferEventLoop
 *
 * @author frank albert
 * @version 0.0.0.2
 * # 2019-06-19 00:28
 */

public interface GunNettyTransferEventLoop<U extends SocketChannel> extends GunCoreEventLoop {
    /**
     * queue
     * push to queue
     *
     * @param u transferTarget object
     */
    void push(GunNettyChannelTransfer<U> u);

    /**
     * loop the queue
     */
    @Override
    void loop();


    /**
     * deal event
     *
     * @param key SelectionKey input the SelectionKey
     */
    @Override
    default void dealEvent(SelectionKey key) {
        throw new GunException(GunExceptionMode.NOT_SUPPORT, "dealEvent not support");
    }
}
