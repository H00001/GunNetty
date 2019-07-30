package top.gunplan.netty.impl.eventloop;


import top.gunplan.netty.common.GunNettyContext;
import top.gunplan.netty.impl.GunNettyChannelTransfer;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * GunNettyBaseTransferEventLoopImpl
 *
 * @author frank albert
 * @version 0.0.0.e
 * @date 2019-06-09 22:10
 */
class GunNettyBaseTransferEventLoopImpl<U extends SocketChannel> extends AbstractGunTransferEventLoop<U> {

    private final BlockingQueue<U> kQueue = new LinkedBlockingQueue<>();


    @Override
    public void push(GunNettyChannelTransfer<U> u) {
        kQueue.offer(u.channel());
    }

    @Override
    public void loopTransfer() {
        try {
            U socketChannel = kQueue.take();
            final SelectionKey key = registerReadChannelToDataEventLoop(socketChannel);
            dealEvent(key);
        } catch (IOException e) {
            GunNettyContext.logger.error(e);
        } catch (InterruptedException ignored) {

        }
    }
}
