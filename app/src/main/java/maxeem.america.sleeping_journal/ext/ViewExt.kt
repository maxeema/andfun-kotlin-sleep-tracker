package maxeem.america.sleeping_journal.ext

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.postDelayed
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import maxeem.america.sleeping_journal.misc.Anim
import maxeem.america.sleeping_journal.misc.timeMillis

/**
 * Views extensions
 */

fun View.animFadeIn() = Anim.fadeInOn(this)
fun View.animFadeOut() = Anim.fadeOutOn(this)

fun Drawable?.startIfItAnimatable() { Anim.startOn(this) }
//fun Drawable?.stopIfItAnimatable() { Anim.stopOn(this) }
//fun Drawable?.restartIfItAnimatable() { Anim.restartOn(this) }

fun <T : RecyclerView.LayoutManager> T?.grid() = this as GridLayoutManager
fun <T : ViewGroup.LayoutParams> T.constraint() = this as ConstraintLayout.LayoutParams

val invisibleFadeOutDelayed : View.(Long)->Unit = { delay ->
    val t = timeMillis
    tag = t
    postDelayed(delay) {
        if (t != tag) return@postDelayed
        visibility = View.INVISIBLE
        animFadeOut()
    }
}
