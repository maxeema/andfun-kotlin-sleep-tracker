package maxeem.america.sleep.model

import androidx.lifecycle.map
import maxeem.america.sleep.ext.*

class DetailsModel(nightId: Long) : BaseModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsModel)
    }

    val night = dao.getAsLive(nightId)

    val hasData    = night.map { it != null }
    val isFinished = night.map { it?.isActive()?.not() ?: false }

    val startTime = night.map { it?.formattedPeriodStartTime() }
    val startDate = night.map { it?.formattedPeriodStartDate() }

    val endTime = night.map { it?.formattedPeriodEndTime() }
    val endDate = night.map { it?.formattedPeriodEndDate() }

    val length  = night.map { it?.formattedPeriodLength() }
    val quality = night.map { it?.quality?.label?.asString() }

    val icon = night.map { it?.quality?.img }

}
