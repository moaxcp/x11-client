package com.github.moaxcp.x11client.protocol.dri2;

import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReply;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AuthenticateReply implements XReply, Dri2Object {
  private short sequenceNumber;

  private int authenticated;

  public static AuthenticateReply readAuthenticateReply(byte pad1, short sequenceNumber,
      X11Input in) throws IOException {
    AuthenticateReply.AuthenticateReplyBuilder javaBuilder = AuthenticateReply.builder();
    int length = in.readCard32();
    int authenticated = in.readCard32();
    in.readPad(20);
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.authenticated(authenticated);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(getResponseCode());
    out.writePad(1);
    out.writeCard16(sequenceNumber);
    out.writeCard32(getLength());
    out.writeCard32(authenticated);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class AuthenticateReplyBuilder {
    public int getSize() {
      return 12;
    }
  }
}
