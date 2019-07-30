package top.gunplan.netty.example;

import top.gunplan.netty.GunChannelException;
import top.gunplan.netty.GunException;
import top.gunplan.netty.GunNettyHandle;
import top.gunplan.netty.protocol.GunNetInbound;
import top.gunplan.netty.protocol.GunNetOutbound;

import java.net.SocketAddress;


/**
 * GunNettyStringHandle
 * a example
 *
 * @author frank albert
 * @version 0.0.0.1
 * @date 2019-07-27 08:08
 */
public class GunNettyStringHandle implements GunNettyHandle {
    @Override
    public GunNetOutbound dealDataEvent(GunNetInbound request) throws GunException {
        return (GunNetOutbound) request;
    }

    @Override
    public GunNetOutbound dealConnEvent(SocketAddress address) throws GunException {
        return null;
    }

    @Override
    public void dealCloseEvent() {

    }

    @Override
    public void dealExceptionEvent(GunChannelException exp) {

    }
}