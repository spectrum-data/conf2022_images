package codes.sepctrum.conf2022.img_analyzer

import io.kotest.assertions.fail
import io.kotest.assertions.withClue
import io.kotest.matchers.shouldBe
import java.io.File
import javax.imageio.ImageIO

abstract class ImageTestCaseBase(val testName: String, val srcFile: File) {
    open val name by lazy { "${testName} ${srcFile.name}" }

    val resultDir =
        File("tmp/$testName/").also {
            if (it.exists()) {
                it.deleteRecursively()
            }
            it.mkdirs()
        }

    /**
     * Изображение, содержащее тестовый кейс
     */
    val srcCaseImage by lazy { ImageIO.read(srcFile) }

    /**
     * Участок изображения для переработки (левая половина до границы)
     */
    val srcImage by lazy {
        srcCaseImage.getSubimage(
            0, 0, (srcCaseImage.width - 1) / 2, srcCaseImage.height
        ).extra.clone()
    }

    /**
     * Ожидаемый результат (правая половина до границы)
     */
    val expectedImage by lazy {
        srcCaseImage.getSubimage(
            (srcCaseImage.width - 1) / 2 + 1, 0, (srcCaseImage.width - 1) / 2, srcCaseImage.height
        )
    }

    abstract fun execute()

    fun executeTest() {
        withClue(srcFile.name) {
            execute()
            srcImage.height shouldBe expectedImage.height
            srcImage.width shouldBe expectedImage.width
            val testResult = buildImageTestResult(srcCaseImage, srcImage)
            resultDir.mkdirs()
            val resultFile = File(resultDir, "${srcFile.name.split(".")[0]}_result.png")
            ImageIO.write(testResult.resultImage, "png", resultFile)
            if (!testResult.diffStat.isMatched()) {
                fail("result image not match expected, look at ${resultFile}: ${testResult.diffStat}")
            }
        }
    }
}
