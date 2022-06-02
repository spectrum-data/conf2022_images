/**
 * В этом файле собраны примеры расширений, которые решают
 * самые очевидные недоразвитости базовой библиотеки
 */

package codes.sepctrum.conf2022.img_analyzer

import java.awt.Color
import java.awt.image.BufferedImage


/**
 * Получить автономный клон изображения, если просто взять subImage
 * то будет просто ссылка на исходное изображение
 */
fun ImageAnaylyzerFacade.clone(): BufferedImage {
    val result = BufferedImage(image.width, image.height, image.type)
    result.graphics.drawImage(image, 0, 0, null)
    return result
}

/**
 * Позволяет вместо `image.getRGB(x, y)` делать просто `image[x, y]`
 * даже не стал в extra помещать - настолько очевидная задача
 */
operator fun BufferedImage.get(x: Int, y: Int): Int = this.getRGB(x, y)

/**
 * Позволяет вместо `image.setRGB(x, y, color.rgb)` делать просто `image[x, y] = Color....`
 * даже не стал в extra помещать - настолько очевидная задача
 */
operator fun BufferedImage.set(x: Int, y: Int, color: Color) = this.setRGB(x, y, color.rgb)
/**
 * Позволяет вместо `image.setRGB(x, y, int)` делать просто `image[x, y] = int
 * даже не стал в extra помещать - настолько очевидная задача
 */
operator fun BufferedImage.set(x: Int, y: Int, colorRGB: Int) = this.setRGB(x, y, colorRGB)
