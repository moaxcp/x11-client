package com.github.moaxcp.x11.protocol.xinput;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InputInfoKey implements InputInfo {
  public static final String PLUGIN_NAME = "xinput";

  private byte classId;

  private byte len;

  private byte minKeycode;

  private byte maxKeycode;

  private short numKeys;

  public static InputInfoKey readInputInfoKey(byte classId, byte len, X11Input in) throws
      IOException {
    InputInfoKey.InputInfoKeyBuilder javaBuilder = InputInfoKey.builder();
    byte minKeycode = in.readCard8();
    byte maxKeycode = in.readCard8();
    short numKeys = in.readCard16();
    byte[] pad5 = in.readPad(2);
    javaBuilder.classId(classId);
    javaBuilder.len(len);
    javaBuilder.minKeycode(minKeycode);
    javaBuilder.maxKeycode(maxKeycode);
    javaBuilder.numKeys(numKeys);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(classId);
    out.writeCard8(len);
    out.writeCard8(minKeycode);
    out.writeCard8(maxKeycode);
    out.writeCard16(numKeys);
    out.writePad(2);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class InputInfoKeyBuilder {
    public InputInfoKey.InputInfoKeyBuilder classId(InputClass classId) {
      this.classId = (byte) classId.getValue();
      return this;
    }

    public InputInfoKey.InputInfoKeyBuilder classId(byte classId) {
      this.classId = classId;
      return this;
    }

    public int getSize() {
      return 8;
    }
  }
}
