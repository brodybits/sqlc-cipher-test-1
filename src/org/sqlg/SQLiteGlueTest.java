package org.sqlg;

import android.app.Activity;
import android.os.Bundle;

import org.sqlg.SQLiteGlue;

import java.io.File;

public class SQLiteGlueTest extends Activity
{
  static
  {
    System.loadLibrary("sqlg");
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    File dbfile = new File(getFilesDir(), "DB.db");

    long mydb = SQLiteGlue.sqlg_db_open(dbfile.getAbsolutePath(),
      SQLiteGlue.SQLG_OPEN_READWRITE | SQLiteGlue.SQLG_OPEN_CREATE);

    if (mydb < 0) {
      android.util.Log.w("SQLiteGlueTest", "DB open error: " + -mydb);
      return;
    }

    long st = SQLiteGlue.sqlg_db_prepare_st(mydb, "select upper('How about ascii text?') as caps");

    if (st < 0) {
      android.util.Log.w("SQLiteGlueTest", "prepare statement error: " + -st);
      SQLiteGlue.sqlg_db_close(mydb);
      return;
    }

    SQLiteGlue.sqlg_st_step(st);

    int colcount = SQLiteGlue.sqlg_st_column_count(st);
    android.util.Log.i("SQLiteGlueTest", "column count: " + colcount);

    String colname = SQLiteGlue.sqlg_st_column_name(st, 0);
    android.util.Log.i("SQLiteGlueTest", "column name: " + colname);

    int coltype = SQLiteGlue.sqlg_st_column_type(st, 0);
    android.util.Log.i("SQLiteGlueTest", "column type: " + coltype);

    String first = SQLiteGlue.sqlg_st_column_text(st, 0);

    android.util.Log.i("SQLiteGlueTest", "upper: " + first);

    SQLiteGlue.sqlg_st_finish(st);
    SQLiteGlue.sqlg_db_close(mydb);
  }
}