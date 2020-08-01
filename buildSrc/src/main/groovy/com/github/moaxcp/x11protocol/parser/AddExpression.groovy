package com.github.moaxcp.x11protocol.parser

class AddExpression extends OpExpression {
    AddExpression() {
        op = '+'
    }

    String getExpression() {
        expressions.collect{it.expression}.join(" $op ")
    }
}
