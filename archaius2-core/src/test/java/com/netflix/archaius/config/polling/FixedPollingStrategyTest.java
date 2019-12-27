/**
 * Copyright 2015 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.netflix.archaius.config.polling;

import java.io.IOException;

import java.util.Collections;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.NoSuchElementException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

import com.netflix.archaius.config.PollingDynamicConfig;

public class FixedPollingStrategyTest {

    @Test
    public void testReaderWithoutException() throws InterruptedException {
        // Arrange
        FixedPollingStrategy fixedPollingStrategy = new FixedPollingStrategy(1, TimeUnit.SECONDS);

        // Act
        PollingDynamicConfig featureSchemeConfig = new PollingDynamicConfig(new TestConfigReader(false), fixedPollingStrategy);

        // Assert
        Assert.assertEquals("testValue1", featureSchemeConfig.getString("test1.testKey1"));
        Thread.sleep(5000);
        Assert.assertEquals("testValue1", featureSchemeConfig.getString("test1.testKey1"));
    }

    @Test
    public void testReaderWithException() throws InterruptedException {
        // Arrange
        FixedPollingStrategy fixedPollingStrategy = new FixedPollingStrategy(1, TimeUnit.SECONDS);

        // Act
        PollingDynamicConfig featureSchemeConfig = new PollingDynamicConfig(new TestConfigReader(true), fixedPollingStrategy);

        // Assert
        try {
            featureSchemeConfig.getString("test1.testKey1");
            Assert.fail();
        } catch(NoSuchElementException e) {
            // expected Exception encountered
        }

        Thread.sleep(5000);

        try {
            featureSchemeConfig.getString("test1.testKey1");
            Assert.fail();
        } catch(NoSuchElementException e) {
            // expected Exception encountered
        }
    }
}

class TestConfigReader implements Callable<PollingResponse> {
    private final boolean shouldThrow;
    public TestConfigReader(boolean shouldThrow) {
        this.shouldThrow = shouldThrow;
    }

    @Override
    public PollingResponse call() throws IOException {
        if(shouldThrow) {
            throw new IOException();
        }
        Map<String, String> response = new HashMap<>();
        response.put("test1.testKey1", "testValue1");
        response.put("test1.testKey2", "testValue2");
        PollingResponse pollingResponse = PollingResponse.forSnapshot(response);
        return pollingResponse;
    }
}

