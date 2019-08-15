package maxeem.america.sleep.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import maxeem.america.sleep.misc.Utils
import maxeem.america.sleep.misc.app

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun String.fromHtml() = Utils.fromHtml(this)

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

fun Fragment.compatActivity() = activity as AppCompatActivity?

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

//val asString : Int.()->Unit = { app.getString(this)}
fun Int.asString() = app.getString(this)
fun Int.asDrawable() = app.getDrawable(this)
