package com.vanclykin.notebooksqlite.db

import android.provider.BaseColumns

//НЕОБХОДИМЫЕ КОНСТАТНЫ И ЗНАЧЕНИЯ БАЗЫ ДАННЫХ

object MyDbNameClass {                      //0   title  content
    const val TABLE_NAME = "my_table"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_CONTENT = "content"
    const val COLUMN_IMAGE_URI = "uri"
    const val COLUMN_NAME_TIME = "time"

    const val DATABASE_VERSION = 5
    const val DATABASE_NAME = "NotebookDb.db"

    const val SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS $TABLE_NAME (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT,$COLUMN_NAME_CONTENT TEXT,$COLUMN_IMAGE_URI TEXT,$COLUMN_NAME_TIME TEXT)"
    const val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
}