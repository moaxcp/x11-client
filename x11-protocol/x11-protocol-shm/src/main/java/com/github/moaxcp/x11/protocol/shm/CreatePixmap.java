package com.github.moaxcp.x11.protocol.shm;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreatePixmap implements OneWayRequest {
  public static final String PLUGIN_NAME = "shm";

  public static final byte OPCODE = 5;

  private int pid;

  private int drawable;

  private short width;

  private short height;

  private byte depth;

  private int shmseg;

  private int offset;

  public byte getOpCode() {
    return OPCODE;
  }

  public static CreatePixmap readCreatePixmap(X11Input in) throws IOException {
    CreatePixmap.CreatePixmapBuilder javaBuilder = CreatePixmap.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int pid = in.readCard32();
    int drawable = in.readCard32();
    short width = in.readCard16();
    short height = in.readCard16();
    byte depth = in.readCard8();
    byte[] pad8 = in.readPad(3);
    int shmseg = in.readCard32();
    int offset = in.readCard32();
    javaBuilder.pid(pid);
    javaBuilder.drawable(drawable);
    javaBuilder.width(width);
    javaBuilder.height(height);
    javaBuilder.depth(depth);
    javaBuilder.shmseg(shmseg);
    javaBuilder.offset(offset);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(pid);
    out.writeCard32(drawable);
    out.writeCard16(width);
    out.writeCard16(height);
    out.writeCard8(depth);
    out.writePad(3);
    out.writeCard32(shmseg);
    out.writeCard32(offset);
  }

  @Override
  public int getSize() {
    return 28;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class CreatePixmapBuilder {
    public int getSize() {
      return 28;
    }
  }
}
