/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.channel;

import top.gunplan.netty.GunCoreEventLoop;
import top.gunplan.netty.GunNettyTimer;
import top.gunplan.netty.impl.channel.pool.GunNettyChannelReuse;
import top.gunplan.netty.impl.pipeline.GunNettyPipeline;
import top.gunplan.netty.observe.GunNettyChannelObserve;

import java.net.SocketAddress;
import java.nio.channels.Channel;
import java.util.List;

/**
 * GunNettyChannel
 *
 * @author frank albert
 * @version 0.0.0.2
 */
public interface GunNettyChannel<CH extends Channel,
        LOOP extends GunCoreEventLoop,
        PL extends GunNettyPipeline> extends Channel, GunNettyChannelReuse {

    /**
     * get the handle that the channel attachment
     *
     * @return handle
     */
    PL pipeline();


    /**
     * get java channel to transfer
     *
     * @return java channel
     */
    CH channel();


    /**
     * is or not close
     *
     * @return boolean
     */

    boolean isClose();
    /**
     * get channel id
     *
     * @return cid
     */
    long channelId();


    /**
     * set channel id
     * registerEventLoop
     *
     * @param eventLoop loop
     * @return self
     */

    GunNettyChannel<CH, LOOP, PL> registerEventLoop(LOOP eventLoop);

    /**
     * get remote address
     *
     * @return SocketAddress
     */
    SocketAddress remoteAddress();

    /**
     * get local address
     *
     * @return SocketAddress
     */
    SocketAddress localAddress();


    /**
     * Tells whether or not this key is valid.
     *
     * <p> A key is valid upon creation and remains so until it is cancelled,
     * its channel is closed, or its selector is closed.  </p>
     *
     * @return {@code true} if, and only if, this key is valid
     */
    boolean isValid();


    /**
     * when channel not use
     */
    void destroy();


    /**
     * get timers
     *
     * @return timers
     */
    List<GunNettyTimer> timers();


    /**
     * addObserve
     *
     * @param observe observe
     */
    void addObserve(GunNettyChannelObserve observe);

    /**
     * remove all observe
     */
    void cleanAllObserve();


    void execute(Runnable run);

}
