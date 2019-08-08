package com.example.android.trackmysleepquality.misc

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

val Any.hash get() = hashCode()

fun String.fromHtml() = Utils.fromHtml(this)

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

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

fun <T : RecyclerView.LayoutManager> T?.grid() = this as GridLayoutManager

fun Int.asString() = app.getString(this)
fun Int.asDrawable() = app.getDrawable(this)

fun asString(code: ()->Int) = code().asString()
fun asDrawable(code: ()->Int) = code().asDrawable()
