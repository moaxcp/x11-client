package com.github.moaxcp.x11.protocol.xkb;

import com.github.moaxcp.x11.protocol.TwoWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetNames implements TwoWayRequest<GetNamesReply> {
  public static final String PLUGIN_NAME = "xkb";

  public static final byte OPCODE = 17;

  private short deviceSpec;

  private int which;

  public XReplyFunction<GetNamesReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> GetNamesReply.readGetNamesReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static GetNames readGetNames(X11Input in) throws IOException {
    GetNames.GetNamesBuilder javaBuilder = GetNames.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    short deviceSpec = in.readCard16();
    byte[] pad4 = in.readPad(2);
    int which = in.readCard32();
    javaBuilder.deviceSpec(deviceSpec);
    javaBuilder.which(which);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard16(deviceSpec);
    out.writePad(2);
    out.writeCard32(which);
  }

  public boolean isWhichEnabled(@NonNull NameDetail... maskEnums) {
    for(NameDetail m : maskEnums) {
      if(!m.isEnabled(which)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public int getSize() {
    return 12;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class GetNamesBuilder {
    public boolean isWhichEnabled(@NonNull NameDetail... maskEnums) {
      for(NameDetail m : maskEnums) {
        if(!m.isEnabled(which)) {
          return false;
        }
      }
      return true;
    }

    public GetNames.GetNamesBuilder whichEnable(NameDetail... maskEnums) {
      for(NameDetail m : maskEnums) {
        which((int) m.enableFor(which));
      }
      return this;
    }

    public GetNames.GetNamesBuilder whichDisable(NameDetail... maskEnums) {
      for(NameDetail m : maskEnums) {
        which((int) m.disableFor(which));
      }
      return this;
    }

    public int getSize() {
      return 12;
    }
  }
}
