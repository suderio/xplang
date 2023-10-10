package net.technearts.xp

import net.technearts.xp.TokenType.*
import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime.now
import java.time.ZoneOffset

class Interpreter : Expr.Visitor<Any> {
    class RuntimeError(val token: Token, message: String?) : RuntimeException(message)

    private val environment = Environment()
    fun interpret(expressions: List<Expr>) {
        try {
            for (expression in expressions) {
                println(evaluate(expression))
            }
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    private fun applyBinaryOperator(expr: Expr.Binary, operator: BinaryOperator, shortCircuit: Boolean = false): Any {
        return if (shortCircuit && operator is ShortCircuit) {
            val left = evaluate(expr.left)
            if (operator.proceed(left)) {
                operator.op(left, evaluate(expr.right))
            } else {
                operator.shortCircuitValue()
            }
        } else {
            operator.op(evaluate(expr.left), evaluate(expr.right))
        }
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        return when (expr.operator.type) {
            PLUS -> applyBinaryOperator(expr, Sum())
            MINUS -> applyBinaryOperator(expr, Sub())
            SLASH -> applyBinaryOperator(expr, Div())
            STAR -> applyBinaryOperator(expr, Mul())
            CIRCUMFLEX -> applyBinaryOperator(expr, Pow())
            PERCENT -> applyBinaryOperator(expr, Mod())

            GREATER -> applyBinaryOperator(expr, GT())
            GREATER_EQUAL -> applyBinaryOperator(expr, GE())
            LESS -> applyBinaryOperator(expr, LT())
            LESS_EQUAL -> applyBinaryOperator(expr, LE())
            EQUAL -> applyBinaryOperator(expr, EQ())
            LESS_GREATER -> applyBinaryOperator(expr, NE())

            PIPE -> applyBinaryOperator(expr, Or())
            PIPE_PIPE -> applyBinaryOperator(expr, Or(), true)
            AMPERSAND -> applyBinaryOperator(expr, And())
            AMPERSAND_AMPERSAND -> applyBinaryOperator(expr, And(), true)

            SEMICOLON -> cons(evaluate(expr.left), evaluate(expr.right))
            AT -> io(evaluate(expr.left), evaluate(expr.right))
            COLON -> assign(expr.left, expr.right)
            DOT_DOT -> evaluate(expr.left) as BigDecimal..evaluate(expr.right) as BigDecimal
            DOT -> intersect(evaluate(expr.left), evaluate(expr.right))

            else -> {}
        }
    }

    private fun assign(left: Expr, right: Expr): Any {
        if (left is Expr.Variable) {
            environment[left.name] = evaluate(right)
            return environment[left.name]!!
        } else {
            throw RuntimeException("Assignment must have a variable name on the left side.")
        }
    }

    private fun intersect(left: Any, right: Any): MutableList<Any> {
        return if (left is MutableList<*> && right is MutableList<*>) {
            (left.toSet() intersect right.toSet()).filterNotNull().toMutableList()
        } else if (left is MutableList<*>) {
            if (right in left) mutableListOf(right) else emptyList<Any>().toMutableList()
        } else if (right is MutableList<*>) {
            if (left in right) mutableListOf(left) else emptyList<Any>().toMutableList()
        } else {
            emptyList<Any>().toMutableList()
        }
    }

    private fun cons(left: Any, right: Any): MutableList<Any> {
        @Suppress("UNCHECKED_CAST") val result: MutableList<Any> =
            if (left is MutableList<*>) left as MutableList<Any> else mutableListOf(left)
        result.add(right)
        return result
    }

    private fun io(left: Any, right: Any): Any {
        when (right) {
            // TODO reconhecer o identifier após o @
            "in" -> readln()
            "out" -> println(left)
            "err" -> System.err.println(left)
            "now" -> now().toEpochSecond(ZoneOffset.UTC)
        }
        return left
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            TILDE -> isTruthy(right)
            MINUS -> when (right) {
                is BigDecimal -> -right
                is BigInteger -> -right
                else -> throw RuntimeError(expr.operator, "Operand must be a number: $right")
            }

            else -> {}
        }
    }

    override fun visitVariableExpr(variable: Expr.Variable): Any {
            return if (environment[variable.name] != null) environment[variable.name]!! else variable
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value!!
    }

    private fun isTruthy(obj: Any?): Boolean {
        if (obj == null) return false
        return if (obj is Boolean) obj else true
    }

}