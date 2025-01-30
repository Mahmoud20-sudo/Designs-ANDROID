package com.smartapps.designdroid.database;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseAdapter extends SQLiteOpenHelper {

	@SuppressLint("SdCardPath")
	private static String DB_PATH = "/data/data/com.smaprtapps.designdroid/databases/";
	public static String DB_NAME = "DesignsApp.db";
	private SQLiteDatabase myDataBase;
	private final Context myContext;
	private static final int DATABASE_VERSION = 1;

	public DatabaseAdapter(Context context) {
		super(context, DB_NAME, null, DATABASE_VERSION);
		this.myContext = context;

	}

	public void createDataBase() {
		boolean dbExist = checkDataBase();
		if (dbExist) {
			Log.v("DB Exists", "db exists");
			this.getWritableDatabase();
		}
		dbExist = checkDataBase();
		if (!dbExist) {
			this.getReadableDatabase();
			try {
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
		}
		if (checkDB != null)
			checkDB.close();
		return checkDB != null ? true : false;
	}

	private void copyDataBase() throws IOException {

		InputStream myInput = myContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream myOutput = new FileOutputStream(outFileName);
		byte[] buffer = new byte[2048];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}
		myOutput.flush();
		myOutput.close();
		myInput.close();
	}

	public void openDataBase() throws SQLException {
		String myPath = DB_PATH + DB_NAME;
		myDataBase = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);
		Log.d("Test", "Database version: " + myDataBase.getVersion());
	}

	@Override
	public synchronized void close() {
		if (myDataBase != null)
			myDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion)
			Log.v("Database Upgrade", "Database version higher than old");
		myContext.deleteDatabase(DB_NAME);

	}

	public ArrayList<ArrayList<Object>> getCategory() {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase.rawQuery(
					"SELECT * FROM  Categories order by ID", null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// get installed subcategoies
	public ArrayList<ArrayList<Object>> getInstalledSubCategory() {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase
					.rawQuery(
							"SELECT * FROM SubCategory where Downloaded = 1 order by ID",
							null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// get subcategory from SubCategory tabel by type ID

	public ArrayList<ArrayList<Object>> getSubCategory(int typeID) {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase.rawQuery(
					"SELECT * FROM SubCategory where Downloaded = 0 AND CategoryID ="
							+ typeID, null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// insert into NewSubCategory row table()
	public void insertNewCategory(String SubCategoryName, int CategoryID,
			String thumb, String previewimg, int Subcatid, String ArabicNmae,
			int itemsCount, String folderURL) {

		openDataBase();
		myDataBase
				.execSQL("INSERT INTO SubCategory (SubCategoryName, CategoryID, Downloaded, Thumb, PreviewImg, SubCatID, ArabicName, itemsCount, folderURL ) VALUES('"
						+ SubCategoryName
						+ "', "
						+ CategoryID
						+ ", 0, '"
						+ thumb
						+ "', '"
						+ previewimg
						+ "' , "
						+ Subcatid
						+ ", '"
						+ ArabicNmae
						+ "', "
						+ itemsCount
						+ ", '"
						+ folderURL + "')");
		close();
	}

	// get sub category name
	public String getsubcategoryname(int categoryID) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT CategoryName FROM Categories where ID= " + categoryID,
				null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("CategoryName"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// get sub category arabic name
	public String getsubcategoryArabicName(String categoryname) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT ArabicName FROM SubCategory where SubCategoryName like '"
						+ categoryname + "%'", null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("ArabicName"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// get sub category English name
	public String getsubcategoryEnglishName(String categoryname) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT SubCategoryName FROM SubCategory where ArabicName like '"
						+ categoryname + "%'", null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("SubCategoryName"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// insert new item in SubCategory table(Fonts, frames, ...)
	public void insertNewIteminsubcategory(String TableName, String itemName,
			int typeCategoryID) {

		openDataBase();
		myDataBase.execSQL("INSERT INTO " + TableName
				+ " (ItemName, ItemType) VALUES('" + itemName + "',"
				+ typeCategoryID + " )");
		close();

	}

	// get subcategory ID
	public int checkSubcategoryID(int ID) {
		int rowscount;
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT SubCatID FROM SubCategory where SubCatID =" + ID, null);
		rowscount = cursorrows.getCount();
		cursorrows.close();
		close();
		return rowscount;
	}

	// get category name by id
	public String getcategorybyID(int ID) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT CategoryName FROM Categories where ID =" + ID, null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("CategoryName"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// update name of thumb and preview image for first time only
	public void updateimagpath(String imagetype, String path, int SubCatID) {
		openDataBase();
		myDataBase.execSQL("UPDATE SubCategory SET " + imagetype + "= '" + path
				+ "'  WHERE  SubCatID =" + SubCatID);
		close();
	}

	// get subcategory which allready downloaded

	public ArrayList<ArrayList<Object>> getSubCategorydownloaded(int categoryid) {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase.rawQuery(
					"SELECT SubCategoryName FROM SubCategory where CategoryID = "
							+ 1 + " and Downloaded = 1", null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// update thumb or preview image with its path in the device to database
	// imagetype = ThumbImg or PrevImg
	// path = the path for folder (shopimg) with the imagename.png or .jbg
	// SubCatID = the id for the subcategory
	public void insertimagepath(String imagetype, String path, int SubCatID) {
		openDataBase();
		myDataBase.execSQL("UPDATE SubCategory SET " + imagetype + "= '" + path
				+ "'  WHERE  SubCatID =" + SubCatID);
		close();
	}

	// update downloaded file flag in database to be installed
	public void updatedownloadeflag(int SubCatID) {
		openDataBase();
		myDataBase
				.execSQL("UPDATE SubCategory SET Downloaded = 1 Where SubCatID ="
						+ SubCatID);
		close();
	}

	// get all subcategoies
	public ArrayList<ArrayList<Object>> getAllSubCategory() {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase.rawQuery("SELECT * FROM SubCategory", null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// get all subcategoies
	public ArrayList<ArrayList<Object>> getServerSubCategory() {
		// create an ArrayList that will hold all of the data collected from
		// the database.
		ArrayList<ArrayList<Object>> dataArrays = new ArrayList<ArrayList<Object>>();
		Cursor cursor;
		try {
			// ask the database object to create the cursor.
			openDataBase();
			cursor = myDataBase.rawQuery(
					"SELECT * FROM SubCategory where Downloaded = 0", null);
			// move the cursor's pointer to position zero.
			cursor.moveToFirst();

			// if there is data after the current cursor position, add it
			// to the ArrayList.
			if (!cursor.isAfterLast()) {
				do {
					ArrayList<Object> dataList = new ArrayList<Object>();

					dataList.add(cursor.getString(0));
					dataList.add(cursor.getString(1));
					dataList.add(cursor.getString(2));
					dataList.add(cursor.getString(3));
					dataList.add(cursor.getString(4));
					dataList.add(cursor.getString(5));
					dataList.add(cursor.getString(6));
					dataList.add(cursor.getString(7));
					dataList.add(cursor.getString(8));
					dataList.add(cursor.getString(9));
					dataList.add(cursor.getString(10));
					dataList.add(cursor.getString(11));

					dataArrays.add(dataList);
				}
				// move the cursor's pointer up one position.
				while (cursor.moveToNext());
			}
		} catch (SQLException e) {
			Log.e("DB Error", e.toString());
			e.printStackTrace();
		}

		// return the ArrayList that holds the data collected from
		// the database.
		close();
		return dataArrays;
	}

	// removing zaker from repetition table
	public void deleterow(int ZakerID) {

		openDataBase();
		myDataBase.execSQL("DELETE from ZakerRepetition where ZakerID="
				+ ZakerID);
		close();

	}

	// get IntPreferences items
	public String getIntPreferences(String itemname) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT intValue FROM IntPreferences where Itemname like '"
						+ itemname + "'", null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("intValue"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// get StrPreferences items
	public String getStrPreferences(String itemname) {
		Cursor cursorrows;
		openDataBase();
		cursorrows = myDataBase.rawQuery(
				"SELECT StrValue FROM StringPreferences where ItemName like '"
						+ itemname + "'", null);
		String data = "";
		if (cursorrows.moveToFirst()) {
			do {
				data = cursorrows.getString(cursorrows
						.getColumnIndex("StrValue"));
				// do what ever you want here
			} while (cursorrows.moveToNext());
		}
		cursorrows.close();
		close();
		return data;
	}

	// get IntPreferences items
	public void updateIntPreferences(String itemname, int itmeValue) {
		openDataBase();
		myDataBase.execSQL("UPDATE IntPreferences SET intValue = " + itmeValue
				+ "  WHERE  Itemname like '" + itemname + "'");
		close();
	}

	// update StrPreferences items
	public void updateStrPreferences(String itemname, String itmeValue) {
		openDataBase();
		myDataBase.execSQL("UPDATE StringPreferences SET StrValue = '"
				+ itmeValue + "'  WHERE  ItemName like '" + itemname + "'");
		close();
	}

}