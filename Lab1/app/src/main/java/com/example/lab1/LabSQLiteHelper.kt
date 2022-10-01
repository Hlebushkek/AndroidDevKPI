package com.example.lab1

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Bundle
import android.util.Log
import androidx.core.database.getStringOrNull

class LabSQLiteHelper(
    private val applicationContext: Context?
) : SQLiteOpenHelper(applicationContext, "lab.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val sql = applicationContext?.assets?.open("db_init.sql")?.bufferedReader().use {
            it?.readText()
        }
        db?.execSQL(sql);
    }

    override fun onUpgrade(db: SQLiteDatabase?, db1: Int, db2: Int) {
        TODO("Not yet implemented")
    }

    fun addHistoryUnit(text: String, fontName: String, fontStyle: Int) {
        val values = ContentValues()
        values.put(TEXT_COL, text)
        values.put(FONT_NAME_COL, fontName)
        values.put(FONT_STYLE_COL, fontStyle)

        val db = this.writableDatabase
        db.insert(TABLE_NAME, null, values)
//        db.close()
    }

    fun getHistory(): Array<HistoryItem> {
        val history = arrayListOf<HistoryItem>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        val textColIndex = cursor.getColumnIndex(LabSQLiteHelper.TEXT_COL)
        val fontNameColIndex = cursor.getColumnIndex(LabSQLiteHelper.FONT_NAME_COL)
        val fontStyleColIndex = cursor.getColumnIndex(LabSQLiteHelper.FONT_STYLE_COL)

        if (cursor.count > 0) {
            cursor.moveToFirst()

            do {
                val text = cursor.getString(textColIndex)
                val fontName = cursor.getString(fontNameColIndex)
                val fontStyle = cursor.getInt(fontStyleColIndex)

                history.add(HistoryItem(text, fontName, fontStyle))
            } while (cursor.moveToNext())
        }

        cursor.close()

        return history.toTypedArray().reversedArray()
    }

    companion object {
        @JvmStatic
        fun newInstance(applicationContext: Context?) = LabSQLiteHelper(applicationContext)

        private val DATABASE_NAME = "Lab.db"
        private val DATABASE_VERSION = 1

        const val TABLE_NAME = "History"

        const val ID_COL = "id"
        const val TEXT_COL = "text"
        const val FONT_NAME_COL = "font_name"
        const val FONT_STYLE_COL = "font_style"
    }
}