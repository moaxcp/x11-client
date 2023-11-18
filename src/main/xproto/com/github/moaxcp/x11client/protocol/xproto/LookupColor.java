package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class LookupColor implements TwoWayRequest<LookupColorReply>, XprotoObject {
  public static final byte OPCODE = 92;

  private int cmap;

  @NonNull
  private List<Byte> name;

  public XReplyFunction<LookupColorReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> LookupColorReply.readLookupColorReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static LookupColor readLookupColor(X11Input in) throws IOException {
    LookupColor.LookupColorBuilder javaBuilder = LookupColor.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int cmap = in.readCard32();
    short nameLen = in.readCard16();
    byte[] pad5 = in.readPad(2);
    List<Byte> name = in.readChar(Short.toUnsignedInt(nameLen));
    javaBuilder.cmap(cmap);
    javaBuilder.name(name);
    in.readPadAlign(javaBuilder.getSize());
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(cmap);
    short nameLen = (short) name.size();
    out.writeCard16(nameLen);
    out.writePad(2);
    out.writeChar(name);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 12 + 1 * name.size();
  }

  public static class LookupColorBuilder {
    public int getSize() {
      return 12 + 1 * name.size();
    }
  }
}
