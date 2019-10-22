package maxeem.america.sleep.data

import androidx.room.Room
import maxeem.america.sleep.app
import maxeem.america.sleep.misc.DATABASE_NAME

abstract class NightsDatabaseImpl : NightsDatabase() {

    companion object {
        val instance by lazy {
            Room.databaseBuilder(app, NightsDatabase::class.java, DATABASE_NAME).run {
                fallbackToDestructiveMigration()
                build()
            }
        }
    }

}