package com.github.moaxcp.x11client.protocol.res;

import com.github.moaxcp.x11client.protocol.X11Input;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.XObject;
import com.github.moaxcp.x11client.protocol.XReply;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class QueryClientResourcesReply implements XReply, ResObject {
  private short sequenceNumber;

  @NonNull
  private List<Type> types;

  public static QueryClientResourcesReply readQueryClientResourcesReply(byte pad1,
      short sequenceNumber, X11Input in) throws IOException {
    QueryClientResourcesReply.QueryClientResourcesReplyBuilder javaBuilder = QueryClientResourcesReply.builder();
    int length = in.readCard32();
    int numTypes = in.readCard32();
    byte[] pad5 = in.readPad(20);
    List<Type> types = new ArrayList<>((int) (Integer.toUnsignedLong(numTypes)));
    for(int i = 0; i < Integer.toUnsignedLong(numTypes); i++) {
      types.add(Type.readType(in));
    }
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.types(types);
    if(javaBuilder.getSize() < 32) {
      in.readPad(32 - javaBuilder.getSize());
    }
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(getResponseCode());
    out.writePad(1);
    out.writeCard16(sequenceNumber);
    out.writeCard32(getLength());
    int numTypes = types.size();
    out.writeCard32(numTypes);
    out.writePad(20);
    for(Type t : types) {
      t.write(out);
    }
    out.writePadAlign(getSize());
  }

  @Override
  public int getSize() {
    return 32 + XObject.sizeOf(types);
  }

  public static class QueryClientResourcesReplyBuilder {
    public int getSize() {
      return 32 + XObject.sizeOf(types);
    }
  }
}
