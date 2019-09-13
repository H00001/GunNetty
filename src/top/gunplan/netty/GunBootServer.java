/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty;

import top.gunplan.netty.impl.GunNettyCoreThreadManager;
import top.gunplan.netty.observe.GunNettyServicesObserve;


/**
 * server interface ,has kinds of implements but now we have only one
 * it is NioStdServerImpl
 *
 * @author dosdrtt
 * @date 2019-04-21
 * @since 0.0.0.3
 */
public interface GunBootServer extends GunBootServerBase, GunServerStateManager {
    /**
     * stop
     * <p>
     * stop the server
     *
     * @return stop result
     * 0:normal
     * other value : not normal
     * @throws InterruptedException when stoping the server
     */
    @Override
    int stop() throws InterruptedException;

    /**
     * register observe
     *
     * @param observe observe object
     * @return GunBootServer
     */
    GunBootServer registerObserve(GunNettyServicesObserve observe);

    /**
     * GunNettyCoreThreadManager
     *
     * @return threadManager
     */
    GunNettyCoreThreadManager threadManager();


    /**
     * sync
     * boot by synchronized or asynchronizated
     *
     * @return sync result
     * @throws GunNettyCanNotBootException exception very urgency
     */
    @Override
    int sync() throws GunNettyCanNotBootException;

    /**
     * set the Thread pool that dispose the request
     *
     * @param var1  this Executor is used to deal with accept request
     * @param var2 this Executor is used to deal with data request
     * @return this
     */

    GunBootServer setExecutors(int var1, int var2);


    /**
     * check it can or not be boot
     *
     * @return check result true is can be boot
     */
    boolean initCheck();


    /**
     * get time manager
     *
     * @return time manager
     */
    GunTimeEventManager timeManager();

    /**
     * when has a new channel
     *
     * @param initHandle input the pipeline
     */

    void onHasChannel(ChannelInitHandle initHandle);


    /**
     * is or not use steal pool
     */
    default GunBootServer useStealMode(boolean use) {
        throw new GunException(GunExceptionType.NOT_SUPPORT, "not support steal mode");
    }

    /**
     * server event handle
     *
     * @param handle handle
     */
    void whenServerChannelStateChanged(SystemChannelChangedHandle handle);

}

