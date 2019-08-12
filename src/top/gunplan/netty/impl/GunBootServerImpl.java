/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl;

import top.gunplan.netty.*;
import top.gunplan.netty.common.GunNettyContext;
import top.gunplan.netty.impl.property.GunNettyCoreProperty;
import top.gunplan.netty.impl.property.base.GunNettyPropertyManager;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


/**
 * GunBootServer's real implement ,this class is not public
 *
 * @author Gunplan
 * @version 0.1.1.4
 * @apiNote 0.0.0.5
 * @see GunBootServer
 * @since 0.0.0.5
 */

final class GunBootServerImpl implements GunBootServer {
    private volatile boolean isSync;

    private volatile GunNettyCoreThreadManager threadManager;

    private volatile boolean runnable = false;

    private volatile GunNettyObserve observe;

    private volatile ExecutorService acceptExecutor;

    private volatile ExecutorService requestExecutor;

    private volatile GunNettyPipeline pipeline = new GunNettyPipelineImpl();


    ChannelInitHandle initHandle;

    private volatile GunNettyCoreProperty coreProperty;

    GunBootServerImpl() {
        observe = new GunNettyDefaultObserve();


    }

    @Override
    public final boolean isSync() {
        return isSync;
    }

    @Override
    public GunBootServer registerObserve(GunNettyObserve observe) {
        if (observe != null) {
            this.observe = observe;
        }
        return this;
    }

    @Override
    public GunNettyCoreThreadManager manager() {
        return threadManager;
    }

    @Override
    public boolean isRunnable() {
        return this.runnable;

    }


    @Override
    public GunNettyPipeline pipeline() {
        return pipeline;
    }

    @Override
    public void setPipeline(GunNettyPipeline pipeline) {
        if (pipeline != null) {
            this.pipeline = pipeline;
        } else {
            throw new GunException(GunExceptionType.NULLPTR, "Your GunNetty Pipeline is null");
        }
    }

    @Override
    public void onHasChannel(ChannelInitHandle initHandle) {
        this.initHandle = initHandle;
    }

    @Override
    public boolean initCheck() {
        if (acceptExecutor == null) {
            throw new GunException(GunExceptionType.EXC0, "acceptExecutor is null");
        } else if (requestExecutor == null) {
            throw new GunException(GunExceptionType.EXC0, "requestExecutor is null");
        } else if (this.pipeline.check().getResult() == GunPipelineCheckResult.CheckResult.ERROR) {
            throw new GunException(GunExceptionType.EXC0, "handle or chain result is not normal");
        } else if (runnable) {
            throw new GunException(GunExceptionType.STATE_ERROR, "system has running");
        }
        return true;
    }

    @Override
    public int stop() throws InterruptedException {
        this.pipeline.destroy();
        if (threadManager.stopAndWait()) {
            this.runnable = false;
        }
        return GunNettyWorkState.STOP.state;
    }

    @Override
    public void setSyncType(boolean b) {
        isSync = b;
    }

    private void init() {
        coreProperty = GunNettySystemServices.coreProperty();
        threadManager = GunNettyCoreThreadManager.
                initInstance(GunNettySystemServices.coreProperty(), observe);
    }


    @Override
    public synchronized int sync() throws GunNettyCanNotBootException {
        if (baseParameterCheck() == 0) {
            init();
        } else {
            return GunNettyWorkState.BOOT_ERROR_2.state;
        }
        try {
            threadManager.init(acceptExecutor, requestExecutor, initHandle, pipeline, coreProperty.getPort());
        } catch (IOException exc) {
            GunNettyContext.logger.setTAG(GunNettyCanNotBootException.class).urgency(exc.getMessage());
            return GunNettyWorkState.BOOT_ERROR_1.state;
        }
        if (this.observe.onBooting(coreProperty)) {
            pipeline.init();
            Future<Integer> executing = threadManager.startAndWait();
            this.observe.onBooted(coreProperty);
            this.runnable = true;
            if (isSync) {
                try {
                    int val = executing.get();
                    pipeline.destroy();
                    this.observe.onStatusChanged(GunNettyObserve.GunNettyChangeStatus.RUN_TO_STOP);
                    this.observe.onStop(coreProperty);
                    threadManager.stopAndWait();
                    return val;
                } catch (InterruptedException | ExecutionException e) {
                    throw new GunNettyCanNotBootException(e);
                }

            } else {
                return (GunNettyWorkState.ASYNC.state | GunNettyWorkState.RUNNING.state);
            }
        }
        return GunNettyWorkState.BOOT_ERROR_1.state;
    }

    private int baseParameterCheck() {
        final GunNettyPropertyManager propertyManager = GunNettySystemServices.PROPERTY_MANAGER;
        if (!this.initCheck() || !propertyManager.initProperty()) {
            throw new GunException(GunExceptionType.EXC0, "Exception has been threw");
        }
        return 0;
    }


    @Override
    public GunBootServer setExecutors(ExecutorService acceptExecuters, ExecutorService requestExecuters) {
        this.acceptExecutor = acceptExecuters;
        this.requestExecutor = requestExecuters;
        return this;
    }

}
