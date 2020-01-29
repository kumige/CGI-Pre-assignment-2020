package mikkoromo.cgipre_assignment

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*


@androidx.room.Database(entities = [User::class, Record::class], version = 1)

abstract class Database : RoomDatabase() {
    abstract fun dao(): DAO

    companion object {
        @Volatile
        private var instance: Database? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            Database::class.java, "bird-records.db"
        )
            .build()

        fun getInstance(context: Context): Database? {
            synchronized(LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Database::class.java, "Posts.db"
                    )
                        .allowMainThreadQueries()
                        .build()
                }
                return instance
            }
        }

    }

}

@Entity(tableName = "user")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "user") var user: String
)

@Entity(tableName = "records")
data class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "notes") var notes: String,
    @ColumnInfo(name = "rarity") var rarity: String,
    @ColumnInfo(name = "timestamp") var timestamp: String,
    @ColumnInfo(name = "latitude") var latitude: String,
    @ColumnInfo(name = "longitude") var longitude: String,
    @ColumnInfo(name = "image_path") var imagePath: String?
)

@Dao
interface DAO {
    @Insert
    fun insertRecord(record: Record)

    @Query("SELECT * FROM records")
    fun getRecords(): LiveData<List<Record>>

    @Query("SELECT * FROM records ORDER BY rarity DESC")
    fun getRecordsByRarity(): LiveData<List<Record>>

    @Query("DELETE FROM records WHERE id=:id")
    fun deleteRecord(id: Int)

}
