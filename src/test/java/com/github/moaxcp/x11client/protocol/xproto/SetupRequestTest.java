package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.X11InputStream;
import com.github.moaxcp.x11client.protocol.X11Output;
import com.github.moaxcp.x11client.protocol.X11OutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static com.github.moaxcp.x11client.protocol.Utilities.byteArrayToList;
import static org.assertj.core.api.Assertions.assertThat;

public class SetupRequestTest {
  ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
  X11Output out = new X11OutputStream(outBytes);

  @Test
  void writeAndRead() throws IOException {
    SetupRequest expected = SetupRequest.builder()
      .byteOrder((byte) 'B')
      .protocolMajorVersion((short) 11)
      .protocolMinorVersion((short) 0)
      .authorizationProtocolName(byteArrayToList("MIT-MAGIC-COOKIE-1".getBytes()))
      .authorizationProtocolData(byteArrayToList("secret key 123457".getBytes()))
      .build();

    expected.write(out);

    ByteArrayInputStream inBytes = new ByteArrayInputStream(outBytes.toByteArray());
    SetupRequest result = SetupRequest.readSetupRequest(new X11InputStream(inBytes));
    assertThat(result).isEqualTo(expected);
    assertThat(result.getSize()).isEqualTo(outBytes.size());
  }
}
