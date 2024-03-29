package com.github.moaxcp.x11.protocol.xinput;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XStruct;
import com.github.moaxcp.x11.protocol.xproto.GrabStatus;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GrabModifierInfo implements XStruct {
  public static final String PLUGIN_NAME = "xinput";

  private int modifiers;

  private byte status;

  public static GrabModifierInfo readGrabModifierInfo(X11Input in) throws IOException {
    GrabModifierInfo.GrabModifierInfoBuilder javaBuilder = GrabModifierInfo.builder();
    int modifiers = in.readCard32();
    byte status = in.readCard8();
    byte[] pad2 = in.readPad(3);
    javaBuilder.modifiers(modifiers);
    javaBuilder.status(status);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard32(modifiers);
    out.writeCard8(status);
    out.writePad(3);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class GrabModifierInfoBuilder {
    public GrabModifierInfo.GrabModifierInfoBuilder status(GrabStatus status) {
      this.status = (byte) status.getValue();
      return this;
    }

    public GrabModifierInfo.GrabModifierInfoBuilder status(byte status) {
      this.status = status;
      return this;
    }

    public int getSize() {
      return 8;
    }
  }
}
