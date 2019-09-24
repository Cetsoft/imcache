package com.cetsoft.imcache.cache.util;

import org.junit.Test;

public class ArgumentUtilsTest {

  @Test(expected = NullPointerException.class)
  public void checkNotNull() {
    ArgumentUtils.checkNotNull(null, "not null");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkPositive() {
    ArgumentUtils.checkPositive(-1, "positive");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkPositiveZero() {
    ArgumentUtils.checkPositive(0, "positive");
  }

  @Test(expected = NullPointerException.class)
  public void checkNotEmpty() {
    ArgumentUtils.checkNotEmpty(null, "not null");
  }

  @Test(expected = IllegalArgumentException.class)
  public void checkNotEmptyEmptyString() {
    ArgumentUtils.checkNotEmpty("", "not null");
  }

}
