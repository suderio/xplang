package net.technearts.xp

import net.technearts.math.BigDecimalMath
import java.math.BigDecimal
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO
import java.math.BigDecimal.ONE as D_ONE
import java.math.BigDecimal.ZERO as D_ZERO

interface ShortCircuit {
    fun proceed(left: Any): Boolean
    fun shortCircuitValue(): Boolean
}

abstract class BinaryOperator {
    abstract infix fun BigDecimal.op(right: Any): Any
    abstract infix fun BigInteger.op(right: Any): Any
    abstract infix fun Boolean.op(right: Any): Any
    abstract infix fun String.op(right: Any): Any
    abstract infix fun List<*>.op(right: Any): Any

    fun op(left: Any, right: Any): Any {
        return when (left) {
            is BigDecimal -> left op right
            is BigInteger -> left op right
            is Boolean -> left op right
            is String -> left op right
            is List<*> -> left op right
            else -> throw RuntimeException("Unknown Type: $left")
        }
    }

}

class Sum : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.add(right)
        is BigInteger -> this.add(right.toBigDecimal())
        is Boolean -> this.add(if (right) D_ONE else D_ZERO)
        is String -> this.add(right.length.toBigDecimal())
        is List<*> -> this.add(right.size.toBigDecimal())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal().add(right)
        is BigInteger -> this.add(right)
        is Boolean -> this.add(if (right) ONE else ZERO)
        is String -> this.add(right.length.toBigInteger())
        is List<*> -> this.add(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right.add(if (this) D_ONE else D_ZERO)
        is BigInteger -> right.add(if (this) ONE else ZERO)
        is Boolean -> (if (this) ONE else ZERO).add((if (right) ONE else ZERO))
        is String -> right.length.toBigInteger().add(if (this) ONE else ZERO)
        is List<*> -> right.size.toBigInteger().add(if (this) ONE else ZERO)
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal().add(right)
        is BigInteger -> this.length.toBigInteger().add(right)
        is Boolean -> this.length.toBigInteger().add(if (right) ONE else ZERO)
        is String -> this.length.toBigInteger().add(right.length.toBigInteger())
        is List<*> -> this.length.toBigInteger().add(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal().add(right)
        is BigInteger -> this.size.toBigInteger().add(right)
        is Boolean -> this.size.toBigInteger().add(if (right) ONE else ZERO)
        is String -> this.size.toBigInteger().add(right.length.toBigInteger())
        is List<*> -> this.size.toBigInteger().add(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class Sub : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.subtract(right)
        is BigInteger -> this.subtract(right.toBigDecimal())
        is Boolean -> this.subtract(if (right) D_ONE else D_ZERO)
        is String -> this.subtract(right.length.toBigDecimal())
        is List<*> -> this.subtract(right.size.toBigDecimal())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal().subtract(right)
        is BigInteger -> this.subtract(right)
        is Boolean -> this.subtract(if (right) ONE else ZERO)
        is String -> this.subtract(right.length.toBigInteger())
        is List<*> -> this.subtract(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> (if (this) D_ONE else D_ZERO).subtract(right)
        is BigInteger -> (if (this) ONE else ZERO).subtract(right)
        is Boolean -> (if (this) ONE else ZERO).subtract((if (right) ONE else ZERO))
        is String -> (if (this) ONE else ZERO).subtract(right.length.toBigInteger())
        is List<*> -> (if (this) ONE else ZERO).subtract(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal().subtract(right)
        is BigInteger -> this.length.toBigInteger().subtract(right)
        is Boolean -> this.length.toBigInteger().subtract(if (right) ONE else ZERO)
        is String -> this.length.toBigInteger().subtract(right.length.toBigInteger())
        is List<*> -> this.length.toBigInteger().subtract(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal().subtract(right)
        is BigInteger -> this.size.toBigInteger().subtract(right)
        is Boolean -> this.size.toBigInteger().subtract(if (right) ONE else ZERO)
        is String -> this.size.toBigInteger().subtract(right.length.toBigInteger())
        is List<*> -> this.size.toBigInteger().subtract(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class Mul : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.multiply(right)
        is BigInteger -> this.multiply(right.toBigDecimal())
        is Boolean -> this.multiply(if (right) D_ONE else D_ZERO)
        is String -> this.multiply(right.length.toBigDecimal())
        is List<*> -> this.multiply(right.size.toBigDecimal())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal().multiply(right)
        is BigInteger -> this.multiply(right)
        is Boolean -> this.multiply(if (right) ONE else ZERO)
        is String -> this.multiply(right.length.toBigInteger())
        is List<*> -> this.multiply(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> (if (this) D_ONE else D_ZERO).multiply(right)
        is BigInteger -> (if (this) ONE else ZERO).multiply(right)
        is Boolean -> (if (this) ONE else ZERO).multiply((if (right) ONE else ZERO))
        is String -> (if (this) ONE else ZERO).multiply(right.length.toBigInteger())
        is List<*> -> (if (this) ONE else ZERO).multiply(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal().multiply(right)
        is BigInteger -> this.length.toBigInteger().multiply(right)
        is Boolean -> this.length.toBigInteger().multiply(if (right) ONE else ZERO)
        is String -> this.length.toBigInteger().multiply(right.length.toBigInteger())
        is List<*> -> this.length.toBigInteger().multiply(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal().multiply(right)
        is BigInteger -> this.size.toBigInteger().multiply(right)
        is Boolean -> this.size.toBigInteger().multiply(if (right) ONE else ZERO)
        is String -> this.size.toBigInteger().multiply(right.length.toBigInteger())
        is List<*> -> this.size.toBigInteger().multiply(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }
}


class Pow : BinaryOperator() {

    private fun BigDecimal.pow(right: BigDecimal): BigDecimal {
        return BigDecimalMath.pow(this, right)
    }

    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.pow(right)
        is BigInteger -> this.pow(right.toBigDecimal())
        is Boolean -> this.pow(if (right) D_ONE else D_ZERO)
        is String -> this.pow(right.length.toBigDecimal())
        is List<*> -> this.pow(right.size.toBigDecimal())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal().pow(right)
        is BigInteger -> this.pow(right.toInt())
        is Boolean -> this.pow(if (right) 1 else 0)
        is String -> this.pow(right.length)
        is List<*> -> this.pow(right.size)
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> (if (this) D_ONE else D_ZERO).pow(right)
        is BigInteger -> (if (this) ONE else ZERO).pow(right.toInt())
        is Boolean -> (if (this) ONE else ZERO).pow((if (right) 1 else 0))
        is String -> (if (this) ONE else ZERO).pow(right.length)
        is List<*> -> (if (this) ONE else ZERO).pow(right.size)
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal().pow(right)
        is BigInteger -> this.length.toBigInteger().pow(right.toInt())
        is Boolean -> this.length.toBigInteger().pow(if (right) 1 else 0)
        is String -> this.length.toBigInteger().pow(right.length)
        is List<*> -> this.length.toBigInteger().pow(right.size)
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal().pow(right)
        is BigInteger -> this.size.toBigInteger().pow(right.toInt())
        is Boolean -> this.size.toBigInteger().pow(if (right) 1 else 0)
        is String -> this.size.toBigInteger().pow(right.length)
        is List<*> -> this.size.toBigInteger().pow(right.size)
        else -> throw RuntimeException("Unknown Type: $right")
    }
}


class Div : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.divide(right)
        is BigInteger -> this.divide(right.toBigDecimal())
        is Boolean -> this.divide(if (right) D_ONE else D_ZERO)
        is String -> this.divide(right.length.toBigDecimal())
        is List<*> -> this.divide(right.size.toBigDecimal())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal().divide(right)
        is BigInteger -> this.divide(right)
        is Boolean -> this.divide(if (right) ONE else ZERO)
        is String -> this.divide(right.length.toBigInteger())
        is List<*> -> this.divide(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> (if (this) D_ONE else D_ZERO).divide(right)
        is BigInteger -> (if (this) ONE else ZERO).divide(right)
        is Boolean -> (if (this) ONE else ZERO).divide((if (right) ONE else ZERO))
        is String -> (if (this) ONE else ZERO).divide(right.length.toBigInteger())
        is List<*> -> (if (this) ONE else ZERO).divide(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal().divide(right)
        is BigInteger -> this.length.toBigInteger().divide(right)
        is Boolean -> this.length.toBigInteger().divide(if (right) ONE else ZERO)
        is String -> this.length.toBigInteger().divide(right.length.toBigInteger())
        is List<*> -> this.length.toBigInteger().divide(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal().divide(right)
        is BigInteger -> this.size.toBigInteger().divide(right)
        is Boolean -> this.size.toBigInteger().divide(if (right) ONE else ZERO)
        is String -> this.size.toBigInteger().divide(right.length.toBigInteger())
        is List<*> -> this.size.toBigInteger().divide(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }
}


class Mod : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigInteger().mod(right.toBigInteger())
        is BigInteger -> this.toBigInteger().mod(right)
        is Boolean -> this.toBigInteger().mod(if (right) ONE else ZERO)
        is String -> this.toBigInteger().mod(right.length.toBigInteger())
        is List<*> -> this.toBigInteger().mod(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.mod(right.toBigInteger())
        is BigInteger -> this.mod(right)
        is Boolean -> this.mod(if (right) ONE else ZERO)
        is String -> this.mod(right.length.toBigInteger())
        is List<*> -> this.mod(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> (if (this) ONE else ZERO).mod(right.toBigInteger())
        is BigInteger -> (if (this) ONE else ZERO).mod(right)
        is Boolean -> this && right
        is String -> (if (this) ONE else ZERO).mod(right.length.toBigInteger())
        is List<*> -> (if (this) ONE else ZERO).mod(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigInteger().mod(right.toBigInteger())
        is BigInteger -> this.length.toBigInteger().mod(right)
        is Boolean -> this.length.toBigInteger().mod(if (right) ONE else ZERO)
        is String -> this.length.toBigInteger().mod(right.length.toBigInteger())
        is List<*> -> this.length.toBigInteger().mod(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigInteger().mod(right.toBigInteger())
        is BigInteger -> this.size.toBigInteger().mod(right)
        is Boolean -> this.size.toBigInteger().mod(if (right) ONE else ZERO)
        is String -> this.size.toBigInteger().mod(right.length.toBigInteger())
        is List<*> -> this.size.toBigInteger().mod(right.size.toBigInteger())
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

//**************************************************//


class GT : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this > right
        is BigInteger -> this > right.toBigDecimal()
        is Boolean -> this > if (right) D_ONE else D_ZERO
        is String -> this > right.length.toBigDecimal()
        is List<*> -> this > right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() > right
        is BigInteger -> this > right
        is Boolean -> this > if (right) ONE else ZERO
        is String -> this > right.length.toBigInteger()
        is List<*> -> this > right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right > if (this) D_ONE else D_ZERO
        is BigInteger -> right > if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO > if (right) ONE else ZERO
        is String -> right.length.toBigInteger() > if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() > if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() > right
        is BigInteger -> this.length.toBigInteger() > right
        is Boolean -> this.length.toBigInteger() > if (right) ONE else ZERO
        is String -> this.length.toBigInteger() > right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() > right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() > right
        is BigInteger -> this.size.toBigInteger() > right
        is Boolean -> this.size.toBigInteger() > if (right) ONE else ZERO
        is String -> this.size.toBigInteger() > right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() > right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class GE : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this >= right
        is BigInteger -> this >= right.toBigDecimal()
        is Boolean -> this >= if (right) D_ONE else D_ZERO
        is String -> this >= right.length.toBigDecimal()
        is List<*> -> this >= right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() >= right
        is BigInteger -> this >= right
        is Boolean -> this >= if (right) ONE else ZERO
        is String -> this >= right.length.toBigInteger()
        is List<*> -> this >= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right >= if (this) D_ONE else D_ZERO
        is BigInteger -> right >= if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO >= if (right) ONE else ZERO
        is String -> right.length.toBigInteger() >= if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() >= if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() >= right
        is BigInteger -> this.length.toBigInteger() >= right
        is Boolean -> this.length.toBigInteger() >= if (right) ONE else ZERO
        is String -> this.length.toBigInteger() >= right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() >= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() >= right
        is BigInteger -> this.size.toBigInteger() >= right
        is Boolean -> this.size.toBigInteger() >= if (right) ONE else ZERO
        is String -> this.size.toBigInteger() >= right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() >= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}


class LT : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this < right
        is BigInteger -> this < right.toBigDecimal()
        is Boolean -> this < if (right) D_ONE else D_ZERO
        is String -> this < right.length.toBigDecimal()
        is List<*> -> this < right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() < right
        is BigInteger -> this < right
        is Boolean -> this < if (right) ONE else ZERO
        is String -> this < right.length.toBigInteger()
        is List<*> -> this < right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right < if (this) D_ONE else D_ZERO
        is BigInteger -> right < if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO < if (right) ONE else ZERO
        is String -> right.length.toBigInteger() < if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() < if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() < right
        is BigInteger -> this.length.toBigInteger() < right
        is Boolean -> this.length.toBigInteger() < if (right) ONE else ZERO
        is String -> this.length.toBigInteger() < right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() < right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() < right
        is BigInteger -> this.size.toBigInteger() < right
        is Boolean -> this.size.toBigInteger() < if (right) ONE else ZERO
        is String -> this.size.toBigInteger() < right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() < right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class LE : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this <= right
        is BigInteger -> this <= right.toBigDecimal()
        is Boolean -> this <= if (right) D_ONE else D_ZERO
        is String -> this <= right.length.toBigDecimal()
        is List<*> -> this <= right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() <= right
        is BigInteger -> this <= right
        is Boolean -> this <= if (right) ONE else ZERO
        is String -> this <= right.length.toBigInteger()
        is List<*> -> this <= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right <= if (this) D_ONE else D_ZERO
        is BigInteger -> right <= if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO <= if (right) ONE else ZERO
        is String -> right.length.toBigInteger() <= if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() <= if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() <= right
        is BigInteger -> this.length.toBigInteger() <= right
        is Boolean -> this.length.toBigInteger() <= if (right) ONE else ZERO
        is String -> this.length.toBigInteger() <= right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() <= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() <= right
        is BigInteger -> this.size.toBigInteger() <= right
        is Boolean -> this.size.toBigInteger() <= if (right) ONE else ZERO
        is String -> this.size.toBigInteger() <= right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() <= right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class EQ : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this == right
        is BigInteger -> this == right.toBigDecimal()
        is Boolean -> this == if (right) D_ONE else D_ZERO
        is String -> this == right.length.toBigDecimal()
        is List<*> -> this == right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() == right
        is BigInteger -> this == right
        is Boolean -> this == if (right) ONE else ZERO
        is String -> this == right.length.toBigInteger()
        is List<*> -> this == right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right == if (this) D_ONE else D_ZERO
        is BigInteger -> right == if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO == if (right) ONE else ZERO
        is String -> right.length.toBigInteger() == if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() == if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() == right
        is BigInteger -> this.length.toBigInteger() == right
        is Boolean -> this.length.toBigInteger() == if (right) ONE else ZERO
        is String -> this.length.toBigInteger() == right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() == right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() == right
        is BigInteger -> this.size.toBigInteger() == right
        is Boolean -> this.size.toBigInteger() == if (right) ONE else ZERO
        is String -> this.size.toBigInteger() == right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() == right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class NE : BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this != right
        is BigInteger -> this != right.toBigDecimal()
        is Boolean -> this != if (right) D_ONE else D_ZERO
        is String -> this != right.length.toBigDecimal()
        is List<*> -> this != right.size.toBigDecimal()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this.toBigDecimal() != right
        is BigInteger -> this != right
        is Boolean -> this != if (right) ONE else ZERO
        is String -> this != right.length.toBigInteger()
        is List<*> -> this != right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> right != if (this) D_ONE else D_ZERO
        is BigInteger -> right != if (this) ONE else ZERO
        is Boolean -> if (this) ONE else ZERO != if (right) ONE else ZERO
        is String -> right.length.toBigInteger() != if (this) ONE else ZERO
        is List<*> -> right.size.toBigInteger() != if (this) ONE else ZERO
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.length.toBigDecimal() != right
        is BigInteger -> this.length.toBigInteger() != right
        is Boolean -> this.length.toBigInteger() != if (right) ONE else ZERO
        is String -> this.length.toBigInteger() != right.length.toBigInteger()
        is List<*> -> this.length.toBigInteger() != right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.size.toBigDecimal() != right
        is BigInteger -> this.size.toBigInteger() != right
        is Boolean -> this.size.toBigInteger() != if (right) ONE else ZERO
        is String -> this.size.toBigInteger() != right.length.toBigInteger()
        is List<*> -> this.size.toBigInteger() != right.size.toBigInteger()
        else -> throw RuntimeException("Unknown Type: $right")
    }
}

class And : ShortCircuit, BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this != D_ZERO && right != D_ZERO
        is BigInteger -> this != D_ZERO && right != ZERO
        is Boolean -> this != D_ZERO && right
        is String -> this != D_ZERO && right.isNotEmpty()
        is List<*> -> this != D_ZERO && right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this != ZERO && right != D_ZERO
        is BigInteger -> this != ZERO && right != ZERO
        is Boolean -> this != ZERO && right
        is String -> this != ZERO && right.isNotEmpty()
        is List<*> -> this != ZERO && right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> this && right != D_ZERO
        is BigInteger -> this && right != ZERO
        is Boolean -> this && right
        is String -> this && right.isNotEmpty()
        is List<*> -> this && right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.isNotEmpty() && right != D_ZERO
        is BigInteger -> this.isNotEmpty() && right != ZERO
        is Boolean -> this.isNotEmpty() && right
        is String -> this.isNotEmpty() && right.isNotEmpty()
        is List<*> -> this.isNotEmpty() && right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.isNotEmpty() && right != D_ZERO
        is BigInteger -> this.isNotEmpty() && right != ZERO
        is Boolean -> this.isNotEmpty() && right
        is String -> this.isNotEmpty() && right.isNotEmpty()
        is List<*> -> this.isNotEmpty() && right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun proceed(left: Any): Boolean = when (left) {
        is BigDecimal -> left != D_ZERO
        is BigInteger -> left != ZERO
        is Boolean -> left
        is String -> left.isNotEmpty()
        is List<*> -> left.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $left")
    }

    override fun shortCircuitValue() = false
}


class Or : ShortCircuit, BinaryOperator() {
    override fun BigDecimal.op(right: Any): Any = when (right) {
        is BigDecimal -> this != D_ZERO || right != D_ZERO
        is BigInteger -> this != D_ZERO || right != ZERO
        is Boolean -> this != D_ZERO || right
        is String -> this != D_ZERO || right.isNotEmpty()
        is List<*> -> this != D_ZERO || right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun BigInteger.op(right: Any): Any = when (right) {
        is BigDecimal -> this != ZERO || right != D_ZERO
        is BigInteger -> this != ZERO || right != ZERO
        is Boolean -> this != ZERO || right
        is String -> this != ZERO || right.isNotEmpty()
        is List<*> -> this != ZERO || right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun Boolean.op(right: Any): Any = when (right) {
        is BigDecimal -> this || right != D_ZERO
        is BigInteger -> this || right != ZERO
        is Boolean -> this || right
        is String -> this || right.isNotEmpty()
        is List<*> -> this || right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun String.op(right: Any): Any = when (right) {
        is BigDecimal -> this.isNotEmpty() || right != D_ZERO
        is BigInteger -> this.isNotEmpty() || right != ZERO
        is Boolean -> this.isNotEmpty() || right
        is String -> this.isNotEmpty() || right.isNotEmpty()
        is List<*> -> this.isNotEmpty() || right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun List<*>.op(right: Any): Any = when (right) {
        is BigDecimal -> this.isNotEmpty() || right != D_ZERO
        is BigInteger -> this.isNotEmpty() || right != ZERO
        is Boolean -> this.isNotEmpty() || right
        is String -> this.isNotEmpty() || right.isNotEmpty()
        is List<*> -> this.isNotEmpty() || right.isNotEmpty()
        else -> throw RuntimeException("Unknown Type: $right")
    }

    override fun proceed(left: Any): Boolean = when (left) {
        is BigDecimal -> left == D_ZERO
        is BigInteger -> left == ZERO
        is Boolean -> !left
        is String -> left.isEmpty()
        is List<*> -> left.isEmpty()
        else -> throw RuntimeException("Unknown Type: $left")
    }

    override fun shortCircuitValue() = true
}