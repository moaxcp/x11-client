package com.github.moaxcp.x11.protocol.glx;

import com.github.moaxcp.x11.protocol.TwoWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QueryServerString implements TwoWayRequest<QueryServerStringReply> {
  public static final String PLUGIN_NAME = "glx";

  public static final byte OPCODE = 19;

  private int screen;

  private int name;

  public XReplyFunction<QueryServerStringReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> QueryServerStringReply.readQueryServerStringReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static QueryServerString readQueryServerString(X11Input in) throws IOException {
    QueryServerString.QueryServerStringBuilder javaBuilder = QueryServerString.builder();
    byte majorOpcode = in.readCard8();
    short length = in.readCard16();
    int screen = in.readCard32();
    int name = in.readCard32();
    javaBuilder.screen(screen);
    javaBuilder.name(name);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8(majorOpcode);
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writeCard16((short) getLength());
    out.writeCard32(screen);
    out.writeCard32(name);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class QueryServerStringBuilder {
    public int getSize() {
      return 12;
    }
  }
}
