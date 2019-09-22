/**
 * Copyright © 2013 Cetsoft. All rights reserved.
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
package com.cetsoft.imcache.offheap.bytebuffer;

/**
 * The Class Pointer is a pointer to the stored elements, which keeps position of the payload and
 * related OffHeapByteBuffer. Additionally, it keeps track of expiry time for the payload.
 */
public class Pointer {

  /**
   * The position.
   */
  protected int position;

  /**
   * Expiry in long format
   */
  protected long expiry;

  /**
   * The off heap byte buffer.
   */
  protected OffHeapByteBuffer offHeapByteBuffer;

  /**
   * Instantiates a new pointer.
   *
   * @param position the position
   * @param expiry the expiry
   * @param offHeapByteBuffer the off heap byte buffer
   */
  public Pointer(final int position, final long expiry, final OffHeapByteBuffer offHeapByteBuffer) {
    this.position = position;
    this.expiry = expiry;
    this.offHeapByteBuffer = offHeapByteBuffer;
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
   * Gets the expiry time
   *
   * @return the expiry time
   */
  public long getExpiry() {
    return expiry;
  }

  /**
   * @return true if pointer is expired
   */
  public boolean isExpired() {
    return System.currentTimeMillis() > expiry;
  }

  /**
   * Gets the off heap byte buffer.
   *
   * @return the off heap byte buffer
   */
  public OffHeapByteBuffer getOffHeapByteBuffer() {
    return offHeapByteBuffer;
  }

}
