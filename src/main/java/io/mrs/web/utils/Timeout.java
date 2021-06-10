package io.mrs.web.utils;

public enum Timeout {
  POLLING(5),
  PAGE_TIMEOUT(60);

  private final int timeout;

  Timeout(int timeout) {
    this.timeout = timeout;
  }

  public int getValue() {
    return this.timeout;
  }

}
