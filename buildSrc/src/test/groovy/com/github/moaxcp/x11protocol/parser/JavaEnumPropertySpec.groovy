package com.github.moaxcp.x11protocol.parser

import com.github.moaxcp.x11protocol.XmlSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeName

import static com.github.moaxcp.x11protocol.parser.JavaEnumProperty.javaEnumProperty

class JavaEnumPropertySpec extends XmlSpec {
    def create() {
        given:
        xmlBuilder.xcb(header:'xproto') {
            "enum"(name:'EventMask') {
                item(name:'NoEvent') {
                    value("0")
                }
                item(name:'KeyPress') {
                    bit('0')
                }
                item(name:'KeyRelease') {
                    bit('1')
                }
            }
        }
        addChildNodes()
        XUnitField field = new XUnitField(result: result, name: 'mask', type: 'CARD32', enumType: 'EventMask')

        when:
        JavaEnumProperty property = javaEnumProperty(field)

        then:
        property.name == 'mask'
        property.x11Primative == 'CARD32'
        property.memberTypeName == ClassName.get(result.javaPackage, 'EventMask')
        property.typeName == ClassName.get(result.javaPackage, 'EventMask')
        property.ioTypeName == TypeName.INT
        property.member.toString() == 'private com.github.moaxcp.x11client.protocol.xproto.EventMask mask;\n'
        property.readCode.toString() == 'com.github.moaxcp.x11client.protocol.xproto.EventMask mask = com.github.moaxcp.x11client.protocol.xproto.EventMask.getByCode(in.readCard32())'
        property.writeCode.toString() == 'out.writeCard32(mask.getValue())'
    }
}