package codes.sepctrum.conf2022.img_analyzer

import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Класс сознательно пустой, мы его будем обвешивать фичами "снаружи"
 * и еще момент - мы заодно тренируемся писать именно функции прежде всего,
 * не классы - это и с учетом разных стеков важно.
 * Данный класс все, ничего кроме доставки ссылки на изображение он НЕ делает
 */
class ImageAnaylyzerFacade(val image: BufferedImage)

/**
 * Вариант псевдо-свойства, чтобы можно было img.extra.myFunc, а не прямо
 * обвешивать расширениями Image
 */
val BufferedImage.extra get() = ImageAnaylyzerFacade(this)

