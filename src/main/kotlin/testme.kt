package testxpath

import org.w3c.dom.Element
import java.io.File
import kotlin.dom.parseXml
import kotlin.dom.xpath.evaluateIterator
import kotlin.dom.xpath.filterIsElement

fun main(args: Array<String>) {
    parseXml(File(".idea/gradle.xml"))
            .evaluateIterator("//option[contains(@name, 'gradle')]")
            .sequence()
            .filterIsElement()
            .forEach {
                val name = it.getAttribute("name")
                val value = it.getAttribute("value")

                println("$name = $value")
            }
}