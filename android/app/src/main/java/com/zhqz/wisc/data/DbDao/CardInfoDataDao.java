package com.zhqz.wisc.data.DbDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.zhqz.wisc.data.model.CardInfoData;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CARD_INFO_DATA".
*/
public class CardInfoDataDao extends AbstractDao<CardInfoData, Long> {

    public static final String TABLENAME = "CARD_INFO_DATA";

    /**
     * Properties of entity CardInfoData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property CdId = new Property(0, long.class, "cdId", true, "_id");
        public final static Property CustomerId = new Property(1, int.class, "customerId", false, "CUSTOMER_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Number = new Property(3, String.class, "number", false, "NUMBER");
        public final static Property ClassName = new Property(4, String.class, "className", false, "CLASS_NAME");
        public final static Property CardNum = new Property(5, String.class, "cardNum", false, "CARD_NUM");
        public final static Property ZaoSumNum = new Property(6, int.class, "zaoSumNum", false, "ZAO_SUM_NUM");
        public final static Property ZaototalNum = new Property(7, int.class, "zaototalNum", false, "ZAOTOTAL_NUM");
        public final static Property ZaolimitNum = new Property(8, int.class, "zaolimitNum", false, "ZAOLIMIT_NUM");
        public final static Property ZhongSumNum = new Property(9, int.class, "zhongSumNum", false, "ZHONG_SUM_NUM");
        public final static Property ZhongtotalNum = new Property(10, int.class, "zhongtotalNum", false, "ZHONGTOTAL_NUM");
        public final static Property ZhonglimitNum = new Property(11, int.class, "zhonglimitNum", false, "ZHONGLIMIT_NUM");
        public final static Property WanSumNum = new Property(12, int.class, "wanSumNum", false, "WAN_SUM_NUM");
        public final static Property WantotalNum = new Property(13, int.class, "wantotalNum", false, "WANTOTAL_NUM");
        public final static Property WanlimitNum = new Property(14, int.class, "wanlimitNum", false, "WANLIMIT_NUM");
    }


    public CardInfoDataDao(DaoConfig config) {
        super(config);
    }
    
    public CardInfoDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CARD_INFO_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: cdId
                "\"CUSTOMER_ID\" INTEGER NOT NULL ," + // 1: customerId
                "\"NAME\" TEXT," + // 2: name
                "\"NUMBER\" TEXT," + // 3: number
                "\"CLASS_NAME\" TEXT," + // 4: className
                "\"CARD_NUM\" TEXT," + // 5: cardNum
                "\"ZAO_SUM_NUM\" INTEGER NOT NULL ," + // 6: zaoSumNum
                "\"ZAOTOTAL_NUM\" INTEGER NOT NULL ," + // 7: zaototalNum
                "\"ZAOLIMIT_NUM\" INTEGER NOT NULL ," + // 8: zaolimitNum
                "\"ZHONG_SUM_NUM\" INTEGER NOT NULL ," + // 9: zhongSumNum
                "\"ZHONGTOTAL_NUM\" INTEGER NOT NULL ," + // 10: zhongtotalNum
                "\"ZHONGLIMIT_NUM\" INTEGER NOT NULL ," + // 11: zhonglimitNum
                "\"WAN_SUM_NUM\" INTEGER NOT NULL ," + // 12: wanSumNum
                "\"WANTOTAL_NUM\" INTEGER NOT NULL ," + // 13: wantotalNum
                "\"WANLIMIT_NUM\" INTEGER NOT NULL );"); // 14: wanlimitNum
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CARD_INFO_DATA\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CardInfoData entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getCdId());
        stmt.bindLong(2, entity.getCustomerId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(5, className);
        }
 
        String cardNum = entity.getCardNum();
        if (cardNum != null) {
            stmt.bindString(6, cardNum);
        }
        stmt.bindLong(7, entity.getZaoSumNum());
        stmt.bindLong(8, entity.getZaototalNum());
        stmt.bindLong(9, entity.getZaolimitNum());
        stmt.bindLong(10, entity.getZhongSumNum());
        stmt.bindLong(11, entity.getZhongtotalNum());
        stmt.bindLong(12, entity.getZhonglimitNum());
        stmt.bindLong(13, entity.getWanSumNum());
        stmt.bindLong(14, entity.getWantotalNum());
        stmt.bindLong(15, entity.getWanlimitNum());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CardInfoData entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getCdId());
        stmt.bindLong(2, entity.getCustomerId());
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String number = entity.getNumber();
        if (number != null) {
            stmt.bindString(4, number);
        }
 
        String className = entity.getClassName();
        if (className != null) {
            stmt.bindString(5, className);
        }
 
        String cardNum = entity.getCardNum();
        if (cardNum != null) {
            stmt.bindString(6, cardNum);
        }
        stmt.bindLong(7, entity.getZaoSumNum());
        stmt.bindLong(8, entity.getZaototalNum());
        stmt.bindLong(9, entity.getZaolimitNum());
        stmt.bindLong(10, entity.getZhongSumNum());
        stmt.bindLong(11, entity.getZhongtotalNum());
        stmt.bindLong(12, entity.getZhonglimitNum());
        stmt.bindLong(13, entity.getWanSumNum());
        stmt.bindLong(14, entity.getWantotalNum());
        stmt.bindLong(15, entity.getWanlimitNum());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public CardInfoData readEntity(Cursor cursor, int offset) {
        CardInfoData entity = new CardInfoData( //
            cursor.getLong(offset + 0), // cdId
            cursor.getInt(offset + 1), // customerId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // number
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // className
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // cardNum
            cursor.getInt(offset + 6), // zaoSumNum
            cursor.getInt(offset + 7), // zaototalNum
            cursor.getInt(offset + 8), // zaolimitNum
            cursor.getInt(offset + 9), // zhongSumNum
            cursor.getInt(offset + 10), // zhongtotalNum
            cursor.getInt(offset + 11), // zhonglimitNum
            cursor.getInt(offset + 12), // wanSumNum
            cursor.getInt(offset + 13), // wantotalNum
            cursor.getInt(offset + 14) // wanlimitNum
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CardInfoData entity, int offset) {
        entity.setCdId(cursor.getLong(offset + 0));
        entity.setCustomerId(cursor.getInt(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setNumber(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setClassName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCardNum(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setZaoSumNum(cursor.getInt(offset + 6));
        entity.setZaototalNum(cursor.getInt(offset + 7));
        entity.setZaolimitNum(cursor.getInt(offset + 8));
        entity.setZhongSumNum(cursor.getInt(offset + 9));
        entity.setZhongtotalNum(cursor.getInt(offset + 10));
        entity.setZhonglimitNum(cursor.getInt(offset + 11));
        entity.setWanSumNum(cursor.getInt(offset + 12));
        entity.setWantotalNum(cursor.getInt(offset + 13));
        entity.setWanlimitNum(cursor.getInt(offset + 14));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CardInfoData entity, long rowId) {
        entity.setCdId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CardInfoData entity) {
        if(entity != null) {
            return entity.getCdId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CardInfoData entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
