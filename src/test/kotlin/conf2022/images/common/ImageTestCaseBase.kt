package conf2022.images.common

import io.kotest.assertions.fail
import io.kotest.assertions.withClue
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Базовый класс, описывающий процедуру тестирования отдельного файла
 * среди тестов отдельной функции
 */
abstract class ImageTestCaseBase(val testName: String, val srcFile: File) {
    open val name by lazy {
        "${testName} ${srcFile.name}${
            getTestNameExtension().takeIf { it.isNotBlank() }?.let { " ($it)" } ?: ""
        }"
    }

    /**
     * Переменные, вшитые в имя файла кейса `some001_X_Y_Z.png` -> [X, Y, Z]
     */
    protected val fileNameVariables by lazy { srcFile.name.split(".")[0].split("_").drop(1) }


    open fun getTestNameExtension(): String = ""

    val resultDir =
        File("build/reports/images/$testName/").also {
            if (it.exists()) {
                it.deleteRecursively()
            }
            it.mkdirs()
        }


    val rawImage by lazy { ImageIO.read(srcFile) }

    /**
     * Изображение, содержащее тестовый кейс
     */
    val testCaseImage: TestImage by lazy { TestImage.load(rawImage) }

    val isBenchMark = srcFile.name.contains(".benchmark.")


    /**
     * Собственно вызов, который нужно перекрыть - тест
     * меняет свободно изображение `target` на свое усмотрение
     */
    abstract fun execute(target: BufferedImage)

    fun executeTest() {
        require(!isBenchMark)
        withClue(srcFile.name) {
            execute(testCaseImage.target)
            val testResult = testCaseImage.buildResult()
            resultDir.mkdirs()
            val resultFile = File(resultDir, "${srcFile.name.split(".")[0]}_result.png")
            ImageIO.write(testResult.resultImage, "png", resultFile)
            if (!testResult.diffStat.isMatched()) {
                fail("result image not match expected, look at ${resultFile}: ${testResult.diffStat}")
            }
        }
    }


    fun executeBenchMark() {
        val image = ImageIO.read(srcFile)
        execute(image)
    }
}

