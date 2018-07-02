package br.com.catapan.testbitcoin.main;

import android.content.Context;
import android.support.annotation.NonNull;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.com.catapan.testbitcoin.R;
import br.com.catapan.testbitcoin.model.Prices;
import br.com.catapan.testbitcoin.utils.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainPresenter implements MainContract.Presenter {

    private OkHttpClient okHttpClient = new OkHttpClient();
    private MainRepository mainRepository;

    private MainContract.View mainView;

    MainPresenter(Context context, MainContract.View view) {
        mainRepository = new MainRepository(context);
        mainView = view;
    }

    @Override
    public Request getRequest(String url) {
        return mainRepository.getRequest(url);
    }

    @Override
    public void loadBPI() {
        mainView.showLoading(R.string.carregando_api_bpi);

        Request request = getRequest(MainRepository.API_BPI);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainView.hideLoading();
                mainView.showErrorMessage(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                mainView.hideLoading();

                Prices price = null;

                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONObject timeObject = jsonObject.getJSONObject("time");
                    Date date = new Date(timeObject.getString("updated"));
                    Locale br = new Locale("pt", "BR");
                    DateFormat df1 = DateFormat.getDateInstance(DateFormat.FULL, br);

                    JSONObject bpiObject = jsonObject.getJSONObject("bpi");
                    JSONObject object = bpiObject.getJSONObject("USD");

                    price = new Prices();
                    price.setDate(df1.format(date));
                    price.setCodeId(object.getString("code"));
                    price.setRate(object.getDouble("rate_float"));
                    price.setDescription(object.getString("description"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (price != null) {
                    mainView.parseBpiResponse(price);
                    mainRepository.persistencePrices(price);
                }
            }
        });
    }

    @Override
    public void loadBlock() {
        mainView.showLoading(R.string.carregando_api_block);

        Request request = getRequest(MainRepository.API_BLOCK);
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                mainView.hideLoading();
                mainView.showErrorMessage(e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response)
                    throws IOException {
                mainView.hideLoading();

                List<Entry> entries = new ArrayList<>();

                String body = Objects.requireNonNull(response.body()).string();
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    JSONArray values = jsonObject.getJSONArray("values");

                    try {
                        int last15Days = Utils.getLast15Days();
                        for (int i = 0; i < values.length(); i++) {
                            JSONObject object = values.getJSONObject(i);
                            entries.add(new Entry(last15Days++, Float.parseFloat(Utils.getDecimalFormat().format(object.getDouble("y")))));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mainView.parseBlockResponse(entries);
            }
        });
    }

}
