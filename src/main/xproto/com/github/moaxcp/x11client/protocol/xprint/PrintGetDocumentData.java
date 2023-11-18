package com.github.moaxcp.x11client.protocol.xprint;

import com.github.moaxcp.x11client.protocol.TwoWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XReplyFunction;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PrintGetDocumentData implements TwoWayRequest<PrintGetDocumentDataReply>, XprintObject {
  public static final byte OPCODE = 12;

  private int context;

  private int maxBytes;

  public XReplyFunction<PrintGetDocumentDataReply> getReplyFunction() {
    return (field, sequenceNumber, in) -> PrintGetDocumentDataReply.readPrintGetDocumentDataReply(field, sequenceNumber, in);
  }

  public byte getOpCode() {
    return OPCODE;
  }

  public static PrintGetDocumentData readPrintGetDocumentData(X11Input in) throws IOException {
    PrintGetDocumentData.PrintGetDocumentDataBuilder javaBuilder = PrintGetDocumentData.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int context = in.readCard32();
    int maxBytes = in.readCard32();
    javaBuilder.context(context);
    javaBuilder.maxBytes(maxBytes);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(context);
    out.writeCard32(maxBytes);
  }

  @Override
  public int getSize() {
    return 12;
  }

  public static class PrintGetDocumentDataBuilder {
    public int getSize() {
      return 12;
    }
  }
}
