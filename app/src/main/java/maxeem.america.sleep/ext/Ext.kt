package maxeem.america.sleep.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import maxeem.america.sleep.app
import maxeem.america.sleep.handler
import maxeem.america.sleep.misc.Utils

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun String.fromHtml() = Utils.fromHtml(this)

//val asString : Int.()->Unit = { app.getString(this)}
fun Int.asString() = app.getString(this)
fun Int.asDrawable() = app.getDrawable(this)

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

fun Fragment.compatActivity() = activity as AppCompatActivity?

fun AppCompatActivity.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, code: ()->Unit) {
    if (isFinishing || isDestroyed) return
    app.handler.postDelayed(delay) {
        if (lifecycle.currentState.isAtLeast(stateAtLeast))
            code()
    }
}

fun <T : ViewModel, A> singleArgViewModelFactory(constructor: (A) -> T):
        (A) -> ViewModelProvider.NewInstanceFactory {
    return { arg: A ->
        object : ViewModelProvider.NewInstanceFactory() {
            @Suppress("UNCHECKED_CAST")
            override fun <V : ViewModel> create(modelClass: Class<V>): V {
                return constructor(arg) as V
            }
        }
    }
}
