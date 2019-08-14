package maxeem.america.sleeping_journal

import android.app.Application
import android.os.Handler
import maxeem.america.sleeping_journal.misc.app

class App : Application() {

    companion object {
        private var initializer : (()-> App)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
    }

    init { initializer = { this } }

}

val App.handler by lazy { Handler(app.mainLooper) }
