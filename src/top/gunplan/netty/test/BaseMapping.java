package top.gunplan.netty.test;

import top.gunplan.netty.anno.GunHttpmapping;
import top.gunplan.netty.handles.http.GunHttpMappingHandle;
import top.gunplan.netty.protocol.BaseGunHttp2Response;
import top.gunplan.netty.protocol.AbstractGunHttp2Response;
import top.gunplan.netty.protocol.GunHttpStdInfo;
import top.gunplan.netty.protocol.GunNetRequestInterface;

@GunHttpmapping(mappingRule = "/index.aspx")
public class BaseMapping implements GunHttpMappingHandle<AbstractGunHttp2Response> {
    public BaseMapping() {

    }

    @Override
    public AbstractGunHttp2Response doResponse(GunNetRequestInterface protocl) {
        BaseGunHttp2Response response = new BaseGunHttp2Response() {
            @Override
            public String toResponse() {
                return "<!DOCTYPE html>" +
                        "<html>" +
                        "<head>" +
                        "</head>" +
                        "<body>" +
                        "<h1>this is a web server by GunNetty</h1>" +
                        "<p>get start <a href=\"http://netty.gunplan.top\">download</a></p>" +
                        "</body>" +
                        "</html>";
            }
        };
        response.setIswrite(true);
        response.setProtoclType(GunHttpStdInfo.HttpProtoclType.HTTP2_0);
        response.setContentType(GunHttpStdInfo.ContentType.TEXT_HTML);

        return response;
        // return (;
    }
}
