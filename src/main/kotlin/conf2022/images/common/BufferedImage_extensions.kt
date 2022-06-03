/**
 * В этом файле собраны примеры расширений, которые решают
 * самые очевидные недоразвитости базовой библиотеки
 */

package conf2022.images.common

import java.awt.Color
import java.awt.image.BufferedImage


/**
 * Получить автономный клон изображения, если просто взять subImage
 * то будет просто ссылка на исходное изображение
 *
 * //NEWBE: это первое встреченное Вами "расширение" - если у Вас в языке их нет, поясняю
 * концепцию - это "псевдо-метод", который просто внешняя функция которую можно было
 * написать как `fun exClone(image: BufferedImage)`, но мы для сахара делаем ее
 * как-бы методом - снаружи мы теперь можем использовать как `image.exClone()`, а внутри "видеть" `image` как `this`
 * > при этом это все также внешняя функция, и например она не может видеть `private` члены класса
 *
 */
fun BufferedImage.exClone(): BufferedImage {
    val result = BufferedImage(width, height, type)
    result.graphics.drawImage(this, 0, 0, null)
    return result
}

/**
 * Позволяет вместо `image.getRGB(x, y)` делать просто `image[x, y]`
 * даже не стал в extra помещать - настолько очевидная задача
 *
 * * //NEWBE: в Koltin есть не сколько перекрытие, сколько определение операторов
 * и его можно сделать и как расширение, например оператор `get`
 * повзоляет использовать класс, для которого он определен, как-будто это "массив" или "карта"
 * с использованием квадратных скобок `image[x, y]`
 *
 *  //NEWBE: в Kotlin функции можно определить как полным синтаксисом
 *  ```
 *  fun x() : Int {  return 1 }
 *  ```
 *  так и сжатым
 * ```
 * fun x() : Int = 1
 * ```
 * или даже с автовыводом типа
 * ```
 * fun x() = 1
 * ```
 * обычно это используется для каких-то простеньких функций из одного оператора
 */
operator fun BufferedImage.get(x: Int, y: Int): Int = this.getRGB(x, y)

/**
 * Позволяет вместо `image.setRGB(x, y, color.rgb)` делать просто `image[x, y] = Color....`
 * даже не стал в extra помещать - настолько очевидная задача
 *
 */
operator fun BufferedImage.set(x: Int, y: Int, color: Color) = this.setRGB(x, y, color.rgb)

/**
 * Позволяет вместо `image.setRGB(x, y, int)` делать просто `image[x, y] = int
 * даже не стал в extra помещать - настолько очевидная задача
 * //NEWBE: Java и Kotlin поддерживает перегрузку методов при использовании разных типов аргумента,
 * в данном случае Color и Int - компилятор подставит сам нужную реализацию
 */
operator fun BufferedImage.set(x: Int, y: Int, colorRGB: Int) = this.setRGB(x, y, colorRGB)
