package com.github.moaxcp.x11client.protocol.xkb;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetState implements TwoWayRequest<GetStateReply>, XkbObject {
  public static final byte OPCODE = 4;

  private short deviceSpec;

  public XReplyFunction<GetStateReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> GetStateReply.readGetStateReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static GetState readGetState(X11Input in) throws IOException {
    GetState.GetStateBuilder javaBuilder = GetState.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    short deviceSpec = in.readCard16();
    byte[] pad4 = in.readPad(2);
    javaBuilder.deviceSpec(deviceSpec);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard16(deviceSpec);
    out.writePad(2);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class GetStateBuilder {
    public int getSize() {
      return 8;
    }
  }
}
