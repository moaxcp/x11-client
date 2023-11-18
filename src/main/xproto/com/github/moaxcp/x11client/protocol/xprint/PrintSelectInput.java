package com.github.moaxcp.x11client.protocol.xprint;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PrintSelectInput implements OneWayRequest, XprintObject {
  public static final byte OPCODE = 15;

  private int context;

  private int eventMask;

  public byte getOpCode() {
    return OPCODE;
  }

  public static PrintSelectInput readPrintSelectInput(X11Input in) throws IOException {
    PrintSelectInput.PrintSelectInputBuilder javaBuilder = PrintSelectInput.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int context = in.readCard32();
    int eventMask = in.readCard32();
    javaBuilder.context(context);
    javaBuilder.eventMask(eventMask);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(context);
    out.writeCard32(eventMask);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class PrintSelectInputBuilder {
    public int getSize() {
      return 12;
    }
  }
}
