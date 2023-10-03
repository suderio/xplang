package net.technearts.xp.lox

import net.technearts.xp.lox.Expr.Assign
import net.technearts.xp.lox.TokenType.*


class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Void?> {
    private val environment = Environment()
    fun interpret(statements: List<Stmt>) {
        try {
            for (statement in statements) {
                execute(statement)
            }
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }
    private fun execute(stmt: Stmt) {
        stmt.accept<Void?>(this)
    }
    private fun stringify(obj: Any?): String {
        if (obj == null) return "null"
        if (obj is Double) {
            var text = obj.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }
        return obj.toString()
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression): Void? {
        val value = evaluate(stmt.expression)
        println(value)
        return null
    }

    override fun visitEmptyStmt(stmt: Stmt.Empty): Void? {
        return null
    }

    override fun visitPrintStmt(stmt: Stmt.Print): Void? {
        val value = evaluate(stmt.expression)
        println(stringify(value))
        return null
    }
    override fun visitAssignExpr(expr: Assign?): Any {
        val value = evaluate(expr!!.value)
        environment.assign(expr.name, value)
        return value
    }
    override fun visitVariableExpr(expr: Expr.Variable?): Any {
        return environment[expr!!.name]!!
    }
    override fun visitBinaryExpr(expr: Expr.Binary?): Any {
        val left = evaluate(expr!!.left)
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            PLUS -> left as Double + right as Double
            MINUS -> {
                left as Double - right as Double
            }
            SLASH -> left as Double / right as Double
            STAR -> left as Double * right as Double
            GREATER -> left as Double > right as Double
            GREATER_EQUAL -> left as Double >= right as Double
            LESS -> (left as Double) < (right as Double)
            LESS_EQUAL -> left as Double <= right as Double
            EQUAL -> isEqual(left, right)
            LESS_GREATER -> !isEqual(left, right)
            else -> {}
        }
    }

    override fun visitCallExpr(expr: Expr.Call?): Any {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expr: Expr.Grouping?): Any {
        return evaluate(expr!!.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal?): Any {
        return expr!!.value!!
    }

    override fun visitLogicalExpr(expr: Expr.Logical?): Any {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expr: Expr.This?): Any {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpr(expr: Expr.Unary?): Any {
        val right = evaluate(expr!!.right)

        return when (expr.operator.type) {
            TILDE -> isTruthy(right)
            MINUS -> {
                checkNumberOperand(expr.operator, right)
                -(right as Double)
            }
            else -> {}
        }
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null) return false
        return if (obj is Boolean) obj else true
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        return if (a == null) false else a == b
    }

    private fun checkNumberOperand(operator: Token, operand: Any) {
        if (operand is Double) return
        throw RuntimeError(operator, "Operand must be a number.")
    }


    class RuntimeError(val token: Token, message: String?) : RuntimeException(message)

}