package top.gunplan.netty.impl;


import top.gunplan.netty.GunCoreEventLoop;

import java.nio.channels.SocketChannel;
import java.util.Queue;

/**
 * GunNettyTransfer
 *
 * @author frank albert
 * @version 0.0.0.2
 * @date 2019-06-19 00:28
 */

public interface GunNettyTransfer<U extends SocketChannel> extends GunCoreEventLoop {
    /**
     * queue
     * push to queue
     */
    void push(U u);

    /**
     * loop the queue
     */
    void loop();
}
