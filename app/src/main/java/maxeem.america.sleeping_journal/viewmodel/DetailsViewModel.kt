package maxeem.america.sleeping_journal.viewmodel

import maxeem.america.sleeping_journal.ext.singleArgViewModelFactory

class DetailsViewModel(nightId: Long) : BaseViewModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsViewModel)
    }

    val night = dao.getAsLive(nightId)

}
