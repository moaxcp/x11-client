package com.github.moaxcp.x11client.protocol.xfixes;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class SetCursorName implements OneWayRequest, XfixesObject {
  public static final byte OPCODE = 23;

  private int cursor;

  @NonNull
  private List<Byte> name;

  public byte getOpCode() {
    return OPCODE;
  }

  public static SetCursorName readSetCursorName(X11Input in) throws IOException {
    SetCursorName.SetCursorNameBuilder javaBuilder = SetCursorName.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int cursor = in.readCard32();
    short nbytes = in.readCard16();
    byte[] pad5 = in.readPad(2);
    List<Byte> name = in.readChar(Short.toUnsignedInt(nbytes));
    javaBuilder.cursor(cursor);
    javaBuilder.name(name);
    in.readPadAlign(javaBuilder.getSize());
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(cursor);
    short nbytes = (short) name.size();
    out.writeCard16(nbytes);
    out.writePad(2);
    out.writeChar(name);
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 12 + 1 * name.size();
  }

  public static class SetCursorNameBuilder {
    public int getSize() {
      return 12 + 1 * name.size();
    }
  }
}
