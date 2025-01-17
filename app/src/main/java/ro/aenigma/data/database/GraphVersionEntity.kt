package ro.aenigma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.aenigma.util.Constants.Companion.GRAPH_VERSIONS_TABLE
import java.util.Date

@Entity(tableName = GRAPH_VERSIONS_TABLE)
class GraphVersionEntity (
    val version: String,
    val date: Date
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
