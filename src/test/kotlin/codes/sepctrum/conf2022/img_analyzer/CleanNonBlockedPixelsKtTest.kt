package codes.sepctrum.conf2022.img_analyzer

import io.kotest.core.spec.style.StringSpec
import org.openjdk.jmh.annotations.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.concurrent.TimeUnit

internal class CleanNonBlockedPixelsKtTest : StringSpec() {

    init {
        // получим все кейсы из тестовых файлов
        val cases = File("src/test/resources/remove_not_blocked")
            .listFiles()
            .filter {
                it.name.endsWith(".png")
            }.map {
                CleanNonBlockedPixels2CTestCase(it)
            }
        // создадим тесты для всех кейсов
        for (case in cases) {
            // используется расширение kotest для StringSpec, что строка может использоваться
            // как имя для теста
            case.name {
                case.executeTest()
            }
        }
    }
}


@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
class CleanNonBlockedPixelsKtBenchMark {
    val src: BufferedImage = CleanNonBlockedPixels2CTestCase(File("src/test/resources/remove_not_blocked/ex001_3_3.png")).srcImage
    lateinit var wrk: BufferedImage
    @Setup(Level.Invocation)
    fun setUp() {
        wrk = src.getSubimage(0, 0, src.width, src.height)
    }
    @Benchmark
    fun executeBlockClean() {
        wrk.extra.cleanNonBlockedPixels2C(3, 3)
    }
}
