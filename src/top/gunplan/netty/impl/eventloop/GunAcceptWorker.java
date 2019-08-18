/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.eventloop;

import top.gunplan.netty.GunChannelException;
import top.gunplan.netty.GunNettyConnFilter;
import top.gunplan.netty.GunNettyFilter;
import top.gunplan.netty.GunOutboundChecker;
import top.gunplan.netty.impl.GunNettyOutputFilterChecker;
import top.gunplan.netty.impl.channel.GunNettyChildChannel;
import top.gunplan.netty.protocol.GunNetOutbound;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ListIterator;

/**
 * GunAcceptWorker
 *
 * @author dosdrtt
 * @date 2019-04-25
 */
public final class GunAcceptWorker extends BaseGunNettyWorker implements Runnable {

    GunAcceptWorker(final GunNettyChildChannel<SocketChannel> l) {
        super(l);
    }


    @Override
    public void work() {
        GunNetOutbound outbound = null;
        try {
            outbound = pHandle.dealConnEvent(channel.remoteAddress());
        } catch (IOException e) {
            if (handle.dealExceptionEvent(new GunChannelException(e)) != GunNettyFilter.DealResult.NEXT) {
                return;
            }
        }
        GunOutboundChecker checker = new GunNettyOutputFilterChecker(outbound, channel);
        ListIterator<GunNettyConnFilter> iterator = connFilters.listIterator(connFilters.size());
        GunNettyFilter.DealResult result;
        do {
            try {
                result = iterator.previous().doOutputFilter(checker);
            } catch (GunChannelException e) {
                result = handle.dealExceptionEvent(e);
            }
        }
        while (iterator.hasPrevious() && (result != GunNettyFilter.DealResult.NEXT));


    }
}
