package com.github.moaxcp.x11client.protocol.glx;

import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReply;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class GetPixelMapusvReply implements XReply, GlxObject {
  private short sequenceNumber;

  private short datum;

  @NonNull
  private List<Short> data;

  public static GetPixelMapusvReply readGetPixelMapusvReply(byte pad1, short sequenceNumber,
      X11Input in) throws IOException {
    GetPixelMapusvReply.GetPixelMapusvReplyBuilder javaBuilder = GetPixelMapusvReply.builder();
    int length = in.readCard32();
    byte[] pad4 = in.readPad(4);
    int n = in.readCard32();
    short datum = in.readCard16();
    byte[] pad7 = in.readPad(16);
    List<Short> data = in.readCard16((int) (Integer.toUnsignedLong(n)));
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.datum(datum);
    javaBuilder.data(data);
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
    out.writeCard32(getLength());
    out.writePad(4);
    int n = data.size();
    out.writeCard32(n);
    out.writeCard16(datum);
    out.writePad(16);
    out.writeCard16(data);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 34 + 2 * data.size();
  }

  public static class GetPixelMapusvReplyBuilder {
    public int getSize() {
      return 34 + 2 * data.size();
    }
  }
}
