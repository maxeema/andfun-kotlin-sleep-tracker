package maxeem.america.sleep.misc

class Consumable<T>(private var data: T?) {

    fun consume() = data?.also { data = null }

    companion object {
        infix fun <T> of(any: T) = Consumable(any)
    }

}