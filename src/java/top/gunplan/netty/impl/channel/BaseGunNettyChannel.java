/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.channel;

import top.gunplan.netty.GunCoreEventLoop;
import top.gunplan.netty.GunNettyTimer;
import top.gunplan.netty.common.GunNettyExecutors;
import top.gunplan.netty.impl.channel.pool.GunChildrenChannelPool;
import top.gunplan.netty.impl.pipeline.GunNettyPipeline;
import top.gunplan.netty.impl.sequence.GunNettySequencer;
import top.gunplan.netty.observe.GunNettyChannelObserve;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.NetworkChannel;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * BaseGunNettyChannel
 *
 * @author frank albert
 * @version 0.0.1.1
 * # 2019-08-09 23:07
 */

abstract class BaseGunNettyChannel<CH extends NetworkChannel, LOOP extends GunCoreEventLoop, PL extends GunNettyPipeline> implements GunNettyChannel<CH, LOOP, PL> {
    private final PL pipeline;
    private final long id;
    private final Queue<Object> eventQueue = new ConcurrentLinkedQueue<>();
    private volatile GunChildrenChannelPool pool = null;
    LOOP eventLoop;
    List<GunNettyTimer> timers;
    GunNettySequencer unsafeSequencer = GunNettySequencer.newThreadUnSafeSequencer();
    private ScheduledExecutorService scheduledExecutorService;
    private CH channel;
    private final SocketAddress localAddress;
    List<GunNettyChannelObserve> observes = new CopyOnWriteArrayList<>();

    BaseGunNettyChannel(final PL pipeline, final CH channel, final long id) throws IOException {
        this.id = id;
        this.pipeline = pipeline;
        this.channel = channel;
        timers = pipeline != null ? pipeline.timers() : null;
        scheduledExecutorService = GunNettyExecutors.newScheduleExecutorPool(timers != null ? timers.size() : 0);
        scheduledExecutorService.scheduleAtFixedRate(this::doTime, 100, 100, TimeUnit.MILLISECONDS);
        this.localAddress = channel.getLocalAddress();
    }


    @Override
    public GunNettyChannel<CH, LOOP, PL> registerEventLoop(LOOP eventLoop) {
        this.eventLoop = eventLoop;
        return this;
    }

    @Override
    public SocketAddress localAddress() {
        return localAddress;
    }

    @Override
    public PL pipeline() {
        return pipeline;
    }


    @Override
    public long channelId() {
        return id;
    }

    @Override
    public CH channel() {
        return channel;
    }


    @Override
    public void close() throws IOException {
        channel.close();
    }

    /**
     * do time event loop
     */
    public abstract void doTime();


    @Override
    public void destroy() {
        scheduledExecutorService.shutdown();
        System.gc();
    }

    public Object consumeEvent() {
        return eventQueue.poll();
    }


    public boolean pushEvent(Object event) {
        return eventQueue.offer(event);
    }


    public void cleanEvent() {
        eventQueue.clear();
    }

    @Override
    public List<GunNettyTimer> timers() {
        return timers;
    }


    @Override
    public void doReset() {
        cleanEvent();
    }

    @Override
    public boolean isUsed() {
        return false;
    }


    @Override
    public boolean isOpen() {
        return channel().isOpen();
    }


    public void addObserve(GunNettyChannelObserve observe) {
        observes.add(observe);
    }

    public void cleanAllObserve() {
        observes.clear();
    }
}