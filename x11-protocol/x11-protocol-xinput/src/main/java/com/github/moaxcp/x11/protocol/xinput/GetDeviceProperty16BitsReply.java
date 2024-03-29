package com.github.moaxcp.x11.protocol.xinput;

import com.github.moaxcp.x11.protocol.X11Input;
import com.github.moaxcp.x11.protocol.X11Output;
import java.io.IOException;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class GetDeviceProperty16BitsReply implements GetDevicePropertyReply {
  public static final String PLUGIN_NAME = "xinput";

  private byte xiReplyType;

  private short sequenceNumber;

  private int type;

  private int bytesAfter;

  private int numItems;

  private byte format;

  private byte deviceId;

  public static GetDeviceProperty16BitsReply readGetDeviceProperty16BitsReply(byte RESPONSECODE,
      byte xiReplyType, short sequenceNumber, int length, int type, int bytesAfter, int numItems,
      byte format, byte deviceId, byte[] pad9, X11Input in) throws IOException {
    GetDeviceProperty16BitsReply.GetDeviceProperty16BitsReplyBuilder javaBuilder = GetDeviceProperty16BitsReply.builder();
    javaBuilder.xiReplyType(xiReplyType);
    javaBuilder.sequenceNumber(sequenceNumber);
    javaBuilder.type(type);
    javaBuilder.bytesAfter(bytesAfter);
    javaBuilder.numItems(numItems);
    javaBuilder.format(format);
    javaBuilder.deviceId(deviceId);
    return javaBuilder.build();
  }

  @Override
  public void write(X11Output out) throws IOException {
    out.writeCard8(getResponseCode());
    out.writeCard8(xiReplyType);
    out.writeCard16(sequenceNumber);
    out.writeCard32(getLength());
    out.writeCard32(type);
    out.writeCard32(bytesAfter);
    out.writeCard32(numItems);
    out.writeCard8(format);
    out.writeCard8(deviceId);
    out.writePad(10);
  }

  @Override
  public int getSize() {
    return 32;
  }

  public String getPluginName() {
    return PLUGIN_NAME;
  }

  public static class GetDeviceProperty16BitsReplyBuilder {
    public GetDeviceProperty16BitsReply.GetDeviceProperty16BitsReplyBuilder format(
        PropertyFormat format) {
      this.format = (byte) format.getValue();
      return this;
    }

    public GetDeviceProperty16BitsReply.GetDeviceProperty16BitsReplyBuilder format(byte format) {
      this.format = format;
      return this;
    }

    public int getSize() {
      return 32;
    }
  }
}
