package maxeem.america.sleeping_journal.data

import maxeem.america.sleeping_journal.ext.asString
import maxeem.america.sleeping_journal.ext.logPeriod

object Converter {

    @JvmStatic fun nightToPeriodText(night: Night?) =
            night?.run { logPeriod() } ?: ""

    @JvmStatic fun nightToQualityText(night: Night?) =
        night?.run { quality.label.asString() } ?: ""

}