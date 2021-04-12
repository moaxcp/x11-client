package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.X11Output;
import lombok.NonNull;
import lombok.Value;

import java.io.IOException;
import java.util.List;

@Value
public class ClientMessageData8 implements ClientMessageDataUnion {
  List<Byte> data8;
  byte format = 8;

  public ClientMessageData8(@NonNull List<Byte> data8) {
    if(data8.size() != 20) {
      throw new IllegalArgumentException("data8 must have length of 20. Got: \"" + data8.size() + "\"");
    }
    this.data8 = data8;
  }

  @Override
  public int getSize() {
    return 20;
  }

  public void write(X11Output out) throws IOException {
    out.writeCard8(data8);
  }
}
