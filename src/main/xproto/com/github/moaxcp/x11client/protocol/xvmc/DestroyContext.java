package com.github.moaxcp.x11client.protocol.xvmc;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DestroyContext implements OneWayRequest, XvmcObject {
  public static final byte OPCODE = 3;

  private int contextId;

  public byte getOpCode() {
    return OPCODE;
  }

  public static DestroyContext readDestroyContext(X11Input in) throws IOException {
    DestroyContext.DestroyContextBuilder javaBuilder = DestroyContext.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int contextId = in.readCard32();
    javaBuilder.contextId(contextId);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(contextId);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class DestroyContextBuilder {
    public int getSize() {
      return 8;
    }
  }
}
