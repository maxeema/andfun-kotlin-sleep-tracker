package maxeem.america.sleep.misc

/**
 * Global const, conf, values, aliases
 */

const val DATABASE_NAME = "nights-db"
const val MOCK_DATA = false

typealias Str = String
typealias Bool = Boolean

val thread get() = Thread.currentThread()
val timeMillis get() = System.currentTimeMillis()
