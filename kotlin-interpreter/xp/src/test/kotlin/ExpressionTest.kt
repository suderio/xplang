import net.technearts.xp.runFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.io.path.pathString

class ExpressionTest {

    @Test
    fun arithmetics() {
        println("Testing Arithmetic Operators")
        Assertions.assertDoesNotThrow {
            runFile(Paths.get("./src/test/resources/Arithmetics").toRealPath().pathString)
        }
    }

    @Test
    fun comparisons() {
        println("Testing Comparison Operators")
        Assertions.assertDoesNotThrow {
            runFile(Paths.get("./src/test/resources/Comparisons").toRealPath().pathString)
        }
    }

    @Test
    fun lists() {
        println("Testing List Operators")
        Assertions.assertDoesNotThrow {
            runFile(Paths.get("./src/test/resources/Lists").toRealPath().pathString)
        }
    }

    @Test
    fun operators() {
        println("Testing Custom Operators")
        Assertions.assertDoesNotThrow {
            runFile(Paths.get("./src/test/resources/Operators").toRealPath().pathString)
        }
    }
}