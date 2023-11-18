package com.github.moaxcp.x11client.protocol.xtest;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CompareCursor implements TwoWayRequest<CompareCursorReply>, XtestObject {
  public static final byte OPCODE = 1;

  private int window;

  private int cursor;

  public XReplyFunction<CompareCursorReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> CompareCursorReply.readCompareCursorReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static CompareCursor readCompareCursor(X11Input in) throws IOException {
    CompareCursor.CompareCursorBuilder javaBuilder = CompareCursor.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int window = in.readCard32();
    int cursor = in.readCard32();
    javaBuilder.window(window);
    javaBuilder.cursor(cursor);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(window);
    out.writeCard32(cursor);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class CompareCursorBuilder {
    public int getSize() {
      return 12;
    }
  }
}
