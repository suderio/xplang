package net.technearts.xp.lox

abstract class Stmt {
    interface Visitor<R> {
        fun visitExpressionStmt(stmt: Expression): R
        //fun visitFunctionStmt(stmt: Function?): R
        fun visitPrintStmt(stmt: Print): R
        fun visitEmptyStmt(stmt: Empty): R

    }

    // Nested Stmt classes here...
    abstract fun <R> accept(visitor: Visitor<R>): R

    class Expression(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpressionStmt(this)
        }
    }

    class Print(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrintStmt(this)
        }
    }

    class Empty(val expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitEmptyStmt(this)
        }
    }


}