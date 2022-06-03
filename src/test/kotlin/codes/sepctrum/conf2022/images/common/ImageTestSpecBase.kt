package codes.sepctrum.conf2022.images.common

import io.kotest.core.spec.style.FunSpec
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openjdk.jmh.annotations.*
import java.io.File
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO
import kotlin.reflect.KClass

/**
 * Базовый клосс для отдельного спеца (набора тестов для отдельной функции)
 */
@State(Scope.Benchmark)
@Warmup(iterations = 0)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
abstract class ImageTestSpecBase(private val caseClazz: KClass<out ImageTestCaseBase>) : FunSpec() {
    private val imagesDir: File = File("src/test/resources/${this::class.simpleName!!.replace("_jmhType","")}")

    init {
        require(imagesDir.exists()) { "directory ${imagesDir} not exists!" }
        require(imagesDir.isDirectory)
        require(imagesDir.listFiles()?.any { it.name.endsWith(".png") && !it.isDirectory } ?: false)
    }

    private val caseCtor = caseClazz.java.getConstructor(File::class.java)
    val cases = imagesDir.listFiles()!!.filter { it.name.endsWith(".png") && !it.isDirectory }.map {
        caseCtor.newInstance(it)
    }

    init {
        this.context("main cases") {
            for (case in cases.filter { !it.isBenchMark }) {
                test(case.name) {
                    case.executeTest()
                }
            }
        }
        this.context("debug") {
            for (case in cases.filter { it.isBenchMark }) {
                test("render ${case.name}") {
                    case.execute(case.rawImage)
                    withContext(Dispatchers.IO) {
                        ImageIO.write(case.rawImage, "png", File(case.resultDir, case.srcFile.name.replace(".png",".debug.png")))
                    }
                }
            }
        }
    }

    @Benchmark
    fun usualCasesAsBenchmarks() {
        for (case in cases.filter { !it.isBenchMark }) {
            case.executeTest()
        }
    }

    @Benchmark
    fun specialBenchmaks() {
        for (case in cases.filter { !it.isBenchMark }) {
            case.executeBenchMark()
        }
    }
}