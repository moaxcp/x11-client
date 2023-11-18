package com.github.moaxcp.x11client.protocol.xv;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class QueryExtension implements TwoWayRequest<QueryExtensionReply>, XvObject {
  public static final byte OPCODE = 0;

  public XReplyFunction<QueryExtensionReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> QueryExtensionReply.readQueryExtensionReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static QueryExtension readQueryExtension(X11Input in) throws IOException {
    QueryExtension.QueryExtensionBuilder javaBuilder = QueryExtension.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
  }

  @Override
  public int getSize() {
    return 4;
  }

  public static class QueryExtensionBuilder {
    public int getSize() {
      return 4;
    }
  }
}
