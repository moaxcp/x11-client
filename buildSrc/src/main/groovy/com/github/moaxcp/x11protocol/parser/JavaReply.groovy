package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.*

import static com.github.moaxcp.x11protocol.generator.Conventions.getReplyJavaName
import static com.github.moaxcp.x11protocol.generator.Conventions.getReplyTypeName

class JavaReply extends JavaObjectType {
    static JavaReply javaReply(XTypeReply reply) {
        String simpleName= getReplyJavaName(reply.name)

        JavaReply javaReply = new JavaReply(
            superTypes: reply.superTypes + ClassName.get(reply.basePackage, 'XReply'),
            basePackage: reply.basePackage,
            javaPackage: reply.javaPackage,
            simpleName:simpleName,
            className: getReplyTypeName(reply.javaPackage, reply.name)
        )
        javaReply.protocol = reply.toJavaProtocol(javaReply)
        JavaProperty r = javaReply.getJavaProperty('RESPONSECODE')
        r.constantField = true
        r.localOnly = true
        r.writeValueExpression = CodeBlock.of('getResponseCode()')
        JavaProperty l = javaReply.getJavaProperty('length')
        l.writeValueExpression = CodeBlock.of('getLength()')
        if(!(javaReply.protocol[1] instanceof JavaReadParameter)) {
            throw new IllegalStateException("First field must be a JavaReadParameter")
        }
        JavaReadParameter first = (JavaReadParameter) javaReply.protocol[1]
        first.readParam = true
        first.readTypeName = TypeName.BYTE
        if(!(javaReply.protocol[2] instanceof JavaReadParameter)) {
            throw new IllegalStateException("Second field must be a JavaReadParameter")
        }
        ((JavaReadParameter)javaReply.protocol[2]).readParam = true
        return javaReply
    }

    @Override
    void addReadStatements(MethodSpec.Builder methodBuilder) {
        if(lastListNoLength) {
            methodBuilder.addStatement('int javaStart = 1')
            protocol.eachWithIndex { it, i ->
                if(!it.readProtocol
                    || (it instanceof JavaProperty && it.bitcaseInfo)) {
                    return
                }
                methodBuilder.addCode(it.declareAndReadCode)
                if(i != protocol.size() - 1) {
                    methodBuilder.addStatement('javaStart += $L', it.getSizeExpression())
                }
            }
        } else {
            super.addReadStatements(methodBuilder)
        }
        if(fixedSize && fixedSize.get() < 32) {
            methodBuilder.addStatement('in.readPad($L)', 32 - fixedSize.get())
        }
    }

    @Override
    void addBuilderStatement(MethodSpec.Builder methodBuilder, CodeBlock... fields) {
        super.addBuilderStatement(methodBuilder, fields)
        if(!fixedSize) {
            methodBuilder.beginControlFlow('if(javaBuilder.getSize() < 32)')
            methodBuilder.addStatement('in.readPad(32 - javaBuilder.getSize())')
            methodBuilder.endControlFlow()
            return
        }

        if(fixedSize && fixedSize.get() % 4 == 0) {
            return
        }
        methodBuilder.addStatement('in.readPadAlign(javaBuilder.getSize())')
    }

    @Override
    void addWriteStatements(MethodSpec.Builder methodBuilder) {
        super.addWriteStatements(methodBuilder)
        if(fixedSize && fixedSize.get() % 4 == 0) {
            return
        }
        methodBuilder.addStatement('out.writePadAlign(getSize())')
    }
}
