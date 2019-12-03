package maxeem.america.sleep.ext

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import maxeem.america.sleep.R
import maxeem.america.sleep.app
import maxeem.america.sleep.misc.Utils

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun String.fromHtml() = Utils.fromHtml(this)

fun Int.asString() = app.getString(this)
fun Int.asString(vararg args: Any) = app.getString(this, *args)
fun Int.asQuantityString(quantity: Int, vararg args: Any) = app.resources.getQuantityString(this, quantity, *args)
fun Int.asDrawable() = app.getDrawable(this)

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

fun Fragment.compatActivity() = activity as AppCompatActivity?

fun Fragment.materialAlert(@StringRes msg: Int, code: (MaterialAlertDialogBuilder.()->Unit)? = null)
        = materialAlert(app.getString(msg), code)
fun Fragment.materialAlert(msg: CharSequence, code: (MaterialAlertDialogBuilder.()->Unit)? = null) =
    MaterialAlertDialogBuilder(context, R.style.AlertThemeOverlay).apply {
        setMessage(msg)
        code?.invoke(this)
    }.show()