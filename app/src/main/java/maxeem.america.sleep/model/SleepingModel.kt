package maxeem.america.sleep.model

import maxeem.america.sleep.ext.wakeup
import maxeem.america.sleep.misc.Prefs
import org.jetbrains.anko.info

class SleepingModel(private val nightId: Long) : BaseModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::SleepingModel)
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
