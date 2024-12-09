package com.vlteam.vlxbookapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleDatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1; // Phiên bản cơ sở dữ liệu
    private static final String DATABASE_NAME = "example.db"; // Tên cơ sở dữ liệu

    public SimpleDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng cơ sở dữ liệu khi lần đầu tiên khởi chạy
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Nâng cấp cơ sở dữ liệu khi phiên bản thay đổi
        db.execSQL("DROP TABLE IF EXISTS my_table"); // Ví dụ xóa bảng
        onCreate(db); // Tạo lại bảng
    }

    public void createSimpleTable(String tableName, List<String> dataStruct) {
        SQLiteDatabase db = this.getWritableDatabase();
        StringBuilder queryBuilder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");
        for (String column : dataStruct) {
            queryBuilder.append(column).append(",");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        queryBuilder.append(");");

        db.execSQL(queryBuilder.toString());
        db.close();
    }

    public void dropTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        db.close();
    }

    public void save(String tableName, Map<String, Object> data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            if (entry.getValue() instanceof String) {
                values.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                values.put(entry.getKey(), (Integer) entry.getValue());
            } else if (entry.getValue() instanceof Double) {
                values.put(entry.getKey(), (Double) entry.getValue());
            }
        }

        db.insert(tableName, null, values);
        db.close();
    }

    public Object getDataByKey(String tableName, String columnName, String keyColumn, String keyValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(tableName, new String[]{columnName}, keyColumn + "=?",
                new String[]{keyValue}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Object result = cursor.getString(0);
            cursor.close();
            return result;
        }

        return null;
    }

    public void updateDataByColumn(String tableName, String columnName, Object newValue, String keyColumn, Object key) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (newValue instanceof String) {
            values.put(columnName, (String) newValue);
        } else if (newValue instanceof Integer) {
            values.put(columnName, (Integer) newValue);
        } else if (newValue instanceof Double) {
            values.put(columnName, (Double) newValue);
        }

        db.update(tableName, values, keyColumn + " = ?", new String[]{key.toString()});
        db.close();
    }
    public void insert(String tableName, Map<String, Object> data) {
        try (SQLiteDatabase database = getWritableDatabase()) {

            // Tạo danh sách cột và placeholders
            StringBuilder columns = new StringBuilder();
            StringBuilder placeholders = new StringBuilder();

            for (String column : data.keySet()) {
                columns.append(column).append(",");
                placeholders.append("?,");
            }

            // Loại bỏ dấu phẩy cuối cùng
            columns.deleteCharAt(columns.length() - 1);
            placeholders.deleteCharAt(placeholders.length() - 1);

            // Câu lệnh SQL INSERT
            String query = "INSERT INTO " + tableName + " (" + columns + ") VALUES (" + placeholders + ")";

            // Chuẩn bị và gán giá trị
            SQLiteStatement statement = database.compileStatement(query);
            int index = 1;
            for (Object value : data.values()) {
                if (value instanceof String) {
                    statement.bindString(index, (String) value);
                } else if (value instanceof Integer) {
                    statement.bindLong(index, (Integer) value);
                } else if (value instanceof Double) {
                    statement.bindDouble(index, (Double) value);
                } else if (value == null) {
                    statement.bindNull(index);
                }
                index++;
            }

            // Thực thi câu lệnh
            statement.executeInsert();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Object> getColumnValues(String tableName, String columnName) {
        SQLiteDatabase database = null;
        List<Object> result = new ArrayList<>();

        try {
            database = getReadableDatabase();

            // Truy vấn lấy tất cả giá trị của cột
            String query = "SELECT " + columnName + " FROM " + tableName;
            Cursor cursor = database.rawQuery(query, null);

            // Lấy dữ liệu từ Cursor và thêm vào danh sách
            if (cursor.moveToFirst()) {
                do {
                    int columnIndex = cursor.getColumnIndex(columnName);
                    int columnType = cursor.getType(columnIndex);

                    switch (columnType) {
                        case Cursor.FIELD_TYPE_STRING:
                            result.add(cursor.getString(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_INTEGER:
                            result.add(cursor.getInt(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            result.add(cursor.getDouble(columnIndex));
                            break;
                        case Cursor.FIELD_TYPE_NULL:
                            result.add(null);
                            break;
                    }
                } while (cursor.moveToNext());
            }

            // Đóng Cursor
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (database != null) {
                database.close();
            }
        }

        return result;
    }

}
