package net.technearts.xp

import net.technearts.xp.TokenType.*


class Parser(private val tokens: List<Token>) {
    private class ParseError : RuntimeException()

    private var current = 0

    fun parse(): List<Expr> {
        val expressions = ArrayList<Expr>()
        while (!isAtEnd()) {
            expressions += expression()
        }
        return expressions
    }

    private fun isAtEnd(): Boolean {
        return peek().type === EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type === type
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun error(token: Token, message: String): ParseError {
        error(token, message)
        return ParseError()
    }

    private fun expression(): Expr {
        return binary()
    }

    private fun binary(): Expr {
        var expr: Expr = unary()
        while (match(
                PLUS, MINUS, SLASH, STAR, CIRCUMFLEX, PERCENT,
                GREATER, GREATER_EQUAL, LESS, LESS_EQUAL, LESS_GREATER, EQUAL,
                PIPE, PIPE_PIPE, AMPERSAND, AMPERSAND_AMPERSAND,
                SEMICOLON, AT, COLON, DOT_DOT, DOT
            )
        ) {
            val operator: Token = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(TILDE, MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return unaryCall()
    }
    private fun unaryCall(): Expr {
        var expr: Expr = primary()
        while (true) {
            if (match(DOLLAR)) {
                expr = finishCall(expr)
            } else {
                break
            }
        }
        return expr
    }

    private fun finishCall(callee: Expr): Expr {
        return Expr.Call(callee, expression())
    }

    private fun primary(): Expr {
        if (match(FALSE)) return Expr.Literal(false)
        if (match(TRUE)) return Expr.Literal(true)
        if (match(NULL)) return Expr.Literal(null)
        if (match(THIS)) TODO()
        if (match(LEFT)) TODO()
        if (match(RIGHT)) TODO()
        if (match(NUMBER, STRING)) {
            return Expr.Literal(previous().literal!!)
        }
        if (match(IDENTIFIER)) {
            return Expr.Variable(previous())
        }
        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }
        if (match(LEFT_BRACKET)) {
            val expr = expression()
            consume(RIGHT_BRACKET, "Expect ']' after expression.")
            return Expr.Grouping(expr)
        }
        if (match(LEFT_BRACE)) {
            val expr = expression()
            consume(RIGHT_BRACE, "Expect '}' after expression.")
            return Expr.Grouping(expr)
        }
        throw error(peek(), "Expect expression.")
    }

}