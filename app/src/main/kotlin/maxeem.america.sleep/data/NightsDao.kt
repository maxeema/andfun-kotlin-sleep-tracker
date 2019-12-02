package maxeem.america.sleep.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NightsDao {

    @Insert
    suspend fun insert(item: Night) : Long

    @Insert
    suspend fun insert(vararg items: Night): List<Long>

    @Update
    suspend fun update(item: Night) : Int

    @Delete
    suspend fun delete(vararg item: Night) : Int

    @Query("DELETE FROM nights")
    suspend fun clear() : Int

    @Query("SELECT * from nights WHERE id = :nightId")
    suspend fun get(nightId: Long) : Night?

    @Query("SELECT * from nights WHERE id = :nightId")
    fun getAsLive(nightId: Long) : LiveData<Night?>

    @Query("SELECT * FROM nights ORDER BY id DESC")
    fun getAll() : LiveData<List<Night>?>

    @Query("UPDATE nights SET quality = :quality WHERE id = :nightId")
    suspend fun updateQuality(nightId: Long, quality: Night.Quality) : Int

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    suspend fun tonight() : Night?

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    fun tonightAsLive() : LiveData<Night?>

}
