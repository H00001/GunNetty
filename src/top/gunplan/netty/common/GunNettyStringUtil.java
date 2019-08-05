/*
 * Copyright (c) 2019. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package top.gunplan.netty.common;

/**
 * @author dosdrtt
 */
public final class GunNettyStringUtil {
    public static boolean isEmpty0(String in) {
        return in != null && in.trim().length() != 0;
    }

    public static boolean isEmpty(String in) {
        return !isEmpty0(in);
    }

    public static boolean isEmpty0(String[] in) {
        return in == null || in.length == 0;
    }
}
