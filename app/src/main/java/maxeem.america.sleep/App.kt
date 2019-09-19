package maxeem.america.sleep

import android.app.Application
import android.os.Handler

val app = App.instance

class App : Application() {

    companion object {
        private var initializer : (()-> App)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
        @JvmStatic
        val VERSION by lazy { instance.packageManager.getPackageInfo(instance.packageName, 0).versionName }
    }

    init { initializer = { this } }

}

val App.handler by lazy { Handler(app.mainLooper) }
