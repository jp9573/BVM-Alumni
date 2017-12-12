package jaypatel.co.in.bvmalumni;

/**
 * Created by jay on 28/11/17.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class DataBaseAdapter{

    DataBaseHelper helper;
    Context context;
    DataBaseAdapter(Context context) {
        this.context = context;
        helper = new DataBaseHelper(context);
    }

    //Login
    public long insertUser(String email, String name, String mobile, String pass) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", "1");
        contentValues.put("email", email);
        contentValues.put("name", name);
        contentValues.put("mobile", mobile);
        contentValues.put("password", pass);
        db.execSQL("delete from " + helper.TABLE_USER);
        long id = db.insert(helper.TABLE_USER, null, contentValues);
        db.close();
        return id;
    }

    public void logout() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from " + helper.TABLE_USER);
        db.close();
    }

    public boolean isLoggedIn() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"email", "password"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        if(cursor.getCount() == 1)
            return true;
        else
            return false;
    }

    public String getCurrentUserName() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"name"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setUserName(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("name", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getCurrentEmail() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"email"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public String getPassword() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"password"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public String getDOB() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"dob"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setDOB(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("dob", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getMobile() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"mobile"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setMobile(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("mobile", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getOffice() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"office"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setOffice(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("office", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }


    public String getResidence() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"residence"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setResidence(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("residence", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getAddress() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"address"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setAddress(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("address", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }


    public long insertBitmap(Bitmap bmp) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if(bmp != null) {
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            byte[] buffer = out.toByteArray();
            SQLiteDatabase db = helper.getWritableDatabase();

            db.beginTransaction();
            ContentValues values;

            long id = 0;

            try {
                values = new ContentValues();
                values.put("profilePic", buffer);

                id = db.update(helper.TABLE_USER, values, "id=1", null);
                db.setTransactionSuccessful();
            } catch (SQLiteException e) {
                e.printStackTrace();

            } finally {
                db.endTransaction();
                db.close();
            }
            return id;
        }
        return 0;
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = null;
        SQLiteDatabase db = helper.getReadableDatabase();

        db.beginTransaction();
        try {
            String selectQuery = "SELECT profilePic FROM " + helper.TABLE_USER + " WHERE id=1";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("profilePic"));
                    if(blob != null)
                        bitmap = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                }

            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
        return bitmap;
    }

    public boolean isBitmapSet() {
        Bitmap bitmap = null;
        SQLiteDatabase db = helper.getReadableDatabase();

        db.beginTransaction();
        try {
            String selectQuery = "SELECT profilePic FROM " + helper.TABLE_USER + " WHERE id=1";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    byte[] blob = cursor.getBlob(cursor.getColumnIndex("profilePic"));
                    if(blob != null) {
                        if (blob.length > 0)
                            return true;
                        else
                            return false;
                    }else {
                        return false;
                    }
                }

            }
            db.setTransactionSuccessful();
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            db.endTransaction();
            db.close();
        }
        return false;
    }

    public long setYear(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("year", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getYear() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"year"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setBranch(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("branch", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getBranch() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"branch"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setNative(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("native", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getNative() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"native"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setJob(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("job", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getJob() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"job"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setCompany(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("company", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getCompany() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"company"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setDesignation(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("designation", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getDesignation() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"designation"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    public long setAnniversary(String data) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0;
        contentValues.put("anniversary", data);
        id = db.update(helper.TABLE_USER, contentValues, "id=1", null);
        db.close();
        return id;
    }

    public String getAnniversary() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] cols = {"anniversary"};
        Cursor cursor = db.query(helper.TABLE_USER, cols, null, null, null, null, null);
        StringBuffer buffer = new StringBuffer();
        while(cursor.moveToNext()) {
            String a1 = cursor.getString(0);
            buffer.append(a1);
        }
        db.close();
        return buffer.toString();
    }

    static class DataBaseHelper  extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "BVM_ALUMNI_DB";
        private static final int DATABASE_VERSION = 9;
        Context context;

        //Table Names
        private static final String TABLE_USER = "tbluser";

        //Login & AgentInfo
        private static final String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER +
                " (id VARCHAR(5), email VARCHAR(50), name VARCHAR(50), mobile VARCHAR(15), office VARCHAR(15), " +
                "residence VARCHAR(15), address VARCHAR(200), dob VARCHAR(20), profilePic BLOB, password VARCHAR(50)," +
                "year VARCHAR(50),branch VARCHAR(50),native VARCHAR(50),job VARCHAR(50),company VARCHAR(50)," +
                "designation VARCHAR(50),anniversary VARCHAR(50))";
        private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_USER;

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_USER_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_USER_TABLE);
            onCreate(db);
        }
    }

}