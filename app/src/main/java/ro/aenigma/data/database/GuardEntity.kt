package ro.aenigma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.aenigma.util.Constants.Companion.GUARDS_TABLE
import java.util.Date

@Entity(tableName = GUARDS_TABLE)
class GuardEntity (
    val address: String,
    val publicKey: String,
    val hostname: String,
    val date: Date
){
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
