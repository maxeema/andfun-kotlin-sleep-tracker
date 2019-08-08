package com.example.android.trackmysleepquality

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.example.android.trackmysleepquality.misc.Utils
import com.example.android.trackmysleepquality.ext.hash
import com.example.android.trackmysleepquality.viewmodel.BaseViewModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.design.snackbar
import org.jetbrains.anko.info

open class BaseFragment : Fragment(), AnkoLogger {

    init { info("$hash init") }

    protected open val model : BaseViewModel? = null

    open fun consumeBackPressed() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info("$hash onViewCreated, savedInstanceState: $savedInstanceState")

        model?.messageEvent?.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            view.snackbar(msg.msg).apply {
                if (msg is BaseViewModel.MessageEvent.Error)
                    setAction(R.string.details) { requireActivity().alert(Utils.formatError(msg.msg, msg.err)).show() }
            }
            model!!.messageEventConsumed()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState)
        info("$hash onCreate, savedInstanceState: $savedInstanceState")
    }

    override fun onAttach(context: Context) { super.onAttach(context)
        info("$hash onAttach, context: $context")
    }
    override fun onDetach() { super.onDetach();  info("$hash onDetach") }

    override fun onActivityCreated(savedInstanceState: Bundle?) { super.onActivityCreated(savedInstanceState)
        info("$hash onActivityCreated, savedInstanceState: $savedInstanceState")
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) { super.onViewStateRestored(savedInstanceState)
        info("$hash onViewStateRestored, savedInstanceState: $savedInstanceState")
    }
    override fun onSaveInstanceState(outState: Bundle) { super.onSaveInstanceState(outState)
        info("$hash onSaveInstanceState, outState: $outState")
    }

    override fun onStart() { super.onStart(); info("$hash onStart") }
    override fun onResume() { super.onResume(); info("$hash onResume") }

    override fun onPause() { super.onPause(); info("$hash onPause") }
    override fun onStop() { super.onStop(); info("$hash onStop") }

    override fun onDestroyView() { super.onDestroyView(); info("$hash onDestroyView") }
    override fun onDestroy() { super.onDestroy(); info("$hash onDestroy") }

}
