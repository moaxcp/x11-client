package com.github.moaxcp.x11.protocol.xproto;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CopyArea implements OneWayRequest {
  public static final String PLUGIN_NAME = "xproto";

  public static final byte OPCODE = 62;

  private int srcDrawable;

  private int dstDrawable;

  private int gc;

  private short srcX;

  private short srcY;

  private short dstX;

  private short dstY;

  private short width;

  private short height;

  public byte getOpCode() {
    return OPCODE;
  }

  public static CopyArea readCopyArea(X11Input in) throws IOException {
    CopyArea.CopyAreaBuilder javaBuilder = CopyArea.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int srcDrawable = in.readCard32();
    int dstDrawable = in.readCard32();
    int gc = in.readCard32();
    short srcX = in.readInt16();
    short srcY = in.readInt16();
    short dstX = in.readInt16();
    short dstY = in.readInt16();
    short width = in.readCard16();
    short height = in.readCard16();
    javaBuilder.srcDrawable(srcDrawable);
    javaBuilder.dstDrawable(dstDrawable);
    javaBuilder.gc(gc);
    javaBuilder.srcX(srcX);
    javaBuilder.srcY(srcY);
    javaBuilder.dstX(dstX);
    javaBuilder.dstY(dstY);
    javaBuilder.width(width);
    javaBuilder.height(height);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(srcDrawable);
    out.writeCard32(dstDrawable);
    out.writeCard32(gc);
    out.writeInt16(srcX);
    out.writeInt16(srcY);
    out.writeInt16(dstX);
    out.writeInt16(dstY);
    out.writeCard16(width);
    out.writeCard16(height);
  }

  @Override
  public int getSize() {
    return 28;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class CopyAreaBuilder {
    public int getSize() {
      return 28;
    }
  }
}
