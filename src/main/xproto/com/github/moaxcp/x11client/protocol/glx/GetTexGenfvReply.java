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
public class GetTexGenfvReply implements XReply, GlxObject {
  private short sequenceNumber;

  private float datum;

  @NonNull
  private List<Float> data;

  public static GetTexGenfvReply readGetTexGenfvReply(byte pad1, short sequenceNumber, X11Input in)
      throws IOException {
    GetTexGenfvReply.GetTexGenfvReplyBuilder javaBuilder = GetTexGenfvReply.builder();
    int length = in.readCard32();
    byte[] pad4 = in.readPad(4);
    int n = in.readCard32();
    float datum = in.readFloat();
    byte[] pad7 = in.readPad(12);
    List<Float> data = in.readFloat((int) (Integer.toUnsignedLong(n)));
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
    out.writeFloat(datum);
    out.writePad(12);
    out.writeFloat(data);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 32 + 4 * data.size();
  }

  public static class GetTexGenfvReplyBuilder {
    public int getSize() {
      return 32 + 4 * data.size();
    }
  }
}
