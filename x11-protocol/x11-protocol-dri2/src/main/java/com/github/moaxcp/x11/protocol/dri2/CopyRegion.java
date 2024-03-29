package com.github.moaxcp.x11.protocol.dri2;

import com.github.moaxcp.x11.protocol.TwoWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CopyRegion implements TwoWayRequest<CopyRegionReply> {
  public static final String PLUGIN_NAME = "dri2";

  public static final byte OPCODE = 6;

  private int drawable;

  private int region;

  private int dest;

  private int src;

  public XReplyFunction<CopyRegionReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> CopyRegionReply.readCopyRegionReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static CopyRegion readCopyRegion(X11Input in) throws IOException {
    CopyRegion.CopyRegionBuilder javaBuilder = CopyRegion.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int drawable = in.readCard32();
    int region = in.readCard32();
    int dest = in.readCard32();
    int src = in.readCard32();
    javaBuilder.drawable(drawable);
    javaBuilder.region(region);
    javaBuilder.dest(dest);
    javaBuilder.src(src);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(drawable);
    out.writeCard32(region);
    out.writeCard32(dest);
    out.writeCard32(src);
  }

  @Override
  public int getSize() {
    return 20;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class CopyRegionBuilder {
    public int getSize() {
      return 20;
    }
  }
}
