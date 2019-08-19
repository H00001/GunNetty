/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty;

import top.gunplan.netty.impl.property.GunLogProperty;
import top.gunplan.netty.impl.property.GunNettyCoreProperty;
import top.gunplan.netty.impl.property.base.GunNettyPropertyManager;
import top.gunplan.netty.impl.property.base.GunNettyPropertyManagerFactory;

/**
 * GunNettySystemServices
 *
 * @author frank albert
 * @version 0.0.0.2
 * @date 2019-07-27 08:08
 */

public final class GunNettySystemServices {
    public final static GunNettyPropertyManager PROPERTY_MANAGER = GunNettyPropertyManagerFactory.
            propertyInstance();


    public static GunNettyCoreProperty coreProperty() {
        return PROPERTY_MANAGER.acquireProperty(GunNettyCoreProperty.class);
    }

    public static GunLogProperty logProperty() {
        return PROPERTY_MANAGER.acquireProperty(GunLogProperty.class);
    }
}
