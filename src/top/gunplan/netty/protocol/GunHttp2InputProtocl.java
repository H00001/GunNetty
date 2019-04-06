package top.gunplan.netty.protocol;

import top.gunplan.netty.anno.GunNetFilterOrder;

import java.util.HashMap;


/**
 * @author dosdrtt
 */
final public class GunHttp2InputProtocl implements GunNetInputInterface {
    public GunHttp2InputProtocl() {

    }

    private String requestBody;
    private HashMap<String, String> requstHead = new HashMap<>();
    private HashMap<String, String> http2Parameters = new HashMap<>(2);

    public String getRequestBody() {
        return requestBody;
    }

    private String requestUrl;

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    private GunHttpStdInfo.GunHttpRequestType method;

    public HashMap<String, String> getRequstHead() {
        return requstHead;
    }

    public void setRequstHead(HashMap<String, String> requstHead) {
        this.requstHead = requstHead;
    }

    public GunHttpStdInfo.GunHttpRequestType getMethod() {
        return method;
    }

    public void setMethod(GunHttpStdInfo.GunHttpRequestType method) {
        this.method = method;
    }


    public String getRequestUrl() {
        return requestUrl;
    }

    @Override
    public boolean unSerialize(byte[] in) {
        String httpconetnt = new String(in);
        int postion = httpconetnt.indexOf("\r\n");
        this.analyzingHttpHeadFirst(httpconetnt.substring(0, postion));
        int spiltpoint = httpconetnt.indexOf("\r\n\r\n");
        this.analyzingHttpHead(httpconetnt.substring(postion + 2, spiltpoint).split("\r\n"));
        if (this.method == GunHttpStdInfo.GunHttpRequestType.POST) {
            functionToDealPostMethod();
        }
        return true;
    }


    private void functionToDealPostMethod() {


    }

    private void analyzingHttpHeadFirst(final String httpHeadFirst) {
        do {
            if (httpHeadFirst.startsWith(GunHttpStdInfo.GunHttpRequestType.GET.getVal())) {
                this.method = GunHttpStdInfo.GunHttpRequestType.GET;
                break;
            } else if (httpHeadFirst.startsWith(GunHttpStdInfo.GunHttpRequestType.POST.getVal())) {
                this.method = GunHttpStdInfo.GunHttpRequestType.POST;
                break;
            }
            if (httpHeadFirst.startsWith(GunHttpStdInfo.GunHttpRequestType.PUT.getVal())) {
                this.method = GunHttpStdInfo.GunHttpRequestType.PUT;
                break;
            }
            if (httpHeadFirst.startsWith(GunHttpStdInfo.GunHttpRequestType.DELETE.getVal())) {
                this.method = GunHttpStdInfo.GunHttpRequestType.DELETE;
                break;
            }
        } while (false);
        String requrl = httpHeadFirst.split(" ")[1];
        var3310:
        if (requrl.contains("?")) {
            this.requestUrl = requrl.split("\\?")[0];
            String parameters[];
            if (requrl.split("\\?")[1].contains("&")) {
                parameters = requrl.split("&");
            } else {
                parameters = new String[1];
                parameters[0] = requrl.split("\\?")[1];
            }
            for (String parameter : parameters) {
                http2Parameters.put(parameter.split("=")[0], parameter.split("=")[1]);
            }

        } else {
            this.requestUrl = requrl;
        }
    }


    private void analyzingHttpHead(String[] httphead) {
        for (String eachhead : httphead) {
            requstHead.put(eachhead.split(":")[0].trim(), eachhead.split(":")[1].trim());
        }
    }
}
