package ru.ilinykh_ie.currencyConverter.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import ru.ilinykh_ie.currencyConverter.R;
import ru.ilinykh_ie.currencyConverter.model.Currency;
import ru.ilinykh_ie.currencyConverter.model.DailyCourse;

public class SplashActivity extends AppCompatActivity {
    private ArrayList<Currency> currencyList;
    public static final String EXTRA_LIST = "package ru.ilinykh_ie.currencyConverter.view.LIST";
    private final DailyCourse dailyCourse = new DailyCourse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AsyncRequest asyncRequest = new AsyncRequest();
        asyncRequest.execute();

        while (currencyList == null) {
            Thread t = Thread.currentThread();
            try {
                t.join(500);
                currencyList = asyncRequest.getList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent answerIntent = new Intent();
            answerIntent.putExtra(EXTRA_LIST, currencyList);
            setResult(RESULT_OK, answerIntent);
            finish();
        }, 500);

    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    class AsyncRequest extends AsyncTask<Void, Void, ArrayList<Currency>> {
        private ArrayList<Currency> list = null;

        @Override
        protected ArrayList<Currency> doInBackground(Void... voids) {
            try {
                list = dailyCourse.getCurrencyList();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            return list;
        }

        @Override
        protected void onPostExecute(ArrayList<Currency> currencies) {
            list = currencies;
        }

        public ArrayList<Currency> getList() {
            return list;
        }
    }
}
