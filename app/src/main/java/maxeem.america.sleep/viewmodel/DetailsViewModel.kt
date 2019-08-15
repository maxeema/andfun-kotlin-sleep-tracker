package maxeem.america.sleep.viewmodel

import maxeem.america.sleep.ext.singleArgViewModelFactory

class DetailsViewModel(nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsViewModel)
    }

    val night = dao.getAsLive(nightId)

}
