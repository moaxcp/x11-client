package com.github.moaxcp.x11.protocol.xproto;

import com.github.moaxcp.x11.protocol.TwoWayRequest;
import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import com.github.moaxcp.x11.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TranslateCoordinates implements TwoWayRequest<TranslateCoordinatesReply> {
  public static final String PLUGIN_NAME = "xproto";

  public static final byte OPCODE = 40;

  private int srcWindow;

  private int dstWindow;

  private short srcX;

  private short srcY;

  public XReplyFunction<TranslateCoordinatesReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> TranslateCoordinatesReply.readTranslateCoordinatesReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static TranslateCoordinates readTranslateCoordinates(X11Input in) throws IOException {
    TranslateCoordinates.TranslateCoordinatesBuilder javaBuilder = TranslateCoordinates.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int srcWindow = in.readCard32();
    int dstWindow = in.readCard32();
    short srcX = in.readInt16();
    short srcY = in.readInt16();
    javaBuilder.srcWindow(srcWindow);
    javaBuilder.dstWindow(dstWindow);
    javaBuilder.srcX(srcX);
    javaBuilder.srcY(srcY);
    return javaBuilder.build();
  }

  @Override
  public void write(byte majorOpcode, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(srcWindow);
    out.writeCard32(dstWindow);
    out.writeInt16(srcX);
    out.writeInt16(srcY);
  }

  @Override
  public int getSize() {
    return 16;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class TranslateCoordinatesBuilder {
    public int getSize() {
      return 16;
    }
  }
}
