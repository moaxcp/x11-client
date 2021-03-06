package com.github.moaxcp.x11client.protocol;

import java.io.IOException;

public interface XError extends XObject, XResponse {
  default byte getResponseCode() {
    return 0;
  }
  int getSize();
  byte getCode();
  short getSequenceNumber();
  short getMinorOpcode();
  byte getMajorOpcode();
  void write(X11Output out) throws IOException;
}
