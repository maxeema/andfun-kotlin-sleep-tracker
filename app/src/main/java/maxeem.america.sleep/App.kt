package maxeem.america.sleep

import android.app.Application
import android.os.Handler
import maxeem.america.sleep.misc.app

class App : Application() {

    companion object {
        private var initializer : (()-> App)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
    }

    init { initializer = { this } }

}

val App.handler by lazy { Handler(app.mainLooper) }
