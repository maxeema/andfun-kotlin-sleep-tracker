package maxeem.america.sleep.data

import maxeem.america.sleep.ext.asString
import maxeem.america.sleep.ext.logPeriod

object Converter {

    @JvmStatic fun nightToPeriodText(night: Night?) =
            night?.run { logPeriod() } ?: ""

    @JvmStatic fun nightToQualityText(night: Night?) =
        night?.run { quality.label.asString() } ?: ""

}