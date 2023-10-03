package net.technearts.xp

class Environment(private val enclosing: Environment? = null) {
    private val values: MutableMap<String, Any?> = HashMap()
    operator fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
            return values[name.lexeme]
        }
        if (enclosing != null) return enclosing[name];
        throw Interpreter.RuntimeError(
            name,
            "Undefined variable '" + name.lexeme + "'."
        )
    }

    operator fun set(name: Token, value: Any?) {
        values[name.lexeme] = value
    }
}