package codes.sepctrum.conf2022.images

import codes.sepctrum.conf2022.images.common.get
import codes.sepctrum.conf2022.images.common.set
import java.awt.Color
import java.awt.image.BufferedImage
import java.util.LinkedList


/**
 * Замещает в двуцветной матрице цвет [foreground] на [background]
 * в тех случаях, если пиксель не включен в блок своего
 * цвета минимальным размером [minwidth] * [minheight]
 * работает с самим [ImageAnaylyzerFacade.image], не делает копий
 * и не возвращает соответственно значений
 */
fun cleanNonBlockedPixels2C(
    image: BufferedImage,
    minwidth: Int,
    minheight: Int = minwidth,
    foreground: Color = Color.BLACK,
    background: Color = Color.WHITE,
) {
    //TODO
}
