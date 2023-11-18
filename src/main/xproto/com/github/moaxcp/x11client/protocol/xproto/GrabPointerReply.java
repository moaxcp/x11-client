package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReply;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GrabPointerReply implements XReply, XprotoObject {
  private byte status;

  private short sequenceNumber;

  public static GrabPointerReply readGrabPointerReply(byte status, short sequenceNumber,
      X11Input in) throws IOException {
    GrabPointerReply.GrabPointerReplyBuilder javaBuilder = GrabPointerReply.builder();
    int length = in.readCard32();
    in.readPad(24);
    javaBuilder.status(status);
    javaBuilder.sequenceNumber(sequenceNumber);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(getResponseCode());
    out.writeByte(status);
    out.writeCard16(sequenceNumber);
    out.writeCard32(getLength());
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class GrabPointerReplyBuilder {
    public GrabPointerReply.GrabPointerReplyBuilder status(GrabStatus status) {
      this.status = (byte) status.getValue();
      return this;
    }

    public GrabPointerReply.GrabPointerReplyBuilder status(byte status) {
      this.status = status;
      return this;
    }

    public int getSize() {
      return 8;
    }
  }
}
