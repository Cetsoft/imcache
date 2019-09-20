/*
 * Copyright (C) 2015 Cetsoft, http://www.cetsoft.com
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
 * 
 * Author : Yusuf Aytas
 * Date   : Sep 15, 2013
 */
package com.cetsoft.imcache.cache;

/**
 * The listener interface for receiving eviction events. The class that is
 * interested in processing a eviction event implements this interface, when the
 * eviction event occurs, that object's onEviction method is invoked.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
@FunctionalInterface
public interface EvictionListener<K, V> {
    
    /**
     * Called on eviction.
     * @param key the key
     * @param value the value
     */
    void onEviction(K key, V value);
}
