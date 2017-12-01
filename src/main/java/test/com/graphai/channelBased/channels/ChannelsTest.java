package com.graphai.channelBased.channels;

import com.graphai.channelBased.Channels;
import com.graphai.channelBased.channels.IChannel;
import com.graphai.channelBased.channels.Message;
import org.junit.Before;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(Theories.class)
public class ChannelsTest {

    private List<Message> messages = new ArrayList<>();

    public static @DataPoints
    IChannel[] channels =  {
            Channels.getSynchronized(),
            Channels.get(false),
            Channels.get(true),
            Channels.getObservable()
    };

    @Before
    public void Setup() {
        messages.add(Message.ping(null, UUID.randomUUID()));
        messages.add(Message.reply(null,  messages.get(0)));
    }


    @Theory
    public void push(IChannel channel) throws Exception {
        List<Message> result = channel.push(messages.get(0));
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), equalTo(1));
        List<Message> retrieved = channel.pop((m) -> true);
        assertThat(retrieved, is(notNullValue()));
        assertThat(result.size(), equalTo(1));

    }

    @Theory
    public void pushAll(IChannel channel) throws Exception {
        List<Message> result = channel.pushAll(messages);
        assertThat(result, is(notNullValue()));
        assertThat(result.size(), equalTo(2));
        List<Message> retrieved = channel.pop((m) -> true);
        assertThat(retrieved, is(notNullValue()));
        assertThat(result.size(), equalTo(2));
    }
}