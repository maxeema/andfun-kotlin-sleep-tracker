package maxeem.america.sleep.model

import androidx.lifecycle.LiveData
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.misc.Bool
import maxeem.america.sleep.misc.Prefs

abstract class JournalModel : BaseModel() {

    abstract val nights    : LiveData<List<Night>?>
    abstract val hasNights : LiveData<Bool>

    val hasData = Prefs.hasData

    open fun doSleep() {}
    open fun deleteItem(item: Night) {}
    open fun clearData() {}

}
