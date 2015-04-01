package kotlin.dom.xpath

import org.w3c.dom.Node
import kotlin.dom.xpath.xerces.evaluateImpl


/**
 * @author Sergey Mashkov
 */

public object NullResult : Result

public fun Node.evaluate(expression : String) : Result = evaluateImpl(expression, Type.ANY, false)

public fun Node.evaluateString(expression : String) : ValueResult<String> = evaluate(expression).let { result ->
    if (result is ValueResult<*>) result.toStringResult()
    else if (result is NullResult) throw NullPointerException("The expression $expression returned null")
    else throw IllegalArgumentException("The expression $expression returned non-string result $result")
}

[suppress("UNCHECKED_CAST")]
public fun Node.evaluateNumber(expression : String) : ValueResult<Double> = evaluate(expression).let { result ->
    if (result is ValueResult<*> && result.value is Double) result as ValueResult<Double>
    else if (result is NullResult) throw NullPointerException("The expression $expression returned null")
    else throw IllegalArgumentException("The expression $expression returned non-number result $result")
}

public fun Node.evaluateSnapshot(expression : String, ordered : Boolean = false) : SizedSet = evaluateImpl(expression, Type.SNAPSHOT, ordered) as SizedSet
public fun Node.evaluateIterator(expression : String, ordered : Boolean = false) : Set = evaluateImpl(expression, Type.ITERATOR, ordered) as Set
public fun Node.evaluatePeekSome(expression : String, orderedFirst : Boolean = false) : SizedSet = evaluateImpl(expression, Type.SINGLE, orderedFirst) as SizedSet

public fun ValueResult<*>.toStringResult() : ValueResult<String> = object : ValueResult<String> {
    override val value: String
        get() = this.value.toString()
}

public fun Set.toList() : List<Node> = sequence().toList()


