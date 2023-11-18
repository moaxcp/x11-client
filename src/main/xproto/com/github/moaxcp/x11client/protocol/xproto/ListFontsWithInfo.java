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
public class ListFontsWithInfo implements TwoWayRequest<ListFontsWithInfoReply>, XprotoObject {
  public static final byte OPCODE = 50;

  private short maxNames;

  @NonNull
  private List<Byte> pattern;

  public XReplyFunction<ListFontsWithInfoReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> ListFontsWithInfoReply.readListFontsWithInfoReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static ListFontsWithInfo readListFontsWithInfo(X11Input in) throws IOException {
    ListFontsWithInfo.ListFontsWithInfoBuilder javaBuilder = ListFontsWithInfo.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    short maxNames = in.readCard16();
    short patternLen = in.readCard16();
    List<Byte> pattern = in.readChar(Short.toUnsignedInt(patternLen));
    javaBuilder.maxNames(maxNames);
    javaBuilder.pattern(pattern);
    in.readPadAlign(javaBuilder.getSize());
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard16(maxNames);
    short patternLen = (short) pattern.size();
    out.writeCard16(patternLen);
    out.writeChar(pattern);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 8 + 1 * pattern.size();
  }

  public static class ListFontsWithInfoBuilder {
    public int getSize() {
      return 8 + 1 * pattern.size();
    }
  }
}
