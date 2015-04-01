package kotlin.dom.xpath.xerces

import kotlin.dom.xpath.*
import com.sun.org.apache.xpath.internal.domapi.XPathEvaluatorImpl
import org.w3c.dom.Node
import org.w3c.dom.xpath.XPathResult
import java.util.AbstractList

/**
 * @author Sergey Mashkov
 */

[suppress("UNNECESSARY_SAFE_CALL")]
fun Node.evaluateImpl(expression : String, type : Type, ordered : Boolean) =
        XPathEvaluatorImpl(getOwnerDocument()).createExpression(expression, null).evaluate(this, type.toShortCode(ordered), null)?.let {
            if (it is XPathResult) it.route() else NullResult
        } ?: NullResult

private fun Type.toShortCode(ordered : Boolean) = when {
    this is Type.ITERATOR && ordered -> XPathResult.ORDERED_NODE_ITERATOR_TYPE
    this is Type.ITERATOR -> XPathResult.UNORDERED_NODE_ITERATOR_TYPE
    this is Type.SNAPSHOT && ordered -> XPathResult.ORDERED_NODE_SNAPSHOT_TYPE
    this is Type.SNAPSHOT -> XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE
    this is Type.SINGLE && ordered -> XPathResult.FIRST_ORDERED_NODE_TYPE
    this is Type.SINGLE -> XPathResult.ANY_UNORDERED_NODE_TYPE
    this is Type.ANY -> XPathResult.ANY_TYPE
    else -> throw IllegalArgumentException()
}

private class SnapshotView(val result : XPathResult) : AbstractList<Node>() {
    override fun size(): Int = result.getSnapshotLength()
    override fun get(index: Int): Node? = if (index in indices) result.snapshotItem(index) else throw IndexOutOfBoundsException("index should be in ${indices}: $index")
}

private class SnapshotResult(val result : XPathResult, override val ordered : Boolean) : SizedSet, Result {
    override val size: Int
        get() = result.getSnapshotLength()

    override fun sequence(): Sequence<Node> = SnapshotView(result).sequence()
}

private class IteratorResult(val result : XPathResult, override val ordered : Boolean) : kotlin.dom.xpath.Set, Result {
    override fun sequence(): Sequence<Node> = kotlin.sequence {result.iterateNext()}
}

private class SomeNodeResult(val result : XPathResult, override val ordered : Boolean) : SizedSet, Result {
    val node = result.getSingleNodeValue()

    override fun sequence(): Sequence<Node> = if (node == null) sequenceOf() else sequenceOf(node)

    override val size: Int
        get() = if (node == null) 0 else 1
}

private class StringResult(val result : XPathResult) : ValueResult<String>, Result {
    override val value: String
        get() = result.getStringValue()
}

private class NumberResult(val result : XPathResult) : ValueResult<Double>, Result {
    override val value: Double
        get() = result.getNumberValue()
}

private class UnknownResult(val result : XPathResult) : ValueResult<XPathResult>, Result {
    override val value: XPathResult
        get() = result
}

private fun XPathResult.route() : Result = when (getResultType()) {
    XPathResult.UNORDERED_NODE_SNAPSHOT_TYPE -> SnapshotResult(this, false)
    XPathResult.ORDERED_NODE_SNAPSHOT_TYPE -> SnapshotResult(this, true)
    XPathResult.UNORDERED_NODE_ITERATOR_TYPE -> IteratorResult(this, false)
    XPathResult.ORDERED_NODE_ITERATOR_TYPE -> IteratorResult(this, true)
    XPathResult.ANY_UNORDERED_NODE_TYPE -> SomeNodeResult(this, false)
    XPathResult.FIRST_ORDERED_NODE_TYPE -> SomeNodeResult(this, true)
    XPathResult.STRING_TYPE -> StringResult(this)
    XPathResult.NUMBER_TYPE -> NumberResult(this)
    else -> UnknownResult(this)
}
