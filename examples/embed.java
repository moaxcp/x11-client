///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.github.moaxcp.x11:x11-client:+

import com.github.moaxcp.x11client.*;
import com.github.moaxcp.x11client.protocol.KeySym;
import com.github.moaxcp.x11client.protocol.Utilities;
import com.github.moaxcp.x11client.protocol.XEvent;
import com.github.moaxcp.x11client.protocol.xproto.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static com.github.moaxcp.x11client.protocol.Utilities.toList;

class embed {

  public static void main(String... args) throws Exception {
    try (X11Client client = X11Client.connect()) {
      var window = CreateWindow.builder()
          .depth(client.getDepth(0))
          .wid(client.nextResourceId())
          .parent(client.getRoot(0))
          .x((short) 10)
          .y((short) 10)
          .width((short) 900)
          .height((short) 700)
          .borderWidth((short) 0)
          .clazz(WindowClass.COPY_FROM_PARENT)
          .visual(client.getVisualId(0))
          .backgroundPixel(client.getBlackPixel(0))
          .borderPixel(client.getBlackPixel(0))
          .eventMaskEnable(EventMask.EXPOSURE, EventMask.KEY_PRESS)
          .build();
      client.send(window);

      //XStoreName sets window title
      client.send(ChangeProperty.builder()
          .window(window.getWid())
          .property(Atom.WM_NAME.getValue())
          .type(Atom.STRING.getValue())
          .format((byte) 8)
          .dataLen("embed".length())
          .data(Utilities.toByteList("embed"))
          .build());

      //XSetWMProtocols for adding delete atom
      InternAtomReply wmProtocols = client.send(InternAtom.builder().name(Utilities.toByteList("WM_PROTOCOLS")).build());
      InternAtomReply deleteAtom = client.send(InternAtom.builder().name(Utilities.toByteList("WM_DELETE_WINDOW")).build());
      client.send(ChangeProperty.builder()
          .window(window.getWid())
          .property(wmProtocols.getAtom())
          .type(Atom.ATOM.getValue())
          .format((byte) 32)
          .mode(PropMode.REPLACE)
          .data(toList(ByteBuffer.allocate(4).putInt(deleteAtom.getAtom()).array()))
          .dataLen(1)
          .build());

      client.send(MapWindow.builder()
          .window(window.getWid())
          .build());

      var color = client.send(AllocColor.builder()
          .cmap(client.getDefaultScreen().getDefaultColormap())
          .red((short) 0xffff)
          .green((short) 0xffff)
          .blue((short) 0)
          .build());

      var subWindow = CreateWindow.builder()
          .depth(client.getDepth(0))
          .wid(client.nextResourceId())
          .parent(window.getWid())
          .x((short) 10)
          .y((short) 10)
          .width((short) 100)
          .height((short) 100)
          .borderWidth((short) 0)
          .clazz(WindowClass.COPY_FROM_PARENT)
          .visual(client.getVisualId(0))
          .backgroundPixel(color.getPixel())
          .borderPixel(client.getBlackPixel(0))
          .build();
      client.send(window);
      while (true) {
        XEvent event = client.getNextEvent();
        if (event instanceof ExposeEvent) {

        } else if(event instanceof KeyPressEvent) {
          KeyPressEvent keyPress = (KeyPressEvent) event;
          KeySym keysym = client.keyCodeToKeySym(keyPress.getDetail(), keyPress.getState());
          if(keysym == KeySym.XK_Escape) {
            break;
          }
        } else if(event instanceof ClientMessageEvent) {
          ClientMessageEvent clientMessage = (ClientMessageEvent) event;
          if (clientMessage.getFormat() == 32) {
            ClientMessageData32 data = (ClientMessageData32) clientMessage.getData();
            if (data.getData32().get(0) == deleteAtom.getAtom()) {
              break;
            }
          }
        } else {
          throw new IllegalStateException(event.toString());
        }
      }
    }
  }
}