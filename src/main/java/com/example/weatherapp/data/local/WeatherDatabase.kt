package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [WeatherEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object {
        @Volatile
        private var instance: WeatherDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. Create new table with the updated schema
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS weather_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        cityName TEXT NOT NULL DEFAULT '',
                        description TEXT NOT NULL DEFAULT '',
                        temperature REAL NOT NULL DEFAULT 0.0,
                        feelsLike REAL NOT NULL DEFAULT 0.0,
                        humidity INTEGER NOT NULL DEFAULT 0,
                        pressure INTEGER NOT NULL DEFAULT 0,
                        windSpeed REAL NOT NULL DEFAULT 0.0,
                        icon TEXT NOT NULL DEFAULT '',
                        timestamp INTEGER NOT NULL
                    )
                """)

                // 2. Drop old table (we assume schema changed too much to migrate data)
                database.execSQL("DROP TABLE IF EXISTS weather")

                // 3. Rename new table to match old table name
                database.execSQL("ALTER TABLE weather_new RENAME TO weather")
            }
        }

        fun getDatabase(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_db"
                )
                    .addMigrations(MIGRATION_1_2)
                    // Uncomment this line if you want to reset DB on migration mismatch (dev only)
                    // .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }
        }
    }
}
