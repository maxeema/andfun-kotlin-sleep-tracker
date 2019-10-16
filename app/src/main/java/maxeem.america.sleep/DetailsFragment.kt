package maxeem.america.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import maxeem.america.sleep.databinding.FragmentDetailsBinding
import maxeem.america.sleep.ext.compatActivity
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.model.DetailsModel
import org.jetbrains.anko.info

class DetailsFragment : BaseFragment() {

    override val model by viewModels<DetailsModel> {
        val nightId = DetailsFragmentArgs.fromBundle(requireArguments()).nightId
        DetailsModel.FACTORY(nightId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        info("$hash onCreateView, $savedInstanceState")

        val binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.model = model
        binding.lifecycleOwner = viewLifecycleOwner

        model.onComplete = {
            NavigationUI.navigateUp(findNavController(), null) // findNavController().popBackStack()
            model.onComplete = null
        }

        compatActivity()?.apply {
            setSupportActionBar(binding.toolbar)
            NavigationUI.setupActionBarWithNavController(this, findNavController())
        }

        return binding.root
    }

}
