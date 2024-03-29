package com.github.moaxcp.x11.protocol.xproto;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XEvent;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GravityNotifyEvent implements XEvent {
  public static final String PLUGIN_NAME = "xproto";

  public static final byte NUMBER = 24;

  private byte firstEventOffset;

  private boolean sentEvent;

  private short sequenceNumber;

  private int event;

  private int window;

  private short x;

  private short y;

  @Override
  public byte getResponseCode() {
    return (byte) (firstEventOffset + NUMBER);
  }

  @Override
  public byte getNumber() {
    return NUMBER;
  }

  public static GravityNotifyEvent readGravityNotifyEvent(byte firstEventOffset, boolean sentEvent,
      X11Input in) throws IOException {
    GravityNotifyEvent.GravityNotifyEventBuilder javaBuilder = GravityNotifyEvent.builder();
    byte[] pad1 = in.readPad(1);
    short sequenceNumber = in.readCard16();
    int event = in.readCard32();
    int window = in.readCard32();
    short x = in.readInt16();
    short y = in.readInt16();
    byte[] pad7 = in.readPad(16);
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.event(event);
    javaBuilder.window(window);
    javaBuilder.x(x);
    javaBuilder.y(y);

    javaBuilder.sentEvent(sentEvent);
    javaBuilder.firstEventOffset(firstEventOffset);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(sentEvent ? (byte) (0b10000000 | getResponseCode()) : getResponseCode());
    out.writePad(1);
    out.writeCard16(sequenceNumber);
    out.writeCard32(event);
    out.writeCard32(window);
    out.writeInt16(x);
    out.writeInt16(y);
    out.writePad(16);
  }

  @Override
  public int getSize() {
    return 32;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class GravityNotifyEventBuilder {
    public int getSize() {
      return 32;
    }
  }
}
