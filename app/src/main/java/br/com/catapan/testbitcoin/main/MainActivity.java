package br.com.catapan.testbitcoin.main;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.catapan.testbitcoin.R;
import br.com.catapan.testbitcoin.model.Prices;
import br.com.catapan.testbitcoin.utils.Utils;

public class MainActivity extends AppCompatActivity implements MainContract.View {

    private ProgressDialog progressDialog1;

    private TextView txtDate;
    private TextView txtCode;
    private TextView txtRate;
    private TextView txtDesc;
    private TextView txtDateHour;

    private MainContract.Presenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtDate = findViewById(R.id.txtDate);
        txtCode = findViewById(R.id.txtCode);
        txtDesc = findViewById(R.id.txtDesc);
        txtDateHour = findViewById(R.id.txtDateHour);
        txtRate = findViewById(R.id.txtRate);

        mainPresenter = new MainPresenter(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void loadBPI() {
        mainPresenter.loadBPI();
    }

    private void loadBlock() {
        mainPresenter.loadBlock();
    }

    private void updateComponents(final Prices prices) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Calendar c = Calendar.getInstance();
                txtDate.setText(prices.getDate());
                txtDateHour.setText(Utils.getSimpleDateFormat().format(c.getTime()));
                txtCode.setText(prices.getCodeId());
                txtRate.setText(String.format(" %s %s", getString(R.string.dollar), Utils.getDecimalFormat().format(prices.getRate())));
                txtDesc.setText(prices.getDescription());
            }
        });
    }

    @Override
    public void parseBpiResponse(Prices prices) {
        progressDialog1.dismiss();

        updateComponents(prices);
    }

    @Override
    public void parseBlockResponse(List<Entry> entries) {
        LineDataSet dataSet = new LineDataSet(entries, getString(R.string.ultimos_15_dias));
        LineData lineData = new LineData(dataSet);

        LineChart chart = findViewById(R.id.chart);
        chart.getDescription().setText("");
        chart.setData(lineData);
        chart.invalidate();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(MainActivity.this, getString(R.string.erro_carregar_api_bpi)
                + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(int message) {
        if (progressDialog1 == null) {
            progressDialog1 = new ProgressDialog(this);
        }
        progressDialog1.setTitle(getString(message));
        progressDialog1.setMessage(getString(R.string.aguarde));
        progressDialog1.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog1.isShowing())
            progressDialog1.dismiss();
    }

    private void refresh() {
        loadBPI();
        loadBlock();
    }

}
