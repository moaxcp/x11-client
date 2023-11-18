package com.github.moaxcp.x11client.protocol.glx;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IsEnabled implements TwoWayRequest<IsEnabledReply>, GlxObject {
  public static final byte OPCODE = (byte) 140;

  private int contextTag;

  private int capability;

  public XReplyFunction<IsEnabledReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> IsEnabledReply.readIsEnabledReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static IsEnabled readIsEnabled(X11Input in) throws IOException {
    IsEnabled.IsEnabledBuilder javaBuilder = IsEnabled.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int contextTag = in.readCard32();
    int capability = in.readCard32();
    javaBuilder.contextTag(contextTag);
    javaBuilder.capability(capability);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(contextTag);
    out.writeCard32(capability);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class IsEnabledBuilder {
    public int getSize() {
      return 12;
    }
  }
}
