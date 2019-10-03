package maxeem.america.sleep.model

class DetailsMockModel(nightId: Long) : DetailsModel() {

    companion object {
        val FACTORY = singleArgViewModelFactory(::DetailsMockModel)
    }

    init {
        night.value = JournalMockModel.instance?.nightById(nightId)
    }

}
