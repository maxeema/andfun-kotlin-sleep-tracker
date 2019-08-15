package maxeem.america.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import maxeem.america.sleep.databinding.FragmentQualityBinding
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.viewmodel.QualityViewModel
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info

class QualityFragment : BaseFragment() {

    override val model by viewModels<QualityViewModel> {
        QualityViewModel.FACTORY(QualityFragmentArgs.fromBundle(requireArguments()).nightId)
    }

    override fun consumeBackPressed() = true.apply {
        view?.snackbar(R.string.qualify_sleep_first)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        info("$hash onCreateView")

        val binding = FragmentQualityBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.model = model

        model.onComplete = { // val quality = it as Night.Quality
            findNavController().navigate(QualityFragmentDirections.actionQualityFragToJournalFrag())
            model.onComplete = null
        }

        return binding.root
    }

}
