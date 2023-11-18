package com.github.moaxcp.x11client.protocol.randr;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetOutputProperty implements TwoWayRequest<GetOutputPropertyReply>, RandrObject {
  public static final byte OPCODE = 15;

  private int output;

  private int property;

  private int type;

  private int longOffset;

  private int longLength;

  private boolean delete;

  private boolean pending;

  public XReplyFunction<GetOutputPropertyReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> GetOutputPropertyReply.readGetOutputPropertyReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static GetOutputProperty readGetOutputProperty(X11Input in) throws IOException {
    GetOutputProperty.GetOutputPropertyBuilder javaBuilder = GetOutputProperty.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int output = in.readCard32();
    int property = in.readCard32();
    int type = in.readCard32();
    int longOffset = in.readCard32();
    int longLength = in.readCard32();
    boolean delete = in.readBool();
    boolean pending = in.readBool();
    byte[] pad10 = in.readPad(2);
    javaBuilder.output(output);
    javaBuilder.property(property);
    javaBuilder.type(type);
    javaBuilder.longOffset(longOffset);
    javaBuilder.longLength(longLength);
    javaBuilder.delete(delete);
    javaBuilder.pending(pending);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(output);
    out.writeCard32(property);
    out.writeCard32(type);
    out.writeCard32(longOffset);
    out.writeCard32(longLength);
    out.writeBool(delete);
    out.writeBool(pending);
    out.writePad(2);
  }

  @Override
  public int getSize() {
    return 28;
  }

  public static class GetOutputPropertyBuilder {
    public int getSize() {
      return 28;
    }
  }
}
