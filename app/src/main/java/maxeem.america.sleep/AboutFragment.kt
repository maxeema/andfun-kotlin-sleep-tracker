package maxeem.america.sleep

import android.content.Intent
import android.net.Uri
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
            googlePlay.setOnClickListener {
                Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(
                            "https://play.google.com/store/apps/details?id=${app.packageName}")
//                            "https://play.google.com/store/apps/details?id=com.google.android.apps.maps")
                    setPackage("com.android.vending")
                    startActivity(this)
                }
            }
            logo.setOnClickListener { findNavController().popBackStack() }
        }.root

}
