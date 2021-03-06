/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl;

import java.nio.channels.Selector;

/**
 * GunNettySelectorFactory
 *
 * @author frank albert
 * @version 0.0.0.1
 * @date 2019-12-21 09:20
 */
public interface GunNettySelectorFactory {

    GunNettySelectorFactory INSTANCE = new GunNettySelectorFactoryImpl();

    /**
     * get a new instance
     *
     * @return instance
     */
    Selector allocate();

    /**
     * release the instance to pool
     * @param selector instance
     */
    void release(Selector selector);
}
