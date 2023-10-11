package net.technearts.xp

import net.technearts.xp.TokenType.*

internal class Scanner(private val source: String) {
    private var tokens: List<Token> = ArrayList()
    private var start = 0
    private var current = 0
    private var line = 1
    private var keywords: MutableMap<String, TokenType> = HashMap()

    init {
        keywords["false"] = FALSE
        keywords["left"] = LEFT
        keywords["null"] = NULL
        keywords["right"] = RIGHT
        keywords["this"] = THIS
        keywords["true"] = TRUE
    }

    fun scanTokens(): List<Token> {
        while (!isAtEnd()) {
            // We are at the beginning of the next lexeme.
            start = current
            scanToken()
        }
        tokens += (Token(EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd(): Boolean {
        return current >= source.length
    }

    private fun scanToken() {
        when (val c: Char = advance()) {
            '(' -> addToken(LEFT_PAREN)
            ')' -> addToken(RIGHT_PAREN)
            '[' -> addToken(LEFT_BRACKET)
            ']' -> addToken(RIGHT_BRACKET)
            '{' -> addToken(LEFT_BRACE)
            '}' -> addToken(RIGHT_BRACE)
            '-' -> addToken(MINUS)
            '+' -> addToken(PLUS)
            ';' -> addToken(SEMICOLON)
            '*' -> addToken(STAR)
            '=' -> addToken(EQUAL)
            '~' -> addToken(TILDE)
            '^' -> addToken(CIRCUMFLEX)
            '%' -> addToken(PERCENT)
            '@' -> addToken(AT)
            '/' -> addToken(SLASH)
            ':' -> addToken(COLON)
            '$' -> addToken(DOLLAR)
            '<' -> addToken(if (match('=')) LESS_EQUAL else if (match('>')) LESS_GREATER else LESS)
            '>' -> addToken(if (match('=')) GREATER_EQUAL else GREATER)
            '|' -> addToken(if (match('|')) PIPE_PIPE else PIPE)
            '&' -> addToken(if (match('&')) AMPERSAND_AMPERSAND else AMPERSAND)
            '.' -> addToken(if (match('.')) DOT_DOT else DOT)
            '#' -> while (peek() != '\n' && !isAtEnd()) advance() // A comment goes until the end of the line.
            ' ', '\r', '\t', ',' -> {} // Ignore whitespace and number class separator.
            '\n' -> line++
            '"' -> string()
            else -> if (isDigit(c)) {
                number()
            } else if (isAlpha(c)) {
                identifier()
            } else {
                error(line, "Unexpected character.")
            }
        }
    }

    private fun advance(): Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens += Token(type, text, literal, line)
    }

    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false
        current++
        return true
    }

    private fun peek(): Char {
        return if (isAtEnd()) '\u0000' else source[current]
    }

    private fun string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++
            advance()
        }
        if (isAtEnd()) {
            error(line, "Unterminated string.")
            return
        }

        // The closing "
        advance()

        // Trim the surrounding quotes.
        val value = source.substring(start + 1, current - 1)
        addToken(STRING, value)
    }

    private fun isDigit(c: Char): Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        while (isDigit(peek())) advance()

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            // Consume the "."
            advance()
            while (isDigit(peek())) advance()
            addToken(NUMBER, source.substring(start, current).toBigDecimal())
        } else {
            addToken(NUMBER, source.substring(start, current).toBigInteger())
        }
    }

    private fun peekNext(): Char {
        return if (current + 1 >= source.length) '\u0000' else source[current + 1]
    }

    private fun identifier() {
        while (isAlphaNumeric(peek())) advance()
        val text = source.substring(start, current)
        var type = keywords[text]
        if (type == null) type = IDENTIFIER
        addToken(type)
    }

    private fun isAlpha(c: Char): Boolean {
        return c in 'a'..'z' || c in 'A'..'Z' || c == '_'
    }

    private fun isAlphaNumeric(c: Char): Boolean {
        return isAlpha(c) || isDigit(c)
    }
}

/*
Comment: #
IO: @ # @in @out @err @/file
 */

