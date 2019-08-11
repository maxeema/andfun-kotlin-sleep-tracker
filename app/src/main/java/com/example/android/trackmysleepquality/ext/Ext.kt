package com.example.android.trackmysleepquality.ext

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.trackmysleepquality.misc.Utils
import com.example.android.trackmysleepquality.misc.app

/**
 * Extensions
 */

val Any.hash get() = hashCode()

fun String.fromHtml() = Utils.fromHtml(this)

fun <T> MutableLiveData<T>.asImmutable() = this as LiveData<T>
fun <T> LiveData<T>.asMutable()          = this as MutableLiveData<T>

fun <T : Activity> T?.compat() = this as AppCompatActivity

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

fun Int.asString() = app.getString(this)
fun Int.asDrawable() = app.getDrawable(this)
fun Int.asColorDrawable() = app.resources.getColor(this).let { ColorDrawable(it)}
