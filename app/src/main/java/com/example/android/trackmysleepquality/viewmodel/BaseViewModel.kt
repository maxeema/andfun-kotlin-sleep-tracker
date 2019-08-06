package com.example.android.trackmysleepquality.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.data.NightsDao
import com.example.android.trackmysleepquality.data.NightsDatabase
import com.example.android.trackmysleepquality.util.asImmutable
import com.example.android.trackmysleepquality.util.asMutable
import com.example.android.trackmysleepquality.util.hash
import kotlinx.coroutines.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class BaseViewModel : ViewModel(), AnkoLogger {

    init { info("$hash new instance") }

    val messageEvent = MutableLiveData<MessageEvent?>().asImmutable()

    protected val dao = NightsDatabase.instance.nightsDao

    private var job : Job? = null
    private fun checkIsActive() = job?.isActive?.takeIf { it }
            ?.apply { messageEvent.asMutable().value = MessageEvent.Info(R.string.wait)} ?: false

    protected fun action(block: suspend CoroutineScope.()->Unit) {
        if (checkIsActive()) return
        viewModelScope.launch {
            runCatching {
                this.block()
            }.onFailure { err ->
                messageEvent.asMutable().value = MessageEvent.Error(R.string.sorry, err)
            }
        }.apply {
            job = this
        }.invokeOnCompletion {
            job = null
        }
    }

    protected suspend fun <T> dao(block: NightsDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

    fun messageEventConsumed() { messageEvent.asMutable().value = null }

    sealed class MessageEvent {
        class Info(val msg: Int) : MessageEvent()
        class Error(val msg: Int, val err: Throwable) : MessageEvent()
    }

}
