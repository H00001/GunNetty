/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.eventloop;

import top.gunplan.netty.ChannelInitHandle;
import top.gunplan.netty.GunCoreEventLoop;
import top.gunplan.netty.SystemChannelChangedHandle;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.concurrent.ExecutorService;

/**
 * GunConnEventLoop
 *
 * @author frank albert
 * @version 0.0.0.2
 * @date 2019-07-23 00:11
 */
public interface GunConnEventLoop extends GunCoreEventLoop {
    /**
     * openPort
     * open port
     *
     * @param port port
     * @return succeed , normal
     */
    int openPort(int... port);


    /**
     * listen port
     *
     * @return port
     */
    int listenPort();


    Selector select();


    int init(ExecutorService service, SystemChannelChangedHandle handle, ChannelInitHandle childrenHandle) throws IOException;
}
