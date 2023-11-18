package com.github.moaxcp.x11client.protocol.xvmc;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ListSurfaceTypes implements TwoWayRequest<ListSurfaceTypesReply>, XvmcObject {
  public static final byte OPCODE = 1;

  private int portId;

  public XReplyFunction<ListSurfaceTypesReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> ListSurfaceTypesReply.readListSurfaceTypesReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static ListSurfaceTypes readListSurfaceTypes(X11Input in) throws IOException {
    ListSurfaceTypes.ListSurfaceTypesBuilder javaBuilder = ListSurfaceTypes.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int portId = in.readCard32();
    javaBuilder.portId(portId);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(portId);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class ListSurfaceTypesBuilder {
    public int getSize() {
      return 8;
    }
  }
}
