package com.github.moaxcp.x11.protocol.xfixes;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DestroyRegion implements OneWayRequest {
  public static final String PLUGIN_NAME = "xfixes";

  public static final byte OPCODE = 10;

  private int region;

  public byte getOpCode() {
    return OPCODE;
  }

  public static DestroyRegion readDestroyRegion(X11Input in) throws IOException {
    DestroyRegion.DestroyRegionBuilder javaBuilder = DestroyRegion.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int region = in.readCard32();
    javaBuilder.region(region);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(region);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class DestroyRegionBuilder {
    public int getSize() {
      return 8;
    }
  }
}
