/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.filter;

import top.gunplan.netty.GunChannelException;
import top.gunplan.netty.impl.checker.GunInboundChecker;

/**
 * GunNettyOutboundFilter
 *
 * @author frank albert
 * @version 0.0.0.1
 */

@FunctionalInterface
public interface GunNettyOutboundFilter extends GunNettyDataFilter {
    /**
     * doInputFilter
     *
     * @param filterDto input filter dto
     * @return DealResult
     * @throws GunChannelException channel i/o error
     */
    @Override
    default DealResult doInputFilter(GunInboundChecker filterDto) throws GunChannelException {
        return DealResult.NEXT;
    }
}
