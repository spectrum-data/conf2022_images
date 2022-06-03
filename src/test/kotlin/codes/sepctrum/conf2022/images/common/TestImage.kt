package codes.sepctrum.conf2022.images.common

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO


class TestImage private constructor(private val caseImage: BufferedImage) {

    val partWidth = ((caseImage.width - 1) / 2)

    val target: BufferedImage by lazy {
        caseImage.getSubimage(0, 0, partWidth, caseImage.height).exClone()
    }

    private val expected: BufferedImage by lazy {
        caseImage.getSubimage(partWidth + 1, 0, partWidth, caseImage.height)
    }


    data class ImageTestResult(
        val resultImage: BufferedImage,
        val diffStat: DiffStat,
    )

    fun buildResult(): ImageTestResult = result
    private val result by lazy {
        val diffStat = diffWith2C(target, expected)
        val resultImage =
            BufferedImage(
                caseImage.width + caseImage.width + 1,
                caseImage.height, BufferedImage.TYPE_INT_ARGB
            )
        val graph2 = resultImage.createGraphics()
        graph2.drawImage(caseImage, 0, 0, null)
        graph2.color = Color.RED
        graph2.drawLine(caseImage.width, 0, caseImage.width, caseImage.height - 1)
        graph2.drawImage(target, caseImage.width + 1, 0, null)
        ImageTestResult(resultImage, diffStat)
    }


    companion object {
        fun load(file: File): TestImage {
            require(file.exists())
            require(!file.isDirectory)
            require(file.name.endsWith(".png"))
            val image = ImageIO.read(file)
            return load(image)
        }

        fun load(image: BufferedImage): TestImage {
            // тестовое изображение должно быть не менее трех пикселей по ширине
            // и ширина нечетная - слева исходник, справа - ожидаемое,
            // по середине линия разграничения
            require(image.width >= 3 && image.width % 2 == 1)
            // на всякий случай обходим вырожденный случай искуственно пустого файла
            require(image.height > 0)
            return TestImage(image)
        }
    }
}