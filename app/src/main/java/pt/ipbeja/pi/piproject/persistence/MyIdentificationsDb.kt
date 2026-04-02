package pt.ipbeja.pi.piproject.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

/**
 * Database object to connect to the database
 *
 * reference: https://developer.android.com/training/data-storage/room/index.html
 */
@Database(version = 2, entities = [Identification::class], exportSchema = false)
@TypeConverters(
    Converters::class
)
abstract class MyIdentificationsDb : RoomDatabase() {
    abstract fun identificationDao(): IdentificationDao

    companion object {
        @Volatile
        private var INSTANCE: MyIdentificationsDb? = null

        fun getDatabase(context: Context): MyIdentificationsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyIdentificationsDb::class.java,
                    "myidentifications.db"
                )
                    // 2. ADICIONE ESTA LINHA AQUI:
                    .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                    instance
            }
        }
    }
}
