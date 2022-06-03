package codes.sepctrum.conf2022.images

import conf2022.images.common.get
import conf2022.images.common.set
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.LinkedList


/**
 * Замещает в двуцветной матрице [image] цвет [foreground] на [background]
 * в тех случаях, если пиксель не включен в блок своего
 * цвета минимальным размером [minwidth] * [minheight]
 *
 * Дополнительный контракт.
 * 1. Если встречается пиксель не являющися [foreground] - то он рассматривается тоже как фоновый и никак не замещается
 * 2. Блоком "своего цвета" является непрерывный (без пикселей другого цвета) прямоугольник
 * 3. "Минимальный размер" значит, что если `minwidth=3` и `minheight=3` то блок 3x3 , 4x3, 4x5 подходят, а блоки
 *    2x2, 3x2, 2x3, 10x2 - нет
 *
 * Кейсы для данного метода лежат в `src/test/resources/CleanNonBlockedPixels2CTest
 *
 * > Имя файла содержит две переменных - для `minwidth` и `maxheight`, обязательно!
 */
fun cleanNonBlockedPixels2C(
    image: BufferedImage,
    minwidth: Int,
    minheight: Int = minwidth,
    foreground: Color = Color.BLACK,
    background: Color = Color.WHITE,
) {
    //TODO:собственно надо реализовать
}
