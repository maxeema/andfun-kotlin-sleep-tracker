package maxeem.america.sleeping_journal.misc

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.lifecycle.Lifecycle
import maxeem.america.sleeping_journal.App
import maxeem.america.sleeping_journal.handler

/**
 * Consts
 */

const val DATABASE_NAME = "nights-db"

val app = App.instance

val timeMillis get() = System.currentTimeMillis()

fun AppCompatActivity.delayed(delay: Long, stateAtLeast: Lifecycle.State = Lifecycle.State.CREATED, code: ()->Unit) {
    if (isFinishing || isDestroyed) return
    app.handler.postDelayed(delay) {
        if (lifecycle.currentState.isAtLeast(stateAtLeast))
            code()
    }
}
