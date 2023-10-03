package net.technearts.xp

import net.technearts.xp.TokenType.*
import java.math.BigDecimal
import java.math.BigInteger

class Interpreter : Expr.Visitor<Any> {
    class RuntimeError(val token: Token, message: String?) : RuntimeException(message)

    private val environment = Environment()
    fun interpret(expressions: List<Expr>) {
        try {
            for (statement in expressions) {
                println(evaluate(statement))
            }
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left = evaluate(expr.left)
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            PLUS -> sum(expr.left, expr.right)
            MINUS -> subtract(expr.left, expr.right)
            SLASH -> divide(expr.left, expr.right)
            STAR -> multiply(expr.left, expr.right)
            GREATER -> left as BigDecimal > right as BigDecimal
            GREATER_EQUAL -> left as BigDecimal >= right as BigDecimal
            LESS -> (left as BigDecimal) < (right as BigDecimal)
            LESS_EQUAL -> left as BigDecimal <= right as BigDecimal
            EQUAL -> isEqual(left, right)
            LESS_GREATER -> !isEqual(left, right)
            SEMICOLON -> cons(left, right)
            CIRCUMFLEX -> (left as BigDecimal).pow((right as BigDecimal).toInt())
            PERCENT -> left as BigDecimal % right as BigDecimal
            AT -> io(left, right)
            COLON -> Pair(left, right)
            PIPE -> {
                val l = left as Boolean
                val r = right as Boolean
                l || r
            }

            PIPE_PIPE -> left as Boolean || right as Boolean
            AMPERSAND -> {
                val l = left as Boolean
                val r = right as Boolean
                l && r
            }

            AMPERSAND_AMPERSAND -> left as Boolean && right as Boolean
            DOT_DOT -> left as BigDecimal..right as BigDecimal
            else -> {}
        }
    }

    private fun sum(left: Expr, right: Expr): Any {
        val l = evaluate(left)
        val r = evaluate(right)
        return when (l) {
            is BigDecimal -> {
                when (r) {
                    is BigDecimal -> l + r
                    is BigInteger -> l + r.toBigDecimal()
                    else -> TODO()
                }
            }

            is BigInteger -> {
                when (r) {
                    is BigInteger -> l + r
                    is BigDecimal -> l.toBigDecimal() + r
                    else -> TODO()
                }
            }

            else -> TODO()
        }
    }

    private fun subtract(left: Expr, right: Expr): Any {
        val l = evaluate(left)
        val r = evaluate(right)
        return when (l) {
            is BigDecimal -> {
                when (r) {
                    is BigDecimal -> l - r
                    is BigInteger -> l - r.toBigDecimal()
                    else -> TODO()
                }
            }

            is BigInteger -> {
                when (r) {
                    is BigInteger -> l - r
                    is BigDecimal -> l.toBigDecimal() - r
                    else -> TODO()
                }
            }

            else -> TODO()
        }
    }

    private fun multiply(left: Expr, right: Expr): Any {
        val l = evaluate(left)
        val r = evaluate(right)
        try {
            return when (l) {
                is BigDecimal -> {
                    when (r) {
                        is BigDecimal -> l * r
                        is BigInteger -> l * r.toBigDecimal()
                        else -> TODO()
                    }
                }

                is BigInteger -> {
                    when (r) {
                        is BigInteger -> l * r
                        is BigDecimal -> l.toBigDecimal() * r
                        else -> TODO()
                    }
                }

                else -> TODO()
            }
        } catch (e: Exception) {
            return Double.NaN
        }
    }

    private fun divide(left: Expr, right: Expr): Any {
        val l = evaluate(left)
        val r = evaluate(right)
        return when (l) {
            is BigDecimal -> {
                when (r) {
                    is BigDecimal -> l / r
                    is BigInteger -> l / r.toBigDecimal()
                    else -> TODO()
                }
            }

            is BigInteger -> {
                when (r) {
                    is BigInteger -> l / r
                    is BigDecimal -> l.toBigDecimal() / r
                    else -> TODO()
                }
            }

            else -> TODO()
        }
    }

    private fun cons(left: Any, right: Any): MutableList<Any> {
        val result: MutableList<Any> = if (left is MutableList<*>) left as MutableList<Any> else mutableListOf(left)
        result.add(right)
        return result
    }

    private fun io(left: Any, right: Any) {
        when (right) {
            "in" -> TODO()
            "out" -> TODO()
            "err" -> TODO()
            "now" -> TODO()
            else -> TODO()
        }
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right = evaluate(expr.right)

        return when (expr.operator.type) {
            TILDE -> isTruthy(right)
            MINUS -> {
                checkNumberOperand(expr.operator, right)
                -(right as BigDecimal)
            }

            else -> {}
        }
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

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        return if (a == null) false else a == b
    }

    private fun checkNumberOperand(operator: Token, operand: Any) {
        if (operand is BigDecimal) return
        throw RuntimeError(operator, "Operand must be a number.")
    }
}