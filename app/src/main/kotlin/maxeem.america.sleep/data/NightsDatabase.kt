package maxeem.america.sleep.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
        entities = [Night::class],
        version = 1,
        exportSchema = false
)
@TypeConverters(ConvertTypes::class)
abstract class NightsDatabase : RoomDatabase() {

    abstract val nightsDao: NightsDao

}
