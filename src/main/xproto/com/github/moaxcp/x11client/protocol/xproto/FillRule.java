package com.github.moaxcp.x11client.protocol.xproto;

import com.github.moaxcp.x11client.protocol.IntValue;
import java.util.HashMap;
import java.util.Map;

public enum FillRule implements IntValue {
  EVEN_ODD(0),

  WINDING(1);

  static final Map<Integer, FillRule> byCode = new HashMap<>();

  static {
        for(FillRule e : values()) {
            byCode.put(e.value, e);
        }
  }

  private int value;

  FillRule(int value) {
    this.value = value;
  }

  @Override
  public int getValue() {
    return value;
  }

  public static FillRule getByCode(int code) {
    return byCode.get(code);
  }
}
