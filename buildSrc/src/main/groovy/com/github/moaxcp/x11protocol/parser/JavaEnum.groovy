package com.github.moaxcp.x11protocol.parser

import com.squareup.javapoet.ClassName
import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.ParameterizedTypeName
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

import static com.github.moaxcp.x11protocol.generator.Conventions.getEnumJavaName
import static com.github.moaxcp.x11protocol.generator.Conventions.getEnumTypeName

class JavaEnum implements JavaType {
    String simpleName
    ClassName className
    ClassName superInterface
    Map<String, String> values

    @Override
    TypeSpec getTypeSpec() {
        return TypeSpec.enumBuilder(className)
            .addModifiers(Modifier.PUBLIC)
            .addSuperinterface(superInterface)
            .addField(FieldSpec.builder(TypeName.INT, 'value', Modifier.PRIVATE).build())
            .addMethod(MethodSpec.constructorBuilder()
                .addParameter(TypeName.INT, 'value')
                .addStatement("this.\$N = \$N", 'value', 'value')
                .build())
            .addMethod(MethodSpec.methodBuilder('getValue')
                .addAnnotation(Override)
                .addModifiers(Modifier.PUBLIC)
                .addStatement("return value")
                .returns(TypeName.INT)
                .build())
            .with(true) {TypeSpec.Builder builder ->
                values.each {
                    builder.addEnumConstant(it.key,
                        TypeSpec.anonymousClassBuilder('$L', it.value).build())
                }
                builder
            }
            .addField(FieldSpec.builder(ParameterizedTypeName.get(ClassName.get(Map.class), ClassName.get(Integer), className), 'byCode')
                .addModifiers(Modifier.STATIC, Modifier.FINAL)
                .build())
            .addStaticBlock(CodeBlock.of('''\
                for($T e : values()) {
                    byCode.put(e.value, e);
                }
            '''.stripIndent(), className))
            .addMethod(MethodSpec.methodBuilder('getByCode')
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(className)
                .addParameter(TypeName.INT, 'code')
                .addStatement('return $L.get($L)', 'byCode', 'code')
                .build())
            .build()
    }

    static JavaEnum javaEnum(XTypeEnum xEnum) {
        String simpleName = getEnumJavaName(xEnum.name)
        Map<String, String> values = xEnum.items.collectEntries {
            [(it.name):it.value]
        }
        return new JavaEnum(
            simpleName: simpleName,
            className: getEnumTypeName(xEnum.javaPackage, xEnum.name),
            superInterface: ClassName.get(xEnum.basePackage, 'IntValue'),
            values: values
        )
    }
}