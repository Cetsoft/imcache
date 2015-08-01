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
* Author : Christian Bourque
* Date   : August 1, 2015
*/
package com.cetsoft.imcache.cache.coordinator;

import com.cetsoft.imcache.cache.Cache;

/**
 * The singleton cache coordinator is an extension of the global cache
 * coordinator. As the name indicates it's a singleton that can be handy
 * when you need a single cache coordinator for a whole application which can
 * be accessible from anywhere.
 */
public final class SingletonCacheCoordinator extends GlobalCacheCoordinator {

        private volatile static SingletonCacheCoordinator instance;
    
        private SingletonCacheCoordinator() {}
    
        /**
         * Get sole instance of this class.
         * 
         * @return the singleton instance 
         */
        public static SingletonCacheCoordinator getInstance() {
                if (instance == null) {
                    synchronized (SingletonCacheCoordinator.class) {
                        if (instance == null) {
                            instance = new SingletonCacheCoordinator();
                        }
                    }
                }
                return instance;
        }

        /**
         * Clear all caches.
         */
        @SuppressWarnings("rawtypes")
		public synchronized void clearAll() {
                for (Cache cache : cacheMap.values()) {
                    cache.clear();
                }
        }
    
}
