package com.github.moaxcp.x11client.protocol.composite;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateRegionFromBorderClip implements OneWayRequest, CompositeObject {
  public static final byte OPCODE = 5;

  private int region;

  private int window;

  public byte getOpCode() {
    return OPCODE;
  }

  public static CreateRegionFromBorderClip readCreateRegionFromBorderClip(X11Input in) throws
      IOException {
    CreateRegionFromBorderClip.CreateRegionFromBorderClipBuilder javaBuilder = CreateRegionFromBorderClip.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int region = in.readCard32();
    int window = in.readCard32();
    javaBuilder.region(region);
    javaBuilder.window(window);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(region);
    out.writeCard32(window);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class CreateRegionFromBorderClipBuilder {
    public int getSize() {
      return 12;
    }
  }
}
