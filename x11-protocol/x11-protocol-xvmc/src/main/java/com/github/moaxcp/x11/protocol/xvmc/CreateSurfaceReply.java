package com.github.moaxcp.x11.protocol.xvmc;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReply;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class CreateSurfaceReply implements XReply {
  public static final String PLUGIN_NAME = "xvmc";

  private short sequenceNumber;

  @NonNull
  private List<Integer> privData;

  public static CreateSurfaceReply readCreateSurfaceReply(byte pad1, short sequenceNumber,
      X11Input in) throws IOException {
    CreateSurfaceReply.CreateSurfaceReplyBuilder javaBuilder = CreateSurfaceReply.builder();
    int length = in.readCard32();
    byte[] pad4 = in.readPad(24);
    List<Integer> privData = in.readCard32((int) (Integer.toUnsignedLong(length)));
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.privData(privData);
    if(javaBuilder.getSize() < 32) {
      in.readPad(32 - javaBuilder.getSize());
    }
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(getResponseCode());
    out.writePad(1);
    out.writeCard16(sequenceNumber);
    int length = privData.size();
    out.writeCard32(getLength());
    out.writePad(24);
    out.writeCard32(privData);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 32 + 4 * privData.size();
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class CreateSurfaceReplyBuilder {
    public int getSize() {
      return 32 + 4 * privData.size();
    }
  }
}
