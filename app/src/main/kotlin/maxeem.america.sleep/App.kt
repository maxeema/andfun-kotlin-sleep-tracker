package maxeem.america.sleep

import android.app.Application
import android.os.Handler
import maxeem.america.sleep.data.NightsDatabase
import maxeem.america.sleep.data.NightsDatabaseImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.module

val app = App.instance

private val appModule = module {
    single {
        if (BuildConfig.databaseImpl is NightsDatabase)
            BuildConfig.databaseImpl
        else NightsDatabaseImpl.instance
    }
}

class App : Application() {

    companion object {
        private var initializer : (()-> App)? = null
        val instance by lazy { requireNotNull(initializer).apply{ initializer = null }()  }
        @JvmStatic
        val VERSION by lazy { instance.packageManager.getPackageInfo(instance.packageName, 0).versionName }
    }

    init { initializer = { this } }

    override fun onCreate() { super.onCreate()
        startKoin {
            androidLogger(); androidContext(this@App)
            modules(appModule)
        }
    }

}

val App.handler by lazy { Handler(app.mainLooper) }