/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.gunplan.netty.impl.property.base;

import java.io.IOException;

/**
 * PropertyDataBuilder
 *
 * @author frank albert
 * @version 0.0.0.1
 * @date 2019-08-06 13:09
 */
@FunctionalInterface
public interface PropertyDataBuilder<D> {
    /**
     * create data
     *
     * @return data to get properties
     * @throws IOException read error
     */
    D create() throws IOException;
}