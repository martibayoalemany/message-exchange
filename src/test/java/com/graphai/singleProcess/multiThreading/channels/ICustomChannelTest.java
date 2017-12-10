package com.graphai.singleProcess.multiThreading.channels;

import com.graphai.singleProcess.multiThreading.ICustomChannel;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(Theories.class)
public class ICustomChannelTest {
    public static @DataPoints
    ICustomChannel[] channels =  {
            new ArrayBasedChannel(10),
            new FileBasedChannel(10),
            new MemoryChannel(10),
            new ArrayBasedChannel(0),
            new ArrayBasedChannel(1),
            new ArrayBasedChannel(2),
    };

    @Theory
    public void get(ICustomChannel channel) throws Exception {
        for (int i = 0; i < channel.getBufferSize(); i++) {
            channel.put(i, (byte)i);
        }
        for (int i = 0; i < channel.getBufferSize(); i++) {
            assertEquals(channel.get(i), i);
        }
    }

    @Theory
    public void next_index(ICustomChannel channel) throws Exception {
        if(channel.getBufferSize() == 0) {
            assertTrue(channel.nextIndex(0) == -1);
        }
        else if(channel.getBufferSize() == 1) {
            for (int i = 0; i < 2; i++)
                assertTrue(channel.nextIndex(0) == 0);
        }
        else if(channel.getBufferSize() == 2) {
            assertEquals(0, channel.nextIndex(1));
            assertEquals(1, channel.nextIndex(0));
        }
    }

}