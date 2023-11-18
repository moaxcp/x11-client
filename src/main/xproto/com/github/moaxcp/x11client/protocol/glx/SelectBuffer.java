package com.github.moaxcp.x11client.protocol.glx;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SelectBuffer implements OneWayRequest, GlxObject {
  public static final byte OPCODE = 106;

  private int contextTag;

  private int size;

  public byte getOpCode() {
    return OPCODE;
  }

  public static SelectBuffer readSelectBuffer(X11Input in) throws IOException {
    SelectBuffer.SelectBufferBuilder javaBuilder = SelectBuffer.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int contextTag = in.readCard32();
    int size = in.readInt32();
    javaBuilder.contextTag(contextTag);
    javaBuilder.size(size);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(contextTag);
    out.writeInt32(size);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class SelectBufferBuilder {
    public int getSize() {
      return 12;
    }
  }
}
