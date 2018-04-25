package com.mindata.blockchain.core.queue;

import com.lmax.disruptor.EventHandler;
import com.mindata.blockchain.ApplicationContextProvider;
import com.mindata.blockchain.core.queue.base.BaseEvent;

/**
 * @author wuweifeng wrote on 2018/4/20.
 */
public class DisruptorClientHandler implements EventHandler<BaseEvent> {

    @Override
    public void onEvent(BaseEvent baseEvent, long sequence, boolean endOfBatch) throws Exception {
        ApplicationContextProvider.getBean(DisruptorClientConsumer.class).receive(baseEvent);
    }
}