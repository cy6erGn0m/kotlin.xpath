# kotlin.xpath
Kotlin bindings for XPath implementation

This is a typesafe wrapper library for XPath so no more dummy unknown objects and magic numbers

# Example
In my .idea directory I have gradle.xml file that looks like
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="GradleSettings">
    <option name="linkedExternalProjectsSettings">
      <GradleProjectSettings>
        <option name="distributionType" value="LOCAL" />
        <option name="externalProjectPath" value="$PROJECT_DIR$" />
        <option name="gradleHome" value="/usr/java/gradle-2.3" />
        <option name="gradleJvm" value="1.6" />
        <option name="modules">
          <set>
            <option value="$PROJECT_DIR$" />
          </set>
        </option>
      </GradleProjectSettings>
    </option>
  </component>
</project>
```

If I want to get gradle options I could easily query it

```kotlin
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
```
