/**
 * Copyright 2016-2017 Sixt GmbH & Co. Autovermietung KG
 * Licensed under the Apache License, Version 2.0 (the "License"); you may 
 * not use this file except in compliance with the License. You may obtain a 
 * copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */

package com.sixt.service.framework.kafka;

import java.util.UUID;

public class KafkaSubscriberBuilder<TYPE> {

    protected KafkaSubscriberFactory parentFactory;
    protected EventReceivedCallback<TYPE> callback;
    protected String topic;
    protected String groupId = UUID.randomUUID().toString();
    protected boolean enableAutoCommit = false;
    protected KafkaSubscriber.OffsetReset offsetReset = KafkaSubscriber.OffsetReset.Earliest;
    protected int minThreads = 1;
    protected int maxThreads = 1;
    protected int idleTimeoutSeconds = 15;
    protected int pollTime = 1000;

    KafkaSubscriberBuilder(KafkaSubscriberFactory factory, String topic,
                                  EventReceivedCallback<TYPE> callback) {
        parentFactory = factory;
        this.topic = topic;
        this.callback = callback;
    }

    /**
     * Unless called, will initialize with test_service UUID group-id
     */
    public KafkaSubscriberBuilder withGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * Unless called, will initialize without auto-commit
     */
    public KafkaSubscriberBuilder withAutoCommit(boolean value) {
        this.enableAutoCommit = value;
        return this;
    }

    /**
     * Unless called, will initialize by starting at the earliest offset
     */
    public KafkaSubscriberBuilder withOffsetReset(KafkaSubscriber.OffsetReset value) {
        this.offsetReset = value;
        return this;
    }

    /**
     * Unless called, will initialize with single-threaded reader
     */
    public KafkaSubscriberBuilder withThreadPool(int minThreads, int maxThreads, int idleTimeoutSeconds) {
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.idleTimeoutSeconds = idleTimeoutSeconds;
        return this;
    }

    public KafkaSubscriberBuilder withPollTime(int pollTimeMillis) {
        this.pollTime = pollTimeMillis;
        return this;
    }

    @SuppressWarnings(value = "unchecked")
    public KafkaSubscriber<TYPE> build() {
        KafkaSubscriber<TYPE> retval = new KafkaSubscriber<>(callback, topic, groupId,
                enableAutoCommit, offsetReset, minThreads, maxThreads, idleTimeoutSeconds, pollTime);
        parentFactory.builtSubscriber(retval);
        return retval;
    }

}
