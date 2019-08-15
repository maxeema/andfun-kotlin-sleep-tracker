package maxeem.america.sleeping_journal.misc

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff.Mode
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.ImageView
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import maxeem.america.sleeping_journal.R
import maxeem.america.sleeping_journal.data.Night
import maxeem.america.sleeping_journal.ext.asString
import maxeem.america.sleeping_journal.ext.fromHtml

object Utils {

    fun fromHtml(s: String) = HtmlCompat.fromHtml(s, HtmlCompat.FROM_HTML_MODE_COMPACT)

    fun formatError(msgId: Int, err: Throwable) =
        msgId.asString().plus("<br/><br/><small>[ ${err.javaClass.simpleName} ]").let {
            if (err.message.isNullOrBlank()) it else it.plus("<br/>${err.message}")
        }.plus("</small>").fromHtml()

    @SuppressLint("ResourceType")
    fun loadNightImage(view: ImageView, quality: Night.Quality, cached: Boolean = false) =
        Glide.with(view).load(quality.img).apply {
            if (!cached)
                skipMemoryCache(true)
    //        .transition(DrawableTransitionOptions.withCrossFade())
            placeholder(R.drawable.undefined)
            into(view)
        }

    fun dim(dim: Boolean, clockView: View) {
        val paint = Paint()
        paint.color = Color.WHITE
        paint.colorFilter = PorterDuffColorFilter(if (dim) 0x40FFFFFF else 0xC0FFFFFF.toInt(), Mode.MULTIPLY)
        clockView.setLayerType(View.LAYER_TYPE_HARDWARE, paint)
    }

}

