package com.github.moaxcp.x11client.protocol.sync;

import com.github.moaxcp.x11client.protocol.OneWayRequest;
import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DestroyCounter implements OneWayRequest, SyncObject {
  public static final byte OPCODE = 6;

  private int counter;

  public byte getOpCode() {
    return OPCODE;
  }

  public static DestroyCounter readDestroyCounter(X11Input in) throws IOException {
    DestroyCounter.DestroyCounterBuilder javaBuilder = DestroyCounter.builder();
    byte[] pad1 = in.readPad(1);
    short length = in.readCard16();
    int counter = in.readCard32();
    javaBuilder.counter(counter);
    return javaBuilder.build();
  }

  @Override
  public void write(byte offset, X11Output out) throws IOException {
    out.writeCard8((byte)(Byte.toUnsignedInt(OPCODE) + Byte.toUnsignedInt(offset)));
    out.writePad(1);
    out.writeCard16((short) getLength());
    out.writeCard32(counter);
  }

  @Override
  public int getSize() {
    return 8;
  }

  public static class DestroyCounterBuilder {
    public int getSize() {
      return 8;
    }
  }
}
