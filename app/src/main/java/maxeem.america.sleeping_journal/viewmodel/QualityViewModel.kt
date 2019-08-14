package maxeem.america.sleeping_journal.viewmodel

import maxeem.america.sleeping_journal.data.Night
import maxeem.america.sleeping_journal.ext.singleArgViewModelFactory
import maxeem.america.sleeping_journal.misc.Prefs

class QualityViewModel(private val nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::QualityViewModel)
    }

    val night = dao.tonightAsLive()

    fun onSetQuality(quality: Night.Quality) = action {
        val updated = dao { updateQuality(nightId, quality) }
        require(updated == 1) { "updated $updated of 1" }
        Prefs.lastNightQualified = true
        onComplete?.let { it(quality) }
    }

}
