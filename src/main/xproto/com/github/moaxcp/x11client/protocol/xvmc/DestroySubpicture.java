package com.github.moaxcp.x11client.protocol.xvmc;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DestroySubpicture implements OneWayRequest, XvmcObject {
  public static final byte OPCODE = 7;

  private int subpictureId;

  public byte getOpCode() {
    return OPCODE;
  }

  public static DestroySubpicture readDestroySubpicture(X11Input in) throws IOException {
    DestroySubpicture.DestroySubpictureBuilder javaBuilder = DestroySubpicture.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int subpictureId = in.readCard32();
    javaBuilder.subpictureId(subpictureId);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(subpictureId);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class DestroySubpictureBuilder {
    public int getSize() {
      return 8;
    }
  }
}
