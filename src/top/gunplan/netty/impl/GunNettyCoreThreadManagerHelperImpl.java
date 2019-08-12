/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl;

import top.gunplan.netty.common.GunNettyExecutors;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

final class GunNettyCoreThreadManagerHelperImpl implements GunNettyCoreThreadManagerHelper {

    private final ExecutorService DASERVER_POOL;
    private final ExecutorService TRANSFER_POOL;
    private final ExecutorService ACCEPTCO_POOL;
    private final ScheduledExecutorService EXETIMER_POOL;
    private final ExecutorService[] EXE_POOL_LIST;

    GunNettyCoreThreadManagerHelperImpl(int num) {
        this.EXETIMER_POOL = GunNettyExecutors.newScheduleExecutorPool();
        this.TRANSFER_POOL = GunNettyExecutors.newSignalExecutorPool("CoreTransferThread");
        this.ACCEPTCO_POOL = GunNettyExecutors.newSignalExecutorPool("CoreAcceptThread");
        this.DASERVER_POOL = GunNettyExecutors.newFixedExecutorPool(num, "CoreDataThread");
        EXE_POOL_LIST = new ExecutorService[]{ACCEPTCO_POOL, TRANSFER_POOL, DASERVER_POOL, EXETIMER_POOL};
    }

    @Override
    public ExecutorService acceptExecutorServices() {
        return DASERVER_POOL;
    }

    @Override
    public ExecutorService transferExecutorServices() {
        return TRANSFER_POOL;
    }

    @Override
    public ScheduledExecutorService timeExecutorServices() {
        return EXETIMER_POOL;
    }

    @Override
    public ExecutorService dataExecutorServices() {
        return ACCEPTCO_POOL;
    }

    @Override
    public ExecutorService[] allOfServices() {
        return EXE_POOL_LIST;
    }

    @Override
    public Future<Integer> submitTransfer(Runnable task) {
        return transferExecutorServices().submit(task, 0);
    }

    @Override
    public void submitData(Runnable[] tasks) {
        Arrays.stream(tasks).parallel().forEach(DASERVER_POOL::submit);
    }

    @Override
    public Future<Integer> submitConnection(Runnable task) {
        return ACCEPTCO_POOL.submit(task, 1);
    }

    @Override
    public void submitSchedule(Runnable task, long v1, long v2) {
        EXETIMER_POOL.scheduleAtFixedRate(task, v1, v2, TimeUnit.MILLISECONDS);
    }

    @Override
    public GunNettyCoreThreadManagerHelper shutdownReturn() {
        Arrays.stream(EXE_POOL_LIST).forEach(ExecutorService::shutdown);
        return this;
    }

    @Override
    public void syncStop() {
        Stream<ExecutorService> services = Arrays.stream(EXE_POOL_LIST).filter(who -> !who.isTerminated());
        services.parallel().forEach(any -> {
            try {
                for (; !any.isTerminated(); ) {
                    any.awaitTermination(1, TimeUnit.MINUTES);
                }
            } catch (InterruptedException ignore) {
            }
        });
    }
}