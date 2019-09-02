package maxeem.america.sleep

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import maxeem.america.sleep.databinding.FragmentAboutBinding
import maxeem.america.sleep.ext.compatActivity
import maxeem.america.sleep.model.AboutModel

class AboutFragment : BaseFragment() {

    override
    val model by viewModels<AboutModel>()

    override
    fun onCreateView(inflater: LayoutInflater,
                     container: ViewGroup?,
                     savedInstanceState: Bundle?) =
        FragmentAboutBinding.inflate(inflater, container, false).apply {
            model = viewModels<AboutModel>().value
            compatActivity()?.apply {
                setSupportActionBar(toolbar)
                NavigationUI.setupActionBarWithNavController(this, findNavController())
            }
            logo.setOnClickListener { findNavController().popBackStack() }
        }.root

}
