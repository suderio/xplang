package net.technearts.xp

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess

var hadError = false
var hadRuntimeError = false
private val interpreter = Interpreter()
@Throws(IOException::class)
fun main() {
    val input = InputStreamReader(System.`in`)
    val reader = BufferedReader(input)
    while (true) {
        print("> ")
        val line = reader.readLine() ?: break
        runit(line)
        hadError = false
    }
}


@Throws(IOException::class)
fun runFile(path: String) {
    val bytes: ByteArray = Files.readAllBytes(Paths.get(path))
    runit(String(bytes, Charset.defaultCharset()))
    // Indicate an error in the exit code.
    if (hadError) exitProcess(65)
    if (hadRuntimeError) exitProcess(70)
}

@Throws(IOException::class)
fun runCommand(expr: String) {
    runit(expr)
}

private fun runit(source: String) {
    val scanner = Scanner(source)
    val tokens: List<Token> = scanner.scanTokens()
    val parser = Parser(tokens)
    val expressions = parser.parse()

    // Stop if there was a syntax error.
    if (hadError) return

    interpreter.interpret(expressions)
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

fun runtimeError(error: Interpreter.RuntimeError) {
    System.err.println(
        """
            ${error.message}
            [line ${error.token.line}]
            """.trimIndent()
    )
    hadRuntimeError = true
}