/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl.property;

import top.gunplan.netty.GunBootServerBase;
import top.gunplan.netty.GunProperty;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * GunGetPropertyFromNet
 *
 * @author frank albert
 * @version 0.0.0.1
 * @date 2019-08-06 08:31
 */

public class GunGetPropertyFromNet implements GunPropertyStrategy {

    private String address;
    private GunNettyPropertyExporter exporter = new GunNettyPropertyExporter() {
    };

    private GunNettyPropertyAnalyzer<String, String[]> analyzer = new AbstractGunNettyStandStringPropertyAnalysiser() {
        @Override
        public void nextAnalyze(Map<String, GunProperty> propertiesMap, String info) throws GunBootServerBase.GunNettyCanNotBootException {
            GunGetPropertyFromNet.this.address = info;
            GunGetPropertyFromNet.this.settingProperties(propertiesMap);
        }
    };


    public GunGetPropertyFromNet(String address) {
        this.address = address;
    }

    @Override
    public boolean settingProperties(Map<String, GunProperty> propertyMap) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(address))
                .build();


        exporter.export("acquire configure from :" + address);
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .whenCompleteAsync((body, en) -> {
                    if (en == null) {
                        try {
                            analyzer.analyzingProperties(body.split("\n"), propertyMap);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            throw new GunBootServerBase.GunNettyCanNotBootException(e);
                        }
                    } else {
                        throw new GunBootServerBase.GunNettyCanNotBootException(en);
                    }
                })
                .join();

        return true;
    }
}