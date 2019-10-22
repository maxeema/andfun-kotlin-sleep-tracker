package maxeem.america.sleep.data

import androidx.room.TypeConverter

class ConvertTypes {

    @TypeConverter
    fun qualityFrom(quality: Night.Quality) = quality.id

    @TypeConverter
    fun qualityTo(qualityId: Int) = Night.Quality.of(qualityId)

}