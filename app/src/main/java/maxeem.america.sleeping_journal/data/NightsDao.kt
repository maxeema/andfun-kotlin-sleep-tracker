package maxeem.america.sleeping_journal.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NightsDao {

    @Insert
    fun insert(item: Night) : Long

    @Update
    fun update(item: Night) : Int

    @Delete
    fun delete(vararg item: Night) : Int

    @Query("DELETE FROM nights")
    fun clear() : Int

    @Query("SELECT * from nights WHERE id = :nightId")
    fun get(nightId: Long) : Night?

    @Query("SELECT * from nights WHERE id = :nightId")
    fun getAsLive(nightId: Long) : LiveData<Night?>

    @Query("SELECT * FROM nights ORDER BY id DESC")
    fun getAll() : LiveData<List<Night>?>

    @Query("UPDATE nights SET quality = :quality WHERE id = :nightId")
    fun updateQuality(nightId: Long, quality: Night.Quality) : Int

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    fun tonight() : Night?

    @Query("SELECT * FROM nights ORDER BY id DESC LIMIT 1")
    fun tonightAsLive() : LiveData<Night?>

}
