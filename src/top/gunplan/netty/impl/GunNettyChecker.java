/*
 * Copyright (c) frankHan personal 2017-2018
 */

package top.gunplan.netty.impl;

import top.gunplan.netty.impl.channel.GunNettyChildChannel;

import java.nio.channels.SocketChannel;

/**
 * GunNettyChecker
 * Checker filters's transferTarget Object interface
 *
 * @author dosdrtt
 * @version 0.0.0.2
 */
public interface GunNettyChecker<T extends GunNetBound> {
    /**
     * channel
     * get selectionKey
     *
     * @return SelectionKey
     */
    GunNettyChildChannel<SocketChannel> channel();


    /**
     * source information
     *
     * @return bytes
     */
    byte[] source();


    /**
     * set source
     *
     * @param src source
     */
    void setSource(byte[] src);


    /**
     * set the key
     *
     * @param key selection key
     */
    void setChannel(GunNettyChildChannel<SocketChannel> key);


    /**
     * set attached object
     *
     * @param attach object to attachment
     */
    void attachment(Object attach);

    /**
     * get attached object
     *
     * @return object to attachment
     */
    Object attachment();


    /**
     * you can set error to transfer
     *
     * @param throwable w
     */
    void setError(Throwable throwable);


    /**
     * get the error
     *
     * @return error
     */
    Throwable error();


    T transferTarget();
}