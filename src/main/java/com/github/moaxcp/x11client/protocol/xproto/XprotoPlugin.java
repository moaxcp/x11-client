package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.*;
import lombok.Getter;

import java.io.IOException;
import lombok.Setter;

public class XprotoPlugin implements XProtocolPlugin {
  @Getter
  private final String name = "xproto";
  @Getter
  @Setter
  private byte offset;

  @Override
  public boolean supportedRequest(XRequest request) {
    if(request instanceof GetKeyboardMapping) {
      return true;
    }
    if(request instanceof QueryExtension) {
      return true;
    }
    if(request instanceof CreateWindow) {
      return true;
    }
    if(request instanceof CreateGC) {
      return true;
    }
    if(request instanceof MapWindow) {
      return true;
    }
    if(request instanceof PolyFillRectangle) {
      return true;
    }
    switch(request.getOpCode()) {
      case 76:
        return true;
    }
    return false;
  }

  @Override
  public boolean supportedEvent(byte number) {
    if(number < 0) {
      throw new IllegalArgumentException("number must be positive");
    }
    if(Byte.toUnsignedInt(number) < 64) {
      return true;
    }
    return false;
  }

  @Override
  public boolean supportedError(byte code) {
    if(Byte.toUnsignedInt(code) < 128) {
      return true;
    }
    return false;
  }

  @Override
  public XEvent readEvent(byte number, boolean sentEvent, X11Input in) throws IOException {
    switch(number) {
      case 2:
        return KeyPressEvent.readKeyPressEvent(sentEvent, in);
      case 12:
        return ExposeEvent.readExposeEvent(sentEvent, in);
      case 26:
        return CirculateNotifyEvent.readCirculateNotifyEvent(sentEvent, in);
      default:
        throw new IllegalArgumentException("number " + number + " is not supported");
    }
  }

  @Override
  public XError readError(byte code, X11Input in) throws IOException {
    switch(code) {
      case 1:
        return RequestError.readRequestError(in);
      case 2:
        return ValueError.readValueError(in);
      case 3:
        return WindowError.readWindowError(in);
      case 5:
        return AtomError.readAtomError(in);
      case 13:
        return GContextError.readGContextError(in);
      case 16:
        return LengthError.readLengthError(in);
      default:
        throw new IllegalArgumentException("code " + code + " not supported by " + name);
    }
  }
}
