package com.vanclykin.notebooksqlite.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//НУЖЕН ДЛЯ ОТКРЫТИЯ БАЗЫ ДАННЫХ создать бд и таблицу в ней

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, MyDbNameClass.DATABASE_NAME, null, MyDbNameClass.DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(MyDbNameClass.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(MyDbNameClass.SQL_DELETE_TABLE)
        onCreate(db)
    }
}