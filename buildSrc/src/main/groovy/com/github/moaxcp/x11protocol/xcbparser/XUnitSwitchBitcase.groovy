package com.github.moaxcp.x11protocol.xcbparser

import groovy.util.slurpersupport.Node

import static com.github.moaxcp.x11protocol.xcbparser.XUnitField.xUnitField
import static com.github.moaxcp.x11protocol.xcbparser.XUnitListField.xUnitListField

class XUnitSwitchBitcase extends XUnitSwitch {
    String fieldRef
    List<XUnit> fields

    @Override
    List<JavaUnit> getJavaUnit(JavaType javaType) {
        return fields.collect {
            it.getJavaUnit(javaType)
        }.flatten()
    }

    static XUnitSwitchBitcase parseValueList(XResult result, Node node) {
        String fieldRef = node.childNodes().find{Node it -> it.name() == 'fieldref'}.text()
        List<XUnit> fields = []
        node.childNodes().each { Node it ->
            if(it.name() == 'fieldref') {
                return
            }
            if(it.name() == 'bitcase') {
                String enumRef
                String enumItem
                it.childNodes().each { Node bitcaseNode ->
                    if(bitcaseNode.name() == 'enumref') {
                        enumRef = bitcaseNode.attributes().get('ref')
                        enumItem = bitcaseNode.text()
                    } else {
                        XUnit unit = parseXUnit(result, bitcaseNode, new XBitcaseInfo(maskField: fieldRef, enumType: enumRef, enumItem: enumItem))
                        fields.add(unit)
                    }
                }
            }
        }
        return new XUnitSwitchBitcase(fieldRef: fieldRef, fields: fields)
    }

    static XUnit parseXUnit(XResult result, Node node, XBitcaseInfo bitcaseInfo) {
        switch(node.name()) {
            case 'field':
                return xUnitField(result, node, bitcaseInfo)
            case 'list':
                return xUnitListField(result, node, bitcaseInfo)
            default:
                throw new IllegalArgumentException("cannot parse ${node.name()}")
        }
    }
}