/*
 * Copyright 2018 Martin Lapis, Alan Dreamer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tcqq.searchview.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

@Deprecated
class SearchHistoryDatabase extends SQLiteOpenHelper {

    static final String SEARCH_HISTORY_TABLE = "search_history";
    static final String SEARCH_HISTORY_COLUMN_ID = "_id";
    static final String SEARCH_HISTORY_COLUMN_TITLE = "_title";
    static final String SEARCH_HISTORY_COLUMN_SUBTITLE = "_subtitle";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "searchHistoryDatabase.db";
    private static final String CREATE_TABLE_SEARCH_HISTORY = "CREATE TABLE IF NOT EXISTS "
            + SEARCH_HISTORY_TABLE + " ( "
            + SEARCH_HISTORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SEARCH_HISTORY_COLUMN_TITLE + " TEXT, "
            + SEARCH_HISTORY_COLUMN_SUBTITLE + " TEXT " + ");";

    SearchHistoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(@NonNull SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SEARCH_HISTORY);
    }

    @Override
    public void onUpgrade(@NonNull SQLiteDatabase db, int oldVersion, int newVersion) {
        dropAllTables(db);
        onCreate(db);
    }

    private void dropAllTables(@NonNull SQLiteDatabase db) {
        dropTableIfExists(db);
    }

    private void dropTableIfExists(@NonNull SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + SEARCH_HISTORY_TABLE);
    }

}



/* kulate rohy a light a zkontrolvat Bar a compat nekde pouzito???
 * ZKONTROLOVAT VZHLED KODU ...
 * readme
 * +  todo obraky a dodelat vypis metod
 * THIS A PRIVATE
 * colorpicker
 * komENTARE A BUGY
 * */



/*
if ( drawable != null ) {
    Bitmap bitmap = (Bitmap) ((BitmapDrawable) drawable).getBitmap();
    parcel.writeParcelable(bitmap, flags);
}
else {
    parcel.writeParcelable(null, flags);
}

To read the Drawable from the Parcelable:

Bitmap bitmap = (Bitmap) in.readParcelable(getClass().getClassLoader());
if ( bitmap != null ) {
    drawable = new BitmapDrawable(bitmap);
}
else {
    drawable = null;
}
*/