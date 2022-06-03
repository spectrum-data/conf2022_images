package conf2022.images.task

import codes.sepctrum.conf2022.images.cleanNonBlockedPixels2C
import conf2022.images.common.ImageTestCaseBase
import conf2022.images.common.ImageTestSpecBase
import java.awt.image.BufferedImage
import java.io.File

internal class CleanNonBlockedPixels2CTest : ImageTestSpecBase(TestCase::class) {
    class TestCase(srcFile: File) : ImageTestCaseBase(::cleanNonBlockedPixels2C.name, srcFile) {
        override fun getTestNameExtension(): String {
            return "${minWidth}x${minHeight}px"
        }

        /**
         * Ожидаемая минимальная ширина в имени файла `xxxxx_W_x.png`
         */
        val minWidth = fileNameVariables[0].toInt()

        /**
         * Ожидаемая минимальная высота в имени файла `xxxxx_x_H.png`
         */
        val minHeight = fileNameVariables[1].toInt()

        override fun execute(target: BufferedImage) {
            cleanNonBlockedPixels2C(target, minWidth, minHeight)
        }
    }
}