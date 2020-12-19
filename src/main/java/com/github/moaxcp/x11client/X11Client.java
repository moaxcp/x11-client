package com.github.moaxcp.x11client;

import com.github.moaxcp.x11client.protocol.*;
import com.github.moaxcp.x11client.protocol.xproto.Screen;
import com.github.moaxcp.x11client.protocol.xproto.Setup;
import java.io.IOException;
import lombok.NonNull;

/**
 * An x11 client.
 */
public class X11Client implements AutoCloseable {
  private final X11Connection connection;
  private final XProtocolService service;
  private int nextResourceId;

  /**
   * Creates a client for the given displayName and authority.
   * @param displayName to connect to. non-null
   * @param xAuthority to use. non-null
   * @return
   * @throws X11ClientException
   */
  public static X11Client connect(@NonNull DisplayName displayName, @NonNull XAuthority xAuthority) {
    try {
      return new X11Client(X11Connection.connect(displayName, xAuthority));
    } catch (IOException e) {
      throw new X11ClientException("Could not connect with " + displayName, e);
    }
  }

  public static X11Client connect() {
    try {
      return new X11Client(X11Connection.connect());
    } catch (IOException e) {
      throw new X11ClientException("Could not connect", e);
    }
  }

  public static X11Client connect(@NonNull DisplayName name) {
    try {
      return new X11Client(X11Connection.connect(name));
    } catch (IOException e) {
      throw new X11ClientException("Could not connect with " + name, e);
    }
  }

  private X11Client(X11Connection connection) {
    this.connection = connection;
    service = new XProtocolService(connection.getSetup(), connection.getX11Input(), connection.getX11Output());
  }

  public boolean loadedPlugin(String name) {
    return service.loadedPlugin(name);
  }

  public boolean activatedPlugin(String name) {
    return service.activatedPlugin(name);
  }

  public Setup getSetup() {
    return connection.getSetup();
  }

  public Screen getScreen(int screen) {
    return getSetup().getRoots().get(screen);
  }

  public int getRoot(int screen) {
    return getScreen(screen).getRoot();
  }

  public int getWhitePixel(int screen) {
    return getScreen(screen).getWhitePixel();
  }

  public int getBlackPixel(int screen) {
    return getScreen(screen).getBlackPixel();
  }

  public byte getDepth(int screen) {
    return getScreen(screen).getRootDepth();
  }

  public int getVisualId(int screen) {
    return getScreen(screen).getRootVisual();
  }

  public void send(OneWayRequest request) {
    service.send(request);
  }

  public <T extends XReply> T send(TwoWayRequest<T> request) {
    return service.send(request);
  }

  public XEvent getNextEvent() {
    return service.getNextEvent();
  }

  public void flush() {
    service.flush();
  }

  public int nextResourceId() {
    if (hasValidNextResourceFor(nextResourceId)) {
      return maskNextResourceId(nextResourceId++);
    }
    throw new UnsupportedOperationException("must use xc_misc to get resource id");
  }

  private boolean hasValidNextResourceFor(int resourceId) {
    return (resourceId + 1 & ~getSetup().getResourceIdMask()) == 0;
  }

  private int maskNextResourceId(int resourceId) {
    return resourceId | getSetup().getResourceIdBase();
  }

  /**
   * Closes the connection.
   * @throws IOException
   */
  @Override
  public void close() throws IOException {
    connection.close();
  }
}
