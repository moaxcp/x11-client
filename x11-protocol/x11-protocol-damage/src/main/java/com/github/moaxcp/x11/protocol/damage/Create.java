package com.github.moaxcp.x11.protocol.damage;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Create implements OneWayRequest {
  public static final String PLUGIN_NAME = "damage";

  public static final byte OPCODE = 1;

  private int damage;

  private int drawable;

  private byte level;

  public byte getOpCode() {
    return OPCODE;
  }

  public static Create readCreate(X11Input in) throws IOException {
    Create.CreateBuilder javaBuilder = Create.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int damage = in.readCard32();
    int drawable = in.readCard32();
    byte level = in.readCard8();
    byte[] pad6 = in.readPad(3);
    javaBuilder.damage(damage);
    javaBuilder.drawable(drawable);
    javaBuilder.level(level);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(damage);
    out.writeCard32(drawable);
    out.writeCard8(level);
    out.writePad(3);
  }

  @Override
  public int getSize() {
    return 16;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class CreateBuilder {
    public Create.CreateBuilder level(ReportLevel level) {
      this.level = (byte) level.getValue();
      return this;
    }

    public Create.CreateBuilder level(byte level) {
      this.level = level;
      return this;
    }

    public int getSize() {
      return 16;
    }
  }
}
