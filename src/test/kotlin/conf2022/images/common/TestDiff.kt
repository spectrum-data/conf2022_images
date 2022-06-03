/**
 * Тут расширения базового API, предназначенные для тестов
 */
package conf2022.images.common

import java.awt.Color
import java.awt.image.BufferedImage

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
 * [addedColor] - цвет пикселей изображения которые есть в исходном и нет в [base]
 * [removedColor] - цвет пикселей изображения которых нет в исходном но есть в [base]
 * [missColor] - цвет пикселей, которые имеют в исходном любой не стандартный цвет (не [fgColor] и не [bgColor])
 * Понятно, что при наложении x+width, y+width могут пересечь границу изображения
 * реакции две игнорирорвать или ошибка, регулирется [overflowBehavior]
 * если [otherImg] меньше чем исходное - остаток не сверяется
 * если [otherImg] больше исходного то зависит от [overflowBehavior]
 *
 * Ограничения
 * [x] должен входить в исходное изображения
 * [y] должен входить в исходное изображения
 * [width] должен входить в [base]
 * [height] должен входить в [base]
 * все цвета должны быть разными
 *
 * Внимание! При формаировании DIFF не делает клона,
 * переиписывает цвета на сомом изображении
 *
 * returns
 */
fun diffWith2C(
    current: BufferedImage,
    base: BufferedImage,
    /**
     * Позиция x, с которой надо начинать "прикладывать" изображение с которым сравнивается
     */
    x: Int = 0,
    /**
     * Позиция y, с которой надо начинать "прикладывать" изображение с которым сравнивается
     */
    y: Int = 0,
    width: Int = base.width,
    height: Int = base.height,
    fgColor: Color = Color.BLACK,
    bgColor: Color = Color.WHITE,
    addedColor: Color = Color.BLUE,
    removedColor: Color = Color.RED,
    missColor: Color = Color.YELLOW,
    overflowBehavior: DiffOverflowBehavior = DiffOverflowBehavior.IGNORE
): DiffStat {
    require(x >= 0 && x < current.width) {
        "x ${x} exceed size ${current.width}"
    }
    require(y >= 0 && y < current.height) {
        "y ${y} exceed size ${current.height}"
    }
    require(width >= 0 && width <= base.width) {
        "width ${width} exceed size ${base.width}"
    }
    require(height <= base.height) {
        "height ${height} exceed size ${base.height}"
    }
    val allColors = listOf(fgColor, bgColor, addedColor, removedColor, missColor)
    require(allColors.distinct().count() == 5) {
        "есть пересекающиеся цвета в настройках ${allColors.joinToString()}"
    }
    // а вот высоты адаптируем
    var resolvedWidth = minOf(width, base.width)
    var resolvedHeight = minOf(height, base.height)

    if (x + resolvedWidth - 1 > current.width && overflowBehavior == DiffOverflowBehavior.ERROR) {
        error("expected max x x+width-1 ($resolvedWidth) exceed size ${current.width}")
    } else {
        resolvedWidth = minOf(x + resolvedWidth, current.width) - x
    }

    if (y + resolvedHeight - 1 > current.height && overflowBehavior == DiffOverflowBehavior.ERROR) {
        error("expected max y y+height-1 ($resolvedHeight) exceed size ${current.height}")
    } else {
        resolvedHeight = minOf(y + resolvedHeight, current.height) - x
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
            val resultPixel = current[sx, sy]
            val expectedPixel = base[ox, oy]
            checked++
            when {
                resultPixel == expectedPixel -> matched++
                // очистили что не надо было очищать - ставим красное
                resultPixel == bgi -> current[sx, sy] = removedColor.also { removed++ }
                // наоборот не убрали что должны были убрать - ставим синее
                resultPixel == fgi -> current[sx, sy] = addedColor.rgb.also { added++ }
                // какой-то левый цвет - меняем его на желтый - у нас ничего кроме черного и белого не должно было быть
                else -> current[sx, sy] = missColor.also { missed++ }
            }
        }
    }

    return DiffStat(
        checked = checked, matched = matched, added = added, removed = removed, missed = missed,
    )
}
