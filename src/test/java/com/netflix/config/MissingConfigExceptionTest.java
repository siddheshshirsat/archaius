/*
 *
 *  Copyright 2012 Netflix, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.netflix.config;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class MissingConfigExceptionTest {

    @BeforeClass
    public static void init() {
        System.setProperty(DynamicPropertyFactory.THROW_MISSING_CONFIGURATION_SOURCE_EXCEPTION, "true");
    }
    
    @Test
    public void testThrowMissingConfigurationSourceException() {
        try {
            DynamicPropertyFactory.getInstance();
            fail("MissingConfigurationSourceException expected");
        } catch (MissingConfigurationSourceException e) {
            assertNotNull(e);
        }
        DynamicPropertyFactory.setThrowMissingConfigurationSourceException(false);
        try {
            DynamicPropertyFactory.getInstance();            
        } catch (Throwable e) {
            e.printStackTrace();
            fail("unexpected exception: " + e.getMessage());
        }
    }
}
