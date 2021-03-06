/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.eventloop;

/**
 * GunNettyVariableWorker
 *
 * @author frank albert
 * @version 0.0.0.1
 * # 2019-07-21 15:12
 */
public interface GunNettyVariableWorker {

    /**
     * start
     */
    void startEventLoop();

    /**
     * status
     * @return is running
     */
    boolean isRunning();

    /**
     * stop
     */
    void stopEventLoop();
}
