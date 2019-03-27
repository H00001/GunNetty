package top.gunplan.netty.unittest;

import org.junit.jupiter.api.Test;
import top.gunplan.netty.GunBootServer;
import top.gunplan.netty.filters.GunStdHttp2Filter;
import top.gunplan.netty.filters.GunStdToStringFilter;
import top.gunplan.netty.handles.GunStdHttpHandle;
import top.gunplan.netty.impl.GunBootServerFactory;
import top.gunplan.netty.impl.example.GunOutputHander;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;


/**
 * @author dosdrtt
 */
public class GunTestJunit {
    @Test
    public void doTest() throws Exception {


    }

    public static void main(String[] args) throws IOException {
        GunBootServer server = GunBootServerFactory.getInstance();
        server.setExecuters(Executors.newFixedThreadPool(10), Executors.newFixedThreadPool(10)).addFilter(new GunStdHttp2Filter()).setHandel(new GunStdHttpHandle("top.gunplan.netty.test"));
        try {
            server.sync();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}