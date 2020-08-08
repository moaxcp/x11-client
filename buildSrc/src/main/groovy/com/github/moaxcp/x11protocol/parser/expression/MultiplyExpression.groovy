package com.github.moaxcp.x11protocol.parser.expression

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includePackage = false)
@EqualsAndHashCode
class MultiplyExpression extends OpExpression {
    MultiplyExpression() {
        op = '*'
    }
    String getExpression() {
        expressions.collect{
            if(it instanceof OpExpression && (it.op == '+' || it.op == '-' || it.op == '/')) {
                return "(${it.expression})"
            }
            it.expression
        }.join(" $op ")
    }
}