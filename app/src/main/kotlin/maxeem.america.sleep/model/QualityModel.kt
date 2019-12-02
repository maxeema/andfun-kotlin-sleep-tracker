package maxeem.america.sleep.model

import androidx.lifecycle.map
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.ext.logPeriod
import maxeem.america.sleep.misc.Prefs

class QualityModel(private val nightId: Long) : BaseModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::QualityModel)
    }

    val night = dao.getAsLive(nightId)

    val hasData = night.map { it != null }

    val date = night.map { it?.logPeriod() }

    fun onSetQuality(quality: Night.Quality) = action {
        val updated = dao.updateQuality(nightId, quality)
        require(updated == 1) { "updated $updated of 1" }
        Prefs.lastNightHasToQualified = false
        onComplete?.let { it(quality) }
    }

}
