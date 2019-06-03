package top.gunplan.netty.impl;

import top.gunplan.netty.GunNettyFilter;
import top.gunplan.netty.GunFunctionMappingInterFace;
import top.gunplan.netty.anno.GunNetFilterOrder;
import top.gunplan.netty.impl.propertys.GunNettyCoreProperty;
import top.gunplan.netty.protocol.GunNetOutputInterface;
import top.gunplan.utils.AbstractGunBaseLogUtil;
import top.gunplan.utils.GunBytesUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * GunNettyStdFirstFilter First Input Filter and Last Output Filter
 * this class is high dangerous
 *
 * @author dosdrtt
 */
@GunNetFilterOrder
public class GunNettyStdFirstFilter implements GunNettyFilter {

    public GunNettyStdFirstFilter() {
        coreProperty = GunNettyPropertyManagerImpl.coreProperty();
    }


    private void dealCloseEvent(SelectionKey key) throws IOException {
        AbstractGunBaseLogUtil.debug("Client closed", "[CONNECTION]");
        key.channel().close();
    }

    private GunNettyCoreProperty coreProperty;

    @Override
    public DealResult doInputFilter(GunInputFilterChecker filterDto) throws Exception {

        byte[] data;
        SelectionKey key = filterDto.getKey();

        if (key.isValid()) {
            try {
                GunFunctionMappingInterFace<SocketChannel, byte[]> reader = GunBytesUtil::readFromChannel;
                data = reader.readBytes((SocketChannel) key.channel());
                filterDto.setSrc(data);
            } catch (IOException e) {
                dealCloseEvent(key);
                AbstractGunBaseLogUtil.error(e);
                return DealResult.CLOSE;
            }
            if (data == null) {
                dealCloseEvent(key);
                return DealResult.CLOSE;
            } else {
                if (coreProperty.getConnection() == GunNettyCoreProperty.connectionType.CLOSE) {
                    filterDto.getKey().cancel();
                } else if (coreProperty.getConnection() == GunNettyCoreProperty.connectionType.KEEP_ALIVE) {
                    key.interestOps(SelectionKey.OP_READ);
                    ((GunCoreDataEventLoop) key.attachment()).incrAndContinueLoop();

                }
                return DealResult.NEXT;

            }
        } else {
            return DealResult.NOTDEALALLNEXT;
        }
    }

    @Override
    public DealResult doOutputFilter(GunOutputFilterChecker filterDto) throws IOException {
        SocketChannel channel = (SocketChannel) filterDto.getKey().channel();
        sendMessage(filterDto.getRespobj(), channel);
        if (coreProperty.getConnection() == GunNettyCoreProperty.connectionType.CLOSE) {
            channel.close();
            AbstractGunBaseLogUtil.debug("close initiative");
        }
        return DealResult.NEXT;
    }

    private void sendMessage(GunNetOutputInterface opt, SocketChannel channel) throws IOException {
        if (opt != null) {
            if (channel.isOpen()) {
                channel.write(ByteBuffer.wrap(opt.serialize()));
            }
        }
    }

    @Override
    public DealResult doOutputFilter(GunOutputFilterChecker filterDto, SocketChannel channel) throws Exception {
        sendMessage(filterDto.getRespobj(), channel);
        return DealResult.NEXT;
    }
}