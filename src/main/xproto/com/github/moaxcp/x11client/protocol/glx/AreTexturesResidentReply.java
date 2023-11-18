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
public class AreTexturesResidentReply implements XReply, GlxObject {
  private short sequenceNumber;

  private int retVal;

  @NonNull
  private List<Boolean> data;

  public static AreTexturesResidentReply readAreTexturesResidentReply(byte pad1,
      short sequenceNumber, X11Input in) throws IOException {
    AreTexturesResidentReply.AreTexturesResidentReplyBuilder javaBuilder = AreTexturesResidentReply.builder();
    int length = in.readCard32();
    int retVal = in.readCard32();
    byte[] pad5 = in.readPad(20);
    List<Boolean> data = in.readBool((int) (Integer.toUnsignedLong(length) * 4));
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.retVal(retVal);
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
    int length = data.size();
    out.writeCard32(getLength());
    out.writeCard32(retVal);
    out.writePad(20);
    out.writeBool(data);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 32 + 1 * data.size();
  }

  public static class AreTexturesResidentReplyBuilder {
    public int getSize() {
      return 32 + 1 * data.size();
    }
  }
}
