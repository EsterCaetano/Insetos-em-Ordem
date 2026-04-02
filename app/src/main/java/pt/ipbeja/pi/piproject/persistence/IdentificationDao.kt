package pt.ipbeja.pi.piproject.persistence

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

/**
 * Data access object for identifications
 */
@Dao
interface IdentificationDao {
    /**
     * Inserts a record of an identification in the database
     *
     * @param identification the identification to insert
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(identification: Identification)

    /**
     * Removes an identification from the the database
     *
     * @param identification the identification to remove
     */
    @Delete
    suspend fun delete(identification: Identification)

    @Update
    suspend fun update(identification: Identification)

    @Query("UPDATE identification SET 'order' = :order WHERE id = :id")
    suspend fun updateOrderInIdentification(id: Long, order: String)

    @Query("SELECT * FROM identification")
    suspend fun getAll(): List<Identification>

    @Query("SELECT COUNT(*) FROM identification")
    suspend fun count(): Int
}
