package net.technearts.xp.lox


abstract class Expr {

    interface Visitor<R> {
        fun visitAssignExpr(expr: Assign?): R
        fun visitBinaryExpr(expr: Binary?): R
        fun visitCallExpr(expr: Call?): R
        fun visitGroupingExpr(expr: Grouping?): R
        fun visitLiteralExpr(expr: Literal?): R
        fun visitLogicalExpr(expr: Logical?): R
        fun visitThisExpr(expr: This?): R
        fun visitUnaryExpr(expr: Unary?): R
        fun visitVariableExpr(expr: Variable?): R
    }

    abstract fun <R> accept(visitor: Visitor<R>): R


    class Assign(val name: Token, val value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }

    }

    class Binary(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }
    }

    class Call(val callee: Expr, val paren: Token, val arguments: List<Expr>) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this)
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

    class Logical(val left: Expr, val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogicalExpr(this)
        }
    }

    class This(val keyword: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitThisExpr(this)
        }
    }

    class Unary(val operator: Token, val right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }
    }

    class Variable(val name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this)
        }
    }
}