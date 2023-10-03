package net.technearts.xp.lox

import net.technearts.xp.lox.Interpreter.RuntimeError
import net.technearts.xp.lox.TokenType.*
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.system.exitProcess

private val interpreter = Interpreter()
var hadError = false
var hadRuntimeError = false

@Command(name = "xp", mixinStandardHelpOptions = true)
class MainCommand : Runnable {

    @Option(names = ["-e", "--expression"], description = ["Expression"])
    var expr: String? = null

    @Option(names = ["-f", "--file"], description = ["File"])
    var file: File? = null
    override fun run() {
        if (file != null) {
            runFile(file!!.path)
        } else if (expr != null) {
            runCommand(expr!!)
        } else {
            main()
        }
    }

}

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
    val statements = parser.parse()

    // Stop if there was a syntax error.
    if (hadError) return

    interpreter.interpret(statements)
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

fun runtimeError(error: RuntimeError) {
    System.err.println(
        """
            ${error.message}
            [line ${error.token.line}]
            """.trimIndent()
    )
    hadRuntimeError = true
}