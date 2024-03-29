package com.github.moaxcp.x11.protocol.xkb;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XEvent;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IndicatorStateNotifyEvent implements XEvent {
  public static final String PLUGIN_NAME = "xkb";

  public static final byte NUMBER = 4;

  private byte firstEventOffset;

  private boolean sentEvent;

  private byte xkbType;

  private short sequenceNumber;

  private int time;

  private byte deviceID;

  private int state;

  private int stateChanged;

  @Override
  public byte getResponseCode() {
    return (byte) (firstEventOffset + NUMBER);
  }

  @Override
  public byte getNumber() {
    return NUMBER;
  }

  public static IndicatorStateNotifyEvent readIndicatorStateNotifyEvent(byte firstEventOffset,
      boolean sentEvent, X11Input in) throws IOException {
    IndicatorStateNotifyEvent.IndicatorStateNotifyEventBuilder javaBuilder = IndicatorStateNotifyEvent.builder();
    byte xkbType = in.readCard8();
    short sequenceNumber = in.readCard16();
    int time = in.readCard32();
    byte deviceID = in.readCard8();
    byte[] pad5 = in.readPad(3);
    int state = in.readCard32();
    int stateChanged = in.readCard32();
    byte[] pad8 = in.readPad(12);
    javaBuilder.xkbType(xkbType);
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.time(time);
    javaBuilder.deviceID(deviceID);
    javaBuilder.state(state);
    javaBuilder.stateChanged(stateChanged);

    javaBuilder.sentEvent(sentEvent);
    javaBuilder.firstEventOffset(firstEventOffset);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(sentEvent ? (byte) (0b10000000 | getResponseCode()) : getResponseCode());
    out.writeCard8(xkbType);
    out.writeCard16(sequenceNumber);
    out.writeCard32(time);
    out.writeCard8(deviceID);
    out.writePad(3);
    out.writeCard32(state);
    out.writeCard32(stateChanged);
    out.writePad(12);
  }

  @Override
  public int getSize() {
    return 32;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class IndicatorStateNotifyEventBuilder {
    public int getSize() {
      return 32;
    }
  }
}
