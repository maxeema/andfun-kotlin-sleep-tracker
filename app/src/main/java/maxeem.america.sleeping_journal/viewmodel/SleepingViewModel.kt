package maxeem.america.sleeping_journal.viewmodel

import maxeem.america.sleeping_journal.ext.singleArgViewModelFactory
import maxeem.america.sleeping_journal.ext.wakeup
import maxeem.america.sleeping_journal.misc.Prefs
import org.jetbrains.anko.info

class SleepingViewModel(private val nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::SleepingViewModel)
    }

    val night = dao.getAsLive(nightId)

    fun onWakeUp() = action {
        val updated = dao { get(nightId)!!.run { wakeup(); update(this) } }
        info(" updated $updated")
        require(updated == 1) { "updated $updated of 1" }
        Prefs.lastNightActive = false
        onComplete?.invoke(Unit)
    }.let { true }

}
