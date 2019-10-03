package maxeem.america.sleep.model

import androidx.lifecycle.map

class DetailsRealModel(nightId: Long) : DetailsModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsRealModel)
    }

    init {
        dao.getAsLive(nightId).map {
            night.value = it
        }.apply {
            action { night.value = dao { get(nightId) } }
        }
    }

}
