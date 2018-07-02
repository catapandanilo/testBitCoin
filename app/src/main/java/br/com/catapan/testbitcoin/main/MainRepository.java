package br.com.catapan.testbitcoin.main;

import android.arch.persistence.room.Room;
import android.content.Context;

import br.com.catapan.testbitcoin.dao.PricesDatabase;
import br.com.catapan.testbitcoin.model.Prices;
import okhttp3.Request;

class MainRepository {

    static final String API_BPI = "https://api.coindesk.com/v1/bpi/currentprice.json";
    static final String API_BLOCK = "https://api.blockchain.info/charts/market-price?timespan=15days&format=json";

    private static final String DATABASE_NAME = "pricesDB";

    private PricesDatabase pricesDatabase;

    MainRepository(Context context) {
        pricesDatabase = Room.databaseBuilder(context,
                PricesDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration().build();
    }

    void persistencePrices(Prices price) {
        pricesDatabase.daoAccess().deletePrice(price);
        pricesDatabase.daoAccess().insertOnlySinglePrice(price);
    }

    Request getRequest(String url) {
        return new Request.Builder().url(url).build();
    }

}
