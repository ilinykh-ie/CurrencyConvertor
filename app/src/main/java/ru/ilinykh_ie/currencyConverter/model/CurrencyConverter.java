package ru.ilinykh_ie.currencyConverter.model;

import java.util.ArrayList;

public class CurrencyConverter {
    ArrayList<Currency> list;

    public CurrencyConverter(ArrayList<Currency> list) {
        this.list = list;
    }

    public double convert(double amount, int currencyFrom, int currencyTo) {
        if (currencyFrom < 0 || currencyTo < 0 || amount < 0) {
            return -1;
        }

        double rubles = list.get(currencyFrom).getRubleFromThis(amount);

        return list.get(currencyTo).getThisFromRuble(rubles);
    }
}
