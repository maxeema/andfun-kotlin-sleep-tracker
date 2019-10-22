package maxeem.america.sleep

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import maxeem.america.sleep.ext.hash
import maxeem.america.sleep.ext.materialAlert
import maxeem.america.sleep.misc.Utils
import maxeem.america.sleep.model.BaseModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.info

open class BaseFragment : Fragment(), AnkoLogger {

    init { info("$hash init") }

    protected open val model : BaseModel? = null

    open fun consumeBackPressed() = false
    open fun handleUserInteracted() = Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        info("$hash onViewCreated, savedInstanceState: $savedInstanceState")

        model?.messageEvent?.observe(viewLifecycleOwner) { msg ->
            msg ?: return@observe
            view.longSnackbar(msg.msg).apply {
                if (msg is BaseModel.MessageEvent.Error)
                    setAction(R.string.details) { materialAlert(Utils.formatError(msg.msg, msg.err)) }
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
