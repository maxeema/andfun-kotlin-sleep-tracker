package maxeem.america.sleep.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import maxeem.america.sleep.R
import maxeem.america.sleep.data.NightsDatabase
import maxeem.america.sleep.ext.asImmutable
import maxeem.america.sleep.ext.asMutable
import maxeem.america.sleep.ext.hash
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseModel : ViewModel(), AnkoLogger, KoinComponent {

    init { info("$hash new instance") }

    var onComplete : ((Any)->Unit)? = null

    val messageEvent = MutableLiveData<MessageEvent?>().asImmutable()

    private val db : NightsDatabase by inject()
    protected val dao = db.nightsDao

    private var job : Job? = null
    private fun checkIsActive() = job?.isActive?.takeIf { it }
            ?.apply { messageEvent.asMutable().value = MessageEvent.Info(R.string.wait) } ?: false

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

//    protected suspend fun <T> dao(block: NightsDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

    fun messageEventConsumed() { messageEvent.asMutable().value = null }

    sealed class MessageEvent(val msg: Int) {
        class Info(msg: Int) : MessageEvent(msg)
        class Error(msg: Int, val err: Throwable) : MessageEvent(msg)
    }

    protected companion object {
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
    }

}
