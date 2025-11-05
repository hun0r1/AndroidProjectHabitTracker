package com.digiventure.ventnote.config

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.digiventure.ventnote.commons.Constants
import com.digiventure.ventnote.data.persistence.NoteDAO
import com.digiventure.ventnote.data.persistence.NoteModel
import java.util.Date

object DateConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}

object ListStringConverters {
    @TypeConverter
    fun toList(value: String?): List<String> {
        if (value.isNullOrBlank()) return emptyList()
        val trimmed = value.trim()
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) return emptyList()
        val inner = trimmed.substring(1, trimmed.length - 1)
        // Simple parser: iterate chars and split on commas not inside quotes
        val result = mutableListOf<String>()
        val sb = StringBuilder()
        var inQuotes = false
        var i = 0
        while (i < inner.length) {
            val c = inner[i]
            if (c == '"') {
                inQuotes = !inQuotes
                i++
                continue
            }
            if (c == ',' && !inQuotes) {
                result.add(sb.toString().trim().trim('"'))
                sb.clear()
            } else {
                sb.append(c)
            }
            i++
        }
        if (sb.isNotEmpty()) {
            result.add(sb.toString().trim().trim('"'))
        }
        return result.filter { it.isNotEmpty() }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String =
        list?.joinToString(prefix = "[", postfix = "]") { s -> "\"${s.replace("\"", "\\\"")}\"" } ?: "[]"
}

@Database(
    entities = [NoteModel::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration (from = 1, to = 2),
        AutoMigration (from = 2, to = 3)
    ]
)
@TypeConverters(DateConverters::class, ListStringConverters::class)
abstract class NoteDatabase: RoomDatabase() {
    abstract fun dao(): NoteDAO

    companion object{
        @Volatile
        private var instance: NoteDatabase? = null

        fun getInstance(context : Context): NoteDatabase {
            if (instance == null) {
                synchronized(this) {
                    instance = Room.databaseBuilder(
                        context,
                        NoteDatabase::class.java,
                        Constants.BACKUP_FILE_NAME
                    ).build()
                }
            }

            return instance!!
        }
    }
}