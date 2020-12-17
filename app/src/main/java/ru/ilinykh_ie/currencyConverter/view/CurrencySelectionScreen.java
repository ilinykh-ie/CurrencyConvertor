package ru.ilinykh_ie.currencyConverter.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ru.ilinykh_ie.currencyConverter.R;
import ru.ilinykh_ie.currencyConverter.model.Currency;

public class CurrencySelectionScreen extends AppCompatActivity implements View.OnClickListener {
    private ListView listOfCurrencies;
    public final static String SELECTED_CURRENCY_NUMBER = "package ru.ilinykh_ie.currencyConverter.view.CURRENCY_NUMBER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_selection_screen);

        Intent intent = getIntent();

        ArrayList<Currency> currencyList = intent.getExtras().getParcelableArrayList(MainActivity.CURRENCY_LIST);
        String[] currencies = new String[currencyList.size()];

        for (int i = 0; i < currencyList.size(); i++) {
            currencies[i] = currencyList.get(i).toString().replaceAll("их", "ий").replaceAll("ов", "");
        }

        listOfCurrencies = findViewById(R.id.listOfCurrencies);
        listOfCurrencies.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, currencies);
        listOfCurrencies.setAdapter(adapter);

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);
    }

    public void closeSelectionScreen(View view) {
        finish();
    }

    @Override
    public void onClick(View view) {
        int result = listOfCurrencies.getCheckedItemPosition();

        if (result != -1) {
            Intent intent = new Intent();
            intent.putExtra(SELECTED_CURRENCY_NUMBER, result);
            setResult(RESULT_OK, intent);
        }

        finish();
    }
}