package ro.aenigma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.aenigma.util.Constants.Companion.EDGES_TABLE

@Entity(tableName = EDGES_TABLE)
class EdgeEntity (
    val sourceAddress: String,
    val targetAddress: String
)
{
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
