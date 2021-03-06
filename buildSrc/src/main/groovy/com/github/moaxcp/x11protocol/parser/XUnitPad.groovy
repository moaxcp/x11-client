package com.github.moaxcp.x11protocol.parser

class XUnitPad implements XUnit {
    int bytes

    @Override
    JavaPad getJavaUnit(JavaType javaType) {
        return new JavaPad(javaType: javaType, xUnit:this, bytes:bytes)
    }
}
