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
* Date   : Sep 26, 2013
*/
package com.cetsoft.imcache.cache;

/**
 * The Interface VersionedItem provides versioning for the
 * cached items.
 *
 * @param <V> the value type
 */
public interface VersionedItem<V> extends CacheItem<V> {

	/**
	 * Gets the version.
	 *
	 * @return the version
	 */
	int getVersion();

	/**
	 * Sets the version.
	 *
	 * @param version the version
	 * @return the int
	 */
	int setVersion(int version);

	/**
	 * Updates the current value.
	 *
	 * @param value the value
	 */
	void update(V value);
}
