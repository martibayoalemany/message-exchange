package com.graphai.channelBased.channels;

import com.graphai.channelBased.channels.Message;
import org.hamcrest.MatcherAssert;
import org.junit.Test;


import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class MessageTest {
    @Test
    public void ping() throws Exception {
        MatcherAssert.assertThat(Message.ping(null, UUID.randomUUID()), is(notNullValue()));
    }

    @Test
    public void reply() throws Exception {
        Message ping = Message.ping(null, UUID.randomUUID());
        assertThat(ping, is(notNullValue()));
        Message reply = Message.reply(null, ping);
        assertThat(ping.getSender() == reply.getReceiver(), is(true));
    }

}