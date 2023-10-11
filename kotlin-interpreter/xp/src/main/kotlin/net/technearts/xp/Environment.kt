package net.technearts.xp

class Environment(private val enclosing: Environment? = null) {
    private val values: MutableMap<String, Any?> = HashMap()
    operator fun get(name: Token): Any? {
        return get(name.lexeme)
    }

    operator fun get(name: String): Any? {
        if (values.containsKey(name)) {
            return values[name]
        }
        if (enclosing != null) return enclosing[name]
        throw RuntimeException("Undefined variable '$name'.")
    }

    operator fun set(name: Token, value: Any?) {
        values[name.lexeme] = value
    }

    operator fun set(name: String, value: Any?) {
        values[name] = value
    }

    operator fun contains(name: Token): Boolean {
        return name.lexeme in this
    }
    operator fun contains(name: String): Boolean {
        return values[name] != null
    }
}