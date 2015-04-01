package kotlin.dom.xpath

import org.w3c.dom.Node

/**
 * @author Sergey Mashkov
 */

public trait Result

public trait Set {
    val ordered : Boolean

    fun sequence() : Sequence<Node>
}

public trait Sized {
    val size : Int
}

public trait SizedSet : Set, Sized

public trait ValueResult<T> : Result {
    val value : T
}


enum class Type {
    ANY
    ITERATOR
    SNAPSHOT
    SINGLE
}