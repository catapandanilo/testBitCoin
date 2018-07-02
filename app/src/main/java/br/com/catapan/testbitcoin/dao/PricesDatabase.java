package br.com.catapan.testbitcoin.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import br.com.catapan.testbitcoin.model.Prices;

@Database(entities = {Prices.class}, version = 4, exportSchema = false)

public abstract class PricesDatabase extends RoomDatabase {
    public abstract DaoAccess daoAccess();
}

