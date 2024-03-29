package com.github.moaxcp.x11.protocol.xinput;

import com.github.moaxcp.x11.protocol.TwoWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetDeviceMotionEvents implements TwoWayRequest<GetDeviceMotionEventsReply> {
  public static final String PLUGIN_NAME = "xinput";

  public static final byte OPCODE = 10;

  private int start;

  private int stop;

  private byte deviceId;

  public XReplyFunction<GetDeviceMotionEventsReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> GetDeviceMotionEventsReply.readGetDeviceMotionEventsReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static GetDeviceMotionEvents readGetDeviceMotionEvents(X11Input in) throws IOException {
    GetDeviceMotionEvents.GetDeviceMotionEventsBuilder javaBuilder = GetDeviceMotionEvents.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int start = in.readCard32();
    int stop = in.readCard32();
    byte deviceId = in.readCard8();
    byte[] pad6 = in.readPad(3);
    javaBuilder.start(start);
    javaBuilder.stop(stop);
    javaBuilder.deviceId(deviceId);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(start);
    out.writeCard32(stop);
    out.writeCard8(deviceId);
    out.writePad(3);
  }

  @Override
  public int getSize() {
    return 16;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class GetDeviceMotionEventsBuilder {
    public int getSize() {
      return 16;
    }
  }
}
