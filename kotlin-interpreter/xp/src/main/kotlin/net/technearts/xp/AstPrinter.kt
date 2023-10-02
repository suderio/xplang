package net.technearts.xp


internal class AstPrinter : Expr.Visitor<String> {
    fun print(expr: Expr): String {
        return expr.accept(this)
    }

    override fun visitAssignExpr(expr: Expr.Assign?): String {
        TODO("Not yet implemented")
    }

    override fun visitBinaryExpr(expr: Expr.Binary?): String {
        return parenthesize(
            expr!!.operator.lexeme,
            expr.left, expr.right
        )
    }

    override fun visitCallExpr(expr: Expr.Call?): String {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expr: Expr.Grouping?): String {
        return parenthesize("group", expr!!.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal?): String {
        return expr?.value.toString()
    }

    override fun visitLogicalExpr(expr: Expr.Logical?): String {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expr: Expr.This?): String {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpr(expr: Expr.Unary?): String {
        return parenthesize(expr!!.operator.lexeme, expr.right)
    }

    override fun visitVariableExpr(expr: Expr.Variable?): String {
        TODO("Not yet implemented")
    }

    private fun parenthesize(name: String, vararg exprs: Expr): String {
        val builder = StringBuilder()
        builder.append("(").append(name)
        for (expr in exprs) {
            builder.append(" ")
            builder.append(expr.accept<String>(this))
        }
        builder.append(")")
        return builder.toString()
    }

}