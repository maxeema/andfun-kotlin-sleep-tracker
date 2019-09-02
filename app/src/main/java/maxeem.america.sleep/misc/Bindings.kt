package maxeem.america.sleep.misc

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import maxeem.america.sleep.data.Night
import maxeem.america.sleep.ext.fromHtml
import maxeem.america.sleep.misc.Utils.loadNightImage

@BindingAdapter("goneIf")
fun View.goneIf(condition: Boolean) {
    visibility = if (condition) View.GONE else View.VISIBLE
}

@BindingAdapter("textHtml")
fun TextView.setTextHtml(str: String) {
    text = str.fromHtml()
}

@BindingAdapter("srcOfNight")
fun ImageView.bindSrcOfNight(night: Night?) = night?.also {
    loadNightImage(this, night.quality)
}

@BindingAdapter("srcOfNightCached")
fun ImageView.bindSrcOfNightCached(night: Night?) = night?.also {
    loadNightImage(this, night.quality, true)
}
