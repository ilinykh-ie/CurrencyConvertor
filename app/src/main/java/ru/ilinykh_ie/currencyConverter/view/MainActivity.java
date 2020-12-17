package ru.ilinykh_ie.currencyConverter.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

import ru.ilinykh_ie.currencyConverter.R;
import ru.ilinykh_ie.currencyConverter.model.Currency;
import ru.ilinykh_ie.currencyConverter.model.CurrencyConverter;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Currency> currencyList;
    private final int LIST = 0;
    private final int SELECTED_CURRENCY_FROM = 1;
    private final int SELECTED_CURRENCY_TO = 2;
    public static final String CURRENCY_LIST = "package ru.ilinykh_ie.currencyConverter.view.CURRENCY_LIST";
    private int currencyNumberFrom;
    private int currencyNumberTo;
    private CurrencyConverter currencyConverter;
    private boolean isDefaultValuesSetted;
    private boolean isEditing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.refreshButton).performClick();

        EditText convertFrom = findViewById(R.id.vauleConvertFrom);

        convertFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEditing) {
                    return;
                }

                isEditing = true;
                calculateTo();
                isEditing = false;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText convertTo = findViewById(R.id.vauleConvertTo);

        convertTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isEditing) {
                    return;
                }

                isEditing = true;
                calculateFrom();
                isEditing = false;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isDefaultValuesSetted) {
            return;
        }

        if (currencyList != null) {
            currencyNumberFrom = 0;
            TextView textViewFrom = findViewById(R.id.convertFrom);
            textViewFrom.setText(currencyList.get(currencyNumberFrom).charCode);

            int size = currencyList.size();

            for (int i = 0; i < size; i++) {
                if (currencyList.get(i).charCode.equals("USD")) {
                    currencyNumberTo = i;
                    break;
                }
            }

            ((TextView) findViewById(R.id.convertTo)).setText(currencyList.get(currencyNumberTo).charCode);

            isDefaultValuesSetted = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case LIST:
                    currencyList = intent.getExtras().getParcelableArrayList(SplashActivity.EXTRA_LIST);
                    currencyConverter = new CurrencyConverter(currencyList);

                    break;
                case SELECTED_CURRENCY_FROM:
                    currencyNumberFrom = intent.getIntExtra(
                            CurrencySelectionScreen.SELECTED_CURRENCY_NUMBER, 0);

                    TextView textViewFrom = findViewById(R.id.convertFrom);
                    textViewFrom.setText(currencyList.get(currencyNumberFrom).charCode);

                    if (!isEditing) {
                        isEditing = true;
                        calculateFrom();
                        isEditing = false;
                    }
                    break;
                case SELECTED_CURRENCY_TO:
                    currencyNumberTo = intent.getIntExtra(
                            CurrencySelectionScreen.SELECTED_CURRENCY_NUMBER, 0);

                    TextView textViewTo = findViewById(R.id.convertTo);
                    textViewTo.setText(currencyList.get(currencyNumberTo).charCode);

                    if (!isEditing) {
                        isEditing = true;
                        calculateTo();
                        isEditing = false;
                    }

                    break;
            }
        }
    }

    public void refresh(View view) {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivityForResult(intent, LIST);
    }

    public void openSelectionScreenFrom(View view) {
        Intent intent = new Intent(this, CurrencySelectionScreen.class);
        intent.putParcelableArrayListExtra(CURRENCY_LIST, currencyList);
        startActivityForResult(intent, SELECTED_CURRENCY_FROM);
    }

    public void openSelectionScreenTo(View view) {
        Intent intent = new Intent(this, CurrencySelectionScreen.class);
        intent.putParcelableArrayListExtra(CURRENCY_LIST, currencyList);
        startActivityForResult(intent, SELECTED_CURRENCY_TO);
    }

    public void calculateTo() {
        EditText valueConvertFrom = findViewById(R.id.vauleConvertFrom);
        EditText valueConvertTo = findViewById(R.id.vauleConvertTo);

        if (valueConvertFrom.getText().toString().equals("")) {
            valueConvertTo.getText().clear();
            isEditing = false;

            return;
        }

        double result = currencyConverter.convert(Double.parseDouble(valueConvertFrom.getText()
                .toString()), currencyNumberFrom, currencyNumberTo);

        valueConvertTo.setText(String.format(Locale.ENGLISH, "%.2f", result));
    }

    public void calculateFrom() {
        EditText valueConvertFrom = findViewById(R.id.vauleConvertFrom);
        EditText valueConvertTo = findViewById(R.id.vauleConvertTo);

        if (valueConvertTo.getText().toString().equals("")) {
            valueConvertFrom.getText().clear();
            isEditing = false;

            return;
        }

        double result = currencyConverter.convert(Double.parseDouble(valueConvertTo.getText()
                .toString()), currencyNumberTo, currencyNumberFrom);

        valueConvertFrom.setText(String.format(Locale.ENGLISH, "%.2f", result));
    }

    public void changeFromTo(View view) {
        if (currencyList == null) {
            return;
        }

        int tempInt = currencyNumberFrom;
        currencyNumberFrom = currencyNumberTo;
        currencyNumberTo = tempInt;

        isEditing = true;

        TextView textViewFrom = findViewById(R.id.convertFrom);
        textViewFrom.setText(currencyList.get(currencyNumberFrom).charCode);

        TextView textViewTo = findViewById(R.id.convertTo);
        textViewTo.setText(currencyList.get(currencyNumberTo).charCode);

        EditText valueConvertFrom = findViewById(R.id.vauleConvertFrom);
        EditText valueConvertTo = findViewById(R.id.vauleConvertTo);

        String tempValue = valueConvertFrom.getText().toString();
        valueConvertFrom.setText(valueConvertTo.getText().toString());
        valueConvertTo.setText(tempValue);

        isEditing = false;
    }
}