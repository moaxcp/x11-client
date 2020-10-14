package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.*
import javax.lang.model.element.Modifier

import static com.github.moaxcp.x11protocol.generator.Conventions.getRequestJavaName
import static com.github.moaxcp.x11protocol.generator.Conventions.getRequestTypeName

class JavaRequest extends JavaObjectType {
    int opCode
    XTypeReply reply

    static JavaRequest javaRequest(XTypeRequest request) {
        String simpleName = getRequestJavaName(request.name)

        JavaRequest javaRequest = new JavaRequest(
            superTypes: request.superTypes + ClassName.get(request.basePackage, 'XRequest'),
            basePackage: request.basePackage,
            javaPackage: request.javaPackage,
            simpleName:simpleName,
            className: getRequestTypeName(request.javaPackage, request.name),
            opCode: request.opCode,
            reply: request.reply
        )
        javaRequest.protocol = request.toJavaProtocol(javaRequest)
        return javaRequest
    }

    @Override
    void addFields(TypeSpec.Builder typeBuilder) {
        typeBuilder.addField(FieldSpec.builder(TypeName.BYTE, 'OPCODE', Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL)
            .initializer('$L', opCode)
            .build())
        super.addFields(typeBuilder)
    }

    @Override
    void addMethods(TypeSpec.Builder typeBuilder) {
        CodeBlock replyReturn
        if(reply) {
            replyReturn = CodeBlock.builder().addStatement('return Optional.of(() -> $T.read$L(in))', reply.javaType.className, reply.javaType.simpleName).build()
        } else {
            replyReturn = CodeBlock.builder().addStatement('return Optional.empty()').build()
        }
        typeBuilder.addMethod(MethodSpec.methodBuilder('getReplySupplier')
            .addParameter(ClassName.get('com.github.moaxcp.x11client.protocol', 'X11Input'), 'in')
            .returns(ParameterizedTypeName.get(ClassName.get('java.util', 'Optional'), ClassName.get('com.github.moaxcp.x11client.protocol', 'XReplySupplier')))
            .addModifiers(Modifier.PUBLIC)
            .addException(IOException.class)
            .addCode(replyReturn)
            .build())
        typeBuilder.addMethod(MethodSpec.methodBuilder('getOpCode')
            .returns(TypeName.BYTE)
            .addModifiers(Modifier.PUBLIC)
            .addStatement('return OPCODE')
            .build())

        super.addMethods(typeBuilder)
    }

    @Override
    void addReadStatements(MethodSpec.Builder methodBuilder) {
        if(lastListNoLength) {
            methodBuilder.addStatement('int javaStart = 1')
            protocol.eachWithIndex { it, i ->
                methodBuilder.addCode(it.readCode)
                if(i != protocol.size() - 1) {
                    methodBuilder.addStatement('javaStart += $L', it.getSizeExpression())
                }
            }
        } else {
            if(protocol.size() > 1) {
                super.addReadStatements(methodBuilder)
            } else {
                methodBuilder.addStatement('in.readByte()')
                methodBuilder.addStatement('short length = in.readCard16()')
            }
        }
    }

    @Override
    void addSetterStatements(MethodSpec.Builder methodBuilder) {
        super.addSetterStatements(methodBuilder)
        if(fixedSize && fixedSize.get() % 4 == 0) {
            return
        }
        methodBuilder.addStatement('in.readPadAlign(javaObject.getSize())')
    }

    @Override
    void addWriteStatements(MethodSpec.Builder methodBuilder) {
        methodBuilder.addStatement('out.writeCard8(OPCODE)')
        if(protocol.size() > 1) {
            CodeBlock.Builder writeProtocol = CodeBlock.builder()
            protocol.each { it ->
                if(it instanceof JavaProperty && it.name == 'length') {
                    methodBuilder.addStatement('out.writeCard16((short) getLength())')
                } else {
                    methodBuilder.addCode(it.writeCode)
                }
            }
            methodBuilder.addCode(writeProtocol.build())
        } else {
            methodBuilder.addStatement('out.writePad(1)')
            methodBuilder.addStatement('out.writeCard16((short) 0)')
        }
        if(fixedSize && fixedSize.get() % 4 == 0) {
            return
        }
        methodBuilder.addStatement('out.writePadAlign(getSize())')
    }

    @Override
    CodeBlock getSizeExpression() {
        CodeBlock.of('$L + $L',
            CodeBlock.of('1'),
            super.getSizeExpression())
    }

    @Override
    Optional<Integer> getFixedSize() {
        boolean empty = protocol.find {
            it.fixedSize.isEmpty()
        }
        if(empty) {
            return Optional.empty()
        }
        Optional.of(protocol.stream().mapToInt({it.fixedSize.get()}).sum() + 1)
    }
}