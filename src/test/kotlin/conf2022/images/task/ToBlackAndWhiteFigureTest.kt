package conf2022.images.task

import conf2022.images.common.BlackWhiteOptions
import conf2022.images.common.ImageTestCaseBase
import conf2022.images.common.ImageTestSpecBase
import java.awt.image.BufferedImage
import java.io.File

internal class ToBlackAndWhiteFigureTest : ImageTestSpecBase(TestCase::class) {
    class TestCase(srcFile: File) : ImageTestCaseBase(::toBlackAndWhiteFigure.name, srcFile) {
        override fun getTestNameExtension(): String {
            return "${backgroundThreshHold}"
        }

        /**
         * Ожидаемая минимальная ширина в имени файла `xxxxx_W_x.png`
         */
        val backgroundThreshHold: Double = fileNameVariables.getOrElse(0) { "0.5" }.toDouble()


        override fun execute(target: BufferedImage) {
            toBlackAndWhiteFigure(target, BlackWhiteOptions(backgroundThreshHold = backgroundThreshHold))
        }
    }
}
