package codes.sepctrum.conf2022.img_analyzer

import java.awt.Color


/**
 * Замещает в двуцветной матрице цвет [foreground] на [background]
 * в тех случаях, если пиксель не включен в блок своего
 * цвета минимальным размером [minwidth] * [minheight]
 * работает с самим [ImageAnaylyzerFacade.image], не делает копий
 * и не возвращает соответственно значений
 */
fun ImageAnaylyzerFacade.cleanNonBlockedPixels2C(
    minwidth: Int,
    minheight: Int = minwidth,
    foreground: Color = Color.BLACK,
    background: Color = Color.WHITE,
) {
    //TODO: вот тут собственно ее реализовать
}
