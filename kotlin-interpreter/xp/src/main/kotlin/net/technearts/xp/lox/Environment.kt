package net.technearts.xp.lox

import net.technearts.xp.lox.Interpreter.RuntimeError


internal class Environment(val enclosing: Environment? = null) {
    private val values: MutableMap<String, Any?> = HashMap()
    fun define2(name: String, value: Any?) {
        values[name] = value
    }

    operator fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
            return values[name.lexeme]
        }
        if (enclosing != null) return enclosing[name];
        throw RuntimeError(
            name,
            "Undefined variable '" + name.lexeme + "'."
        )
    }

    fun assign(name: Token, value: Any?) {
        values[name.lexeme] = value
        /* is this ok?
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }*/
    }
}