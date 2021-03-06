package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.*
import javax.lang.model.element.Modifier

import static com.github.moaxcp.x11protocol.generator.Conventions.getErrorJavaName
import static com.github.moaxcp.x11protocol.generator.Conventions.getErrorTypeName

class JavaError extends JavaObjectType {
    int number

    static JavaError javaError(XTypeError error) {
        String simpleName = getErrorJavaName(error.name)

        JavaError javaError = new JavaError(
            superTypes: error.superTypes + ClassName.get(error.basePackage, 'XError'),
            basePackage: error.basePackage,
            javaPackage: error.javaPackage,
            simpleName:simpleName,
            className:getErrorTypeName(error.javaPackage, error.name),
            number: error.number
        )
        javaError.protocol = error.toJavaProtocol(javaError)
        JavaProperty c = javaError.getJavaProperty('CODE')
        c.constantField = true
        JavaProperty r = javaError.getJavaProperty('RESPONSECODE')
        r.constantField = true
        r.localOnly = true
        r.writeValueExpression = CodeBlock.of('getResponseCode()')
        if(javaError.fixedSize && javaError.fixedSize.get() < 32) {
            javaError.protocol.add(new JavaPad(bytes: 32 - javaError.fixedSize.get()))
        }
        return javaError
    }

    @Override
    void addMethods(TypeSpec.Builder typeBuilder) {
        typeBuilder.addMethod(MethodSpec.methodBuilder('getCode')
            .addAnnotation(Override)
            .returns(TypeName.BYTE)
            .addModifiers(Modifier.PUBLIC)
            .addStatement('return CODE')
            .build())

        super.addMethods(typeBuilder)
    }

    @Override
    void addWriteStatements(MethodSpec.Builder methodBuilder) {
        super.addWriteStatements(methodBuilder)
        if(fixedSize && fixedSize.get() >= 32) {
            return
        }
        methodBuilder.beginControlFlow('if(getSize() < 32)')
            .addStatement('out.writePad(32 - getSize())')
            .endControlFlow()
    }

    @Override
    void addBuilderStatement(MethodSpec.Builder methodBuilder, CodeBlock... fields) {
        super.addBuilderStatement(methodBuilder)
        if(fixedSize && fixedSize.get() >= 32) {
            return
        }
        methodBuilder.beginControlFlow('if(javaBuilder.getSize() < 32)')
            .addStatement('in.readPad(32 - javaBuilder.getSize())')
            .endControlFlow()
    }
}
