package com.github.moaxcp.x11client.protocol.composite;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReleaseOverlayWindow implements OneWayRequest, CompositeObject {
  public static final byte OPCODE = 8;

  private int window;

  public byte getOpCode() {
    return OPCODE;
  }

  public static ReleaseOverlayWindow readReleaseOverlayWindow(X11Input in) throws IOException {
    ReleaseOverlayWindow.ReleaseOverlayWindowBuilder javaBuilder = ReleaseOverlayWindow.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int window = in.readCard32();
    javaBuilder.window(window);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(window);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class ReleaseOverlayWindowBuilder {
    public int getSize() {
      return 8;
    }
  }
}
