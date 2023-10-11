package net.technearts.xp

abstract class Expr {
    interface Visitor<R> {
        fun visitBinaryExpr(expr: Expr.Binary): R
        fun visitGroupingExpr(expr: Expr.Grouping): R
        fun visitLiteralExpr(expr: Expr.Literal): R
        fun visitUnaryExpr(expr: Expr.Unary): R
        fun visitVariableExpr(expr: Expr.Variable): R
        fun visitCallExpr(expr: Expr.Call): R
    }
    abstract fun <R> accept(visitor: Expr.Visitor<R>): R
    class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }
    }

    class Grouping(val expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitGroupingExpr(this)
        }
    }

    class Literal(val value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }
    }

    class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }
    }


    class Call(val callee: Expr, val argument: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this)
        }
    }


    class Variable(val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this)
        }
    }

}