package com.github.moaxcp.x11.protocol.xproto;

import com.github.moaxcp.x11.protocol.OneWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeleteProperty implements OneWayRequest {
  public static final String PLUGIN_NAME = "xproto";

  public static final byte OPCODE = 19;

  private int window;

  private int property;

  public byte getOpCode() {
    return OPCODE;
  }

  public static DeleteProperty readDeleteProperty(X11Input in) throws IOException {
    DeleteProperty.DeletePropertyBuilder javaBuilder = DeleteProperty.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int window = in.readCard32();
    int property = in.readCard32();
    javaBuilder.window(window);
    javaBuilder.property(property);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(window);
    out.writeCard32(property);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class DeletePropertyBuilder {
    public int getSize() {
      return 12;
    }
  }
}
