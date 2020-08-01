package com.github.moaxcp.x11protocol.parser

import spock.lang.Specification

class ValueExpressionSpec extends Specification {
    def 'constructor'() {
        given:
        ValueExpression expression = new ValueExpression(value:'value')

        expect:
        expression.expression == 'value'
    }
}
