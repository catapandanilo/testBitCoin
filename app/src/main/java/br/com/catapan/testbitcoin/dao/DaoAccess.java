package br.com.catapan.testbitcoin.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.com.catapan.testbitcoin.model.Prices;

@Dao
public interface DaoAccess {
    @Insert
    void insertOnlySinglePrice (Prices prices);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertMultiplePrices (List<Prices> pricesList);

    @Query("SELECT * FROM Prices WHERE codeId = :codeId AND date = :date")
    Prices fetchOnePricesbyPriceId (int codeId, String date);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updatePrice (Prices prices);

    @Delete
    void deletePrice (Prices prices);
}
