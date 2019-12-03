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
import maxeem.america.sleep.misc.Consumable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseModel : ViewModel(), AnkoLogger, KoinComponent {

    init { info("$hash new instance") }

    var onComplete : ((Any)->Unit)? = null

    val messageEvent = MutableLiveData<Consumable<MessageEvent?>>().asImmutable()

    private val db : NightsDatabase by inject()
    protected val dao = db.nightsDao

    private var job : Job? = null
    private fun checkIsActive() = job?.isCompleted?.takeUnless { it }
            ?.apply { messageEvent.asMutable().value = Consumable of MessageEvent.Info(R.string.wait) } ?: false

    protected fun action(code: suspend CoroutineScope.()->Unit) {
        if (checkIsActive()) return
        viewModelScope.launch {
            job = this as Job
            runCatching {
                this.code()
            }.onFailure { err ->
                messageEvent.asMutable().value = Consumable of MessageEvent.Error(R.string.sorry, err)
            }
        }.invokeOnCompletion {
            job = null
        }
    }

//    protected suspend fun <T> dao(block: NightsDao.()->T) = withContext(Dispatchers.IO) { dao.block() }

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
