package testxpath

import org.jetbrains.kotlin.utils.printAndReturn
import org.w3c.dom.Element
import java.io.File
import kotlin.dom.get
import kotlin.dom.parseXml
import kotlin.dom.toXmlString
import kotlin.dom.xpath.evaluateIterator

fun main(args: Array<String>) {
    parseXml(File(".idea/gradle.xml")).evaluateIterator("//option[contains(@name, 'gradle')]").sequence().filter {it is Element}.forEach {
        if (it is Element) {
            val name = it.getAttribute("name")
            val value = it.getAttribute("value")

            println("$name = $value")
        }
    }
}