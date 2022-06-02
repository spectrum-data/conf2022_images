package codes.sepctrum.conf2022.img_analyzer

import java.io.File


class CleanNonBlockedPixels2CTestCase(srcFile: File) : ImageTestCaseBase("remove_not_blocked", srcFile) {
    override val name by lazy { "remove_not_blocked ${srcFile.name} (${this.minWidth}*${this.minHeight}px)" }

    /**
     * Ожидаемая минимальная ширина в имени файла `xxxxx_W_x.png`
     */
    val minWidth by lazy { srcFile.name.split(".")[0].split("_")[1].toInt() }

    /**
     * Ожидаемая минимальная высота в имени файла `xxxxx_x_H.png`
     */
    val minHeight by lazy { srcFile.name.split(".")[0].split("_")[2].toInt() }


    override fun execute() {
        srcImage.extra.cleanNonBlockedPixels2C(minWidth, minHeight)
    }
}
