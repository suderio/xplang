package net.technearts.xp

import net.technearts.xp.TokenType.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess


var hadError = false

@Throws(IOException::class)
fun main(args: Array<String>) {
    if (args.size > 1) {
        println("Usage: xp [script]")
        exitProcess(64)
    } else if (args.size == 1) {
        runFile(args[0])
    } else {
        runPrompt()
    }
}


@Throws(IOException::class)
private fun runFile(path: String) {
    val bytes: ByteArray = Files.readAllBytes(Paths.get(path))
    runit(String(bytes, Charset.defaultCharset()))
    // Indicate an error in the exit code.
    if (hadError) exitProcess(65)
}

@Throws(IOException::class)
private fun runPrompt() {
    val input = InputStreamReader(System.`in`)
    val reader = BufferedReader(input)
    while (true) {
        print("> ")
        val line = reader.readLine() ?: break
        runit(line)
        hadError = false
    }
}

private fun runit(source: String) {
    val scanner = Scanner(source)
    val tokens: List<Token> = scanner.scanTokens()
    val parser = Parser(tokens)
    val expression = parser.parse()

    // Stop if there was a syntax error.
    if (hadError) return

    println(AstPrinter().print(expression!!))
}

fun error(line: Int, message: String) {
    report(line, "", message)
}

private fun report(
    line: Int, where: String, message: String
) {
    System.err.println(
        "[line $line] Error$where: $message"
    )
    hadError = true
}

fun error(token: Token, message: String?) {
    if (token.type === EOF) {
        report(token.line, " at end", message!!)
    } else {
        report(token.line, " at '" + token.lexeme + "'", message!!)
    }
}