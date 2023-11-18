package com.github.moaxcp.x11client.protocol.composite;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetOverlayWindow implements TwoWayRequest<GetOverlayWindowReply>, CompositeObject {
  public static final byte OPCODE = 7;

  private int window;

  public XReplyFunction<GetOverlayWindowReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> GetOverlayWindowReply.readGetOverlayWindowReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static GetOverlayWindow readGetOverlayWindow(X11Input in) throws IOException {
    GetOverlayWindow.GetOverlayWindowBuilder javaBuilder = GetOverlayWindow.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int window = in.readCard32();
    javaBuilder.window(window);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(window);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class GetOverlayWindowBuilder {
    public int getSize() {
      return 8;
    }
  }
}
