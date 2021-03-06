package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.ArrayTypeName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.TypeName
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includePackage = false)
@EqualsAndHashCode
class JavaPad implements JavaUnit, JavaReadParameter {
    String name
    JavaType javaType
    XUnit xUnit
    int bytes
    boolean readParam
    TypeName readTypeName

    @Override
    String getName() {
        if(name) {
            return name
        }
        return 'pad'
    }

    @Override
    XUnit getXUnit() { //todo why is this not generated by groovy
        return xUnit
    }

    @Override
    TypeName getTypeName() {
        return ArrayTypeName.of(byte.class)
    }

    @Override
    TypeName getReadTypeName() {
        if(readTypeName) {
            return readTypeName
        }
        return typeName
    }

    @Override
    CodeBlock getDeclareAndReadCode() {
        return CodeBlock.builder().addStatement(getReadCode()).build()
    }

    @Override
    CodeBlock getReadCode() {
        CodeBlock.of("in.readPad($bytes)")
    }

    @Override
    void addBuilderCode(CodeBlock.Builder code) {
        throw new IllegalStateException('JavaPad cannot be used with builder')
    }

    @Override
    void addWriteCode(CodeBlock.Builder code) {
        code.addStatement("out.writePad($bytes)")
    }

    @Override
    boolean isReadProtocol() {
        return !readParam
    }

    @Override
    CodeBlock getSizeExpression() {
        return CodeBlock.of('$L', bytes)
    }

    @Override
    Optional<Integer> getFixedSize() {
        return Optional.of(bytes)
    }
}
