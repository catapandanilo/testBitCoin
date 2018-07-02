package br.com.catapan.testbitcoin.main;

import com.github.mikephil.charting.data.Entry;

import java.util.List;

import br.com.catapan.testbitcoin.model.Prices;
import okhttp3.Request;

public interface MainContract {

    interface View {
        void showLoading(int message);
        void hideLoading();
        void showErrorMessage(String message);
        void parseBpiResponse(Prices price);
        void parseBlockResponse(List<Entry> entries);
    }

    interface Presenter {
        void loadBPI();
        void loadBlock();
        Request getRequest(String url);
    }

}
