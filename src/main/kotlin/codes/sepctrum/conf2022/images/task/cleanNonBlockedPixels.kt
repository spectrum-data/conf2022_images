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
    // кэш пикселей, которые могут быть началом блока
    val blockStarters = LinkedList<Pair<Int, Int>>()
    val fg = foreground.rgb
    val bg = background.rgb
    // обходим по строкам
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            val pixel = image[x, y]
            if (pixel != fg) {
                continue
            }
            // проверим не является ли точка началом блока
            var isBlockStarter = false
            // только если блок вообще еще помещается
            if (x + minwidth <= image.width && y + minheight <= image.height) {
                // только если диагональная точка тоже "черная"
                if (image[x + minwidth - 1, y + minheight - 1] == fg) {
                    // сразу предположим что это блок
                    isBlockStarter = true
                    // то проверим что все точки в этом прямоугольнике черные
                    for (bx in x until x + minwidth) {
                        for (by in y until y + minheight) {
                            val bpx = image[bx, by]
                            if(bpx != fg){
                                isBlockStarter = false
                                break
                            }
                        }
                    }
                }
            }



            // точка точно в блоке если она начинает этот блок
            if (isBlockStarter) {
                // занесем точку в реестр точек начал блока
                // причем всегда ставим в начало - так как нам их обходить надо будет потом в обратном порядке
                blockStarters.addFirst(x to y)
                continue
            }

            var isInBlock = false
            // также точка в блоке - если есть блок, который ее содержит
            // обходить выгодно от последнего и тут лучше обходить индексами обратно,
            // а не создавать
            for(blk in blockStarters) {
                // если уже не проходим по вертикальной части
                // то все, из цикла выходим сразу
                if(blk.second + minheight - 1 < y) {
                    break
                }
                if(
                    x >= blk.first && (blk.first + minwidth) > x
                ) {
                    isInBlock = true
                    break
                }
            }

            // и вот если точка и не в блоке и не не начало блока, то собственно переводим в фоновый цвет
            if (!isInBlock) {
                image[x, y] = bg
            }
        }
    }
}
