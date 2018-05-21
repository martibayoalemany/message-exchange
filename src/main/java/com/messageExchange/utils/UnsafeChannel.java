package com.messageExchange.utils;


import sun.misc.Unsafe;
import sun.nio.ch.FileChannelImpl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.FileChannel;

/**
 * This solution does not yet work
 */
@Deprecated
public class UnsafeChannel {
    private static final Unsafe UNSAFE;
    private static final Method mmap;
    private static final Method unmmap;

    static {
        try {
            for(Method m : FileChannelImpl.class.getMethods()) {
                System.out.println(m.getName());
            }
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
            mmap = FileChannelImpl.class.getMethod( "map0", int.class, long.class, long.class);
            unmmap = FileChannelImpl.class.getMethod( "unmap0", long.class, long.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Long addr = null;
    private FileChannel ch;

    public static UnsafeChannel of(FileChannel ch) {
        UnsafeChannel unsafeChannel = new UnsafeChannel();
        unsafeChannel.ch = ch;
        return unsafeChannel;
    }

    public UnsafeChannel setAddressUnsafe(long size) {
        try {
            System.setSecurityManager(new SecurityManager());

            this.addr = (long) mmap.invoke(ch, 1, 0L, size);
        } catch (IllegalAccessException | InvocationTargetException e) {
            System.err.println(e.getMessage());
        }
        return this;
    }

    public byte getByteVolatile(long pos) {
        return UNSAFE.getByteVolatile(null, pos + addr);
    }

    public void putByteVolatile(long pos, byte val) {
        UNSAFE.putByteVolatile(null, pos + addr, val);
    }

}