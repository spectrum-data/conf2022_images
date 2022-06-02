/**
 * Тут расширения базового API, предназначенные для тестов
 */
package codes.sepctrum.conf2022.img_analyzer

import io.kotest.assertions.fail
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File

enum class DiffOverflowBehavior {
    IGNORE,
    ERROR
}

data class DiffStat(
    val checked: Int,
    val matched: Int,
    val added: Int,
    val removed: Int,
    val missed: Int
) {
    fun isMatched() = matched == checked
}

/**
 * Формирует DIFF между двумя 2-цветными изображениями
 * В самом простом варианте `image.extra.diffWith2C(otherImg)`
 * сравнит 2 изображения начиная с позиций 0, 0
 * [x],[y] регулируют позиции начала сравнения в исходном изображении
 * [width],[height] - сколько брать из другого изображения
 * [fgColor],[bgColor] - цвета изображения и фона
 * [addedColor] - цвет пикселей изображения которые есть в исходном и нет в [other]
 * [removedColor] - цвет пикселей изображения которых нет в исходном но есть в [other]
 * [missColor] - цвет пикселей, которые имеют в исходном любой не стандартный цвет (не [fgColor] и не [bgColor])
 * Понятно, что при наложении x+width, y+width могут пересечь границу изображения
 * реакции две игнорирорвать или ошибка, регулирется [overflowBehavior]
 * если [otherImg] меньше чем исходное - остаток не сверяется
 * если [otherImg] больше исходного то зависит от [overflowBehavior]
 *
 * Ограничения
 * [x] должен входить в исходное изображения
 * [y] должен входить в исходное изображения
 * [width] должен входить в [other]
 * [height] должен входить в [other]
 * все цвета должны быть разными
 *
 * Внимание! При формаировании DIFF не делает клона,
 * переиписывает цвета на сомом изображении
 *
 * returns
 */
fun ImageAnaylyzerFacade.diffWith2C(
    other: BufferedImage,
    /**
     * Позиция x, с которой надо начинать "прикладывать" изображение с которым сравнивается
     */
    x: Int = 0,
    /**
     * Позиция y, с которой надо начинать "прикладывать" изображение с которым сравнивается
     */
    y: Int = 0,
    width: Int = other.width,
    height: Int = other.height,
    fgColor: Color = Color.BLACK,
    bgColor: Color = Color.WHITE,
    addedColor: Color = Color.BLUE,
    removedColor: Color = Color.RED,
    missColor: Color = Color.YELLOW,
    overflowBehavior: DiffOverflowBehavior = DiffOverflowBehavior.IGNORE
): DiffStat {
    require(x >= 0 && x < image.width) {
        "x ${x} exceed size ${image.width}"
    }
    require(y >= 0 && y < image.height) {
        "y ${y} exceed size ${image.height}"
    }
    require(width >= 0 && width <= other.width) {
        "width ${width} exceed size ${other.width}"
    }
    require(height <= other.height) {
        "height ${height} exceed size ${other.height}"
    }
    val allColors = listOf(fgColor, bgColor, addedColor, removedColor, missColor)
    require(allColors.distinct().count() == 5) {
        "есть пересекающиеся цвета в настройках ${allColors.joinToString()}"
    }
    // а вот высоты адаптируем
    var resolvedWidth = minOf(width, other.width)
    var resolvedHeight = minOf(height, other.height)

    if (x + resolvedWidth - 1 > image.width && overflowBehavior == DiffOverflowBehavior.ERROR) {
        error("expected max x x+width-1 ($resolvedWidth) exceed size ${image.width}")
    } else {
        resolvedWidth = minOf(x + resolvedWidth, image.width) - x
    }

    if (y + resolvedHeight - 1 > image.height && overflowBehavior == DiffOverflowBehavior.ERROR) {
        error("expected max y y+height-1 ($resolvedHeight) exceed size ${image.height}")
    } else {
        resolvedHeight = minOf(y + resolvedHeight, image.height) - x
    }

    var checked = 0
    var matched = 0
    var added = 0
    var removed = 0
    var missed = 0
    val fgi = fgColor.rgb
    val bgi = bgColor.rgb
    for (ox in 0 until resolvedWidth) {
        for (oy in 0 until resolvedHeight) {
            val sx = ox - x
            val sy = oy - y
            val resultPixel = image[sx, sy]
            val expectedPixel = other[ox, oy]
            checked++
            when {
                resultPixel == expectedPixel -> matched++
                // очистили что не надо было очищать - ставим красное
                resultPixel == bgi -> image[sx, sy] = removedColor.also { removed++ }
                // наоборот не убрали что должны были убрать - ставим синее
                resultPixel == fgi -> image[sx, sy] = addedColor.rgb.also { added++ }
                // какой-то левый цвет - меняем его на желтый - у нас ничего кроме черного и белого не должно было быть
                else -> image[sx, sy] = missColor.also { missed++ }
            }
        }
    }

    return DiffStat(
        checked = checked, matched = matched, added = added, removed = removed, missed = missed,
    )


}

data class ImageTestResult(
    val resultImage: BufferedImage,
    val diffStat: DiffStat,
)

fun buildImageTestResult(testCaseImage: BufferedImage, testResultImage: BufferedImage): ImageTestResult {
    val expectedImage = testCaseImage.getSubimage(
        (testCaseImage.width - 1) / 2 + 1, 0, (testCaseImage.width - 1) / 2, testCaseImage.height
    )
    val diffStat = testResultImage.extra.diffWith2C(expectedImage)
    val resultImage =
        BufferedImage(
            testCaseImage.width + testResultImage.width + 1,
            testCaseImage.height, BufferedImage.TYPE_INT_ARGB
        )
    val graph2 = resultImage.createGraphics()
    graph2.drawImage(testCaseImage, 0, 0, null)
    graph2.color = Color.RED
    graph2.drawLine(testCaseImage.width, 0, testCaseImage.width, testCaseImage.height - 1)
    graph2.drawImage(testResultImage, testCaseImage.width + 1, 0, null)
    return ImageTestResult(resultImage, diffStat)
}
