/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.timeevent;

import top.gunplan.netty.GunNettyTimer;
import top.gunplan.netty.impl.GunNettyEventLoopManager;
import top.gunplan.netty.impl.sequence.GunNettySequencer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.LongAdder;


/**
 * AbstractGunTimeExecutor
 *
 * @author dosdrtt
 * @date 2019/05/30
 */
public abstract class AbstractGunTimeExecutor implements GunTimeExecutor {
    volatile List<GunNettyTimer> works = new CopyOnWriteArrayList<>();
    volatile LongAdder sum = new LongAdder();
    final GunNettySequencer sequencer = GunNettySequencer.newThreadSafeSequencer();
    volatile GunNettyEventLoopManager manager;

    public static GunTimeExecutor create(int v) {
        return new GunNettyTimeExecuteImpl(v);
    }

    @Override
    public GunTimeExecutor registerManager(GunNettyEventLoopManager manager) {
        this.manager = manager;
        return this;
    }

    /**
     * when event loop execute
     */


    @Override
    public GunTimeExecutor registerWorker(List<GunNettyTimer> works) {
        this.works.addAll(works);
        return this;
    }

    @Override
    public GunTimeExecutor registerWorker(GunNettyTimer work) {
        this.works.add(work);
        return this;
    }

    @Override
    public GunTimeExecutor eraserWorker(GunNettyTimer work) {
        this.works.remove(work);
        return this;
    }
}