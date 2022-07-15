import conf2022.images.common.exClone
import conf2022.images.common.get
import conf2022.images.common.set
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.random.Random

fun main() {
    val rnd = Random(123)
    for (f in File("src/test/resources/ToBlackAndWhiteFigureTest").listFiles().filter { it.name.startsWith("base") }) {
        val image: BufferedImage = ImageIO.read(f)
        val grayTask = image.exClone()
        for (x in 0..2) {
            for (y in 0..2) {
                if (grayTask[x, y] == Color.BLACK.rgb) {
                    val rdark = rnd.nextInt(10,50)
                    grayTask[x, y] = Color(rdark,rdark,rdark)
                }else{
                    val rlight = rnd.nextInt(210,245)
                    grayTask[x, y] = Color(rlight,rlight,rlight)
                }
            }
        }
        ImageIO.write(grayTask,"png", File(f.parent,f.name.replace("base","gray")))

        val gredTask = image.exClone()
        for (x in 0..2) {
            for (y in 0..2) {
                val deltA = rnd.nextInt(0,70)
                val deltB = rnd.nextInt(0,70)
                val deltM = rnd.nextInt(190,255)
                if (gredTask[x, y] == Color.BLACK.rgb) {
                    gredTask[x, y] = Color(deltM,deltA,deltB)
                }else{
                    gredTask[x, y] = Color(deltA,deltM,deltB)
                }
            }
        }
        ImageIO.write(gredTask,"png", File(f.parent,f.name.replace("base","gred")))

    }
}