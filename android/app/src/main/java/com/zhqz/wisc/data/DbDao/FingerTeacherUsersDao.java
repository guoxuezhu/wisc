package com.zhqz.wisc.data.DbDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhqz.wisc.data.model.FingerTeacherUsers;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "FINGER_TEACHER_USERS".
*/
public class FingerTeacherUsersDao extends AbstractDao<FingerTeacherUsers, Void> {

    public static final String TABLENAME = "FINGER_TEACHER_USERS";

    /**
     * Properties of entity FingerTeacherUsers.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, int.class, "id", false, "ID");
        public final static Property Stid = new Property(1, int.class, "stid", false, "STID");
        public final static Property PsType = new Property(2, int.class, "psType", false, "PS_TYPE");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Context = new Property(4, String.class, "context", false, "CONTEXT");
        public final static Property SchoolId = new Property(5, int.class, "schoolId", false, "SCHOOL_ID");
        public final static Property Ts = new Property(6, String.class, "ts", false, "TS");
        public final static Property Status = new Property(7, int.class, "status", false, "STATUS");
        public final static Property Number = new Property(8, String.class, "number", false, "NUMBER");
    }


    public FingerTeacherUsersDao(DaoConfig config) {
        super(config);
    }
    
    public FingerTeacherUsersDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"FINGER_TEACHER_USERS\" (" + //
                "\"ID\" INTEGER NOT NULL ," + // 0: id
                "\"STID\" INTEGER NOT NULL ," + // 1: stid
                "\"PS_TYPE\" INTEGER NOT NULL ," + // 2: psType
                "\"NAME\" TEXT," + // 3: name
                "\"CONTEXT\" TEXT," + // 4: context
                "\"SCHOOL_ID\" INTEGER NOT NULL ," + // 5: schoolId
                "\"TS\" TEXT," + // 6: ts
                "\"STATUS\" INTEGER NOT NULL ," + // 7: status
                "\"NUMBER\" TEXT);"); // 8: number
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"FINGER_TEACHER_USERS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, FingerTeacherUsers entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getStid());
        stmt.bindLong(3, entity.getPsType());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String context = entity.getContext();
        if (context != null) {
            stmt.bindString(5, context);
        }
        stmt.bindLong(6, entity.getSchoolId());
 
        String ts = entity.getTs();
        if (ts != null) {
            stmt.bindString(7, ts);
        }
        stmt.bindLong(8, entity.getStatus());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(9, number);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, FingerTeacherUsers entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getStid());
        stmt.bindLong(3, entity.getPsType());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
 
        String context = entity.getContext();
        if (context != null) {
            stmt.bindString(5, context);
        }
        stmt.bindLong(6, entity.getSchoolId());
 
        String ts = entity.getTs();
        if (ts != null) {
            stmt.bindString(7, ts);
        }
        stmt.bindLong(8, entity.getStatus());
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(9, number);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public FingerTeacherUsers readEntity(Cursor cursor, int offset) {
        FingerTeacherUsers entity = new FingerTeacherUsers( //
            cursor.getInt(offset + 0), // id
            cursor.getInt(offset + 1), // stid
            cursor.getInt(offset + 2), // psType
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // name
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // context
            cursor.getInt(offset + 5), // schoolId
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // ts
            cursor.getInt(offset + 7), // status
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // number
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, FingerTeacherUsers entity, int offset) {
        entity.setId(cursor.getInt(offset + 0));
        entity.setStid(cursor.getInt(offset + 1));
        entity.setPsType(cursor.getInt(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setContext(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSchoolId(cursor.getInt(offset + 5));
        entity.setTs(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setStatus(cursor.getInt(offset + 7));
        entity.setNumber(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(FingerTeacherUsers entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(FingerTeacherUsers entity) {
        return null;
    }

    @Override
    public boolean hasKey(FingerTeacherUsers entity) {
        // TODO
        return false;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
