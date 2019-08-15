package maxeem.america.sleep.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import maxeem.america.sleep.R
import maxeem.america.sleep.data.NightsDao
import maxeem.america.sleep.data.NightsDatabase
import maxeem.america.sleep.ext.asImmutable
import maxeem.america.sleep.ext.asMutable
import maxeem.america.sleep.ext.hash
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

open class BaseViewModel : ViewModel(), AnkoLogger {

    init { info("$hash new instance") }

    var onComplete : ((Any)->Unit)? = null
    val messageEvent  = MutableLiveData<MessageEvent?>().asImmutable()

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

    sealed class MessageEvent(val msg: Int) {
        class Info(msg: Int) : MessageEvent(msg)
        class Error(msg: Int, val err: Throwable) : MessageEvent(msg)
    }

}
