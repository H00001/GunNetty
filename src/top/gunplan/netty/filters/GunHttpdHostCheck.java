package top.gunplan.netty.filters;

import top.gunplan.netty.GunNettyFilter;
import top.gunplan.netty.anno.GunNetFilterOrder;
import top.gunplan.netty.common.GunNettyPropertyManager;
import top.gunplan.netty.impl.GunRequestFilterDto;
import top.gunplan.netty.impl.example.GunOutputFilterDto;
import top.gunplan.netty.protocol.GunHttp2InputProtocl;

@GunNetFilterOrder(index = 2)
public class GunHttpdHostCheck implements GunNettyFilter {
    @Override
    public DealResult doInputFilter(GunRequestFilterDto filterDto) {
        GunHttp2InputProtocl httpmessage = (GunHttp2InputProtocl) filterDto.getObject();
        return httpmessage.getRequstHead().get("Host").equals(GunNettyPropertyManager.getHttp().getHttphost()) ? DealResult.NEXT : DealResult.NOTDEALALLNEXT;
    }

    @Override
    public DealResult doOutputFilter(GunOutputFilterDto filterDto) {
        return DealResult.NEXT;
    }
}
