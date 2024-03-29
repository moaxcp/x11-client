package com.github.moaxcp.x11.protocol.dri3;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PixmapFromBuffer implements OneWayRequest {
  public static final String PLUGIN_NAME = "dri3";

  public static final byte OPCODE = 2;

  private int pixmap;

  private int drawable;

  private int size;

  private short width;

  private short height;

  private short stride;

  private byte depth;

  private byte bpp;

  private int pixmapFd;

  public byte getOpCode() {
    return OPCODE;
  }

  public static PixmapFromBuffer readPixmapFromBuffer(X11Input in) throws IOException {
    PixmapFromBuffer.PixmapFromBufferBuilder javaBuilder = PixmapFromBuffer.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int pixmap = in.readCard32();
    int drawable = in.readCard32();
    int size = in.readCard32();
    short width = in.readCard16();
    short height = in.readCard16();
    short stride = in.readCard16();
    byte depth = in.readCard8();
    byte bpp = in.readCard8();
    int pixmapFd = in.readInt32();
    javaBuilder.pixmap(pixmap);
    javaBuilder.drawable(drawable);
    javaBuilder.size(size);
    javaBuilder.width(width);
    javaBuilder.height(height);
    javaBuilder.stride(stride);
    javaBuilder.depth(depth);
    javaBuilder.bpp(bpp);
    javaBuilder.pixmapFd(pixmapFd);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(pixmap);
    out.writeCard32(drawable);
    out.writeCard32(size);
    out.writeCard16(width);
    out.writeCard16(height);
    out.writeCard16(stride);
    out.writeCard8(depth);
    out.writeCard8(bpp);
    out.writeInt32(pixmapFd);
  }

  @Override
  public int getSize() {
    return 28;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class PixmapFromBufferBuilder {
    public int getSize() {
      return 28;
    }
  }
}
