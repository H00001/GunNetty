/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.gunplan.netty.protocol;


import top.gunplan.netty.impl.GunNetBound;

/**
 * GunNetOutbound
 *
 * @author dosdrtt
 */
public interface GunNetOutbound extends GunNetBound {
    /**
     * serialize the protocol
     *
     * @return bytes[] transferTarget to client
     */
    byte[] serialize();

}

