import net.technearts.xp.runFile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.nio.file.Paths
import kotlin.io.path.pathString

class ExpressionTest {

    @Test
    fun arithmetics() {
        Assertions.assertDoesNotThrow {
            runFile(Paths.get("./src/test/resources/Arithmetics").toRealPath().pathString)
        }
    }
}