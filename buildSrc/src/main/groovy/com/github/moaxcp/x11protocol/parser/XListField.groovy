package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.CodeBlock

class XListField extends ResolvableXUnit {
    Expression lengthExpression

    @Override
    CodeBlock getReadCode() {
        return null
    }

    @Override
    CodeBlock getWriteCode() {
        return null
    }
}