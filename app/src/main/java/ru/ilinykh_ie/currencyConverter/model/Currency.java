package ru.ilinykh_ie.currencyConverter.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Currency implements Parcelable {
    public String name;
    public int nominal;
    public double value;
    public String charCode;

    public Currency() {
    }

    protected Currency(Parcel in) {
        name = in.readString();
        nominal = in.readInt();
        value = in.readDouble();
        charCode = in.readString();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(nominal);
        dest.writeDouble(value);
        dest.writeString(charCode);
    }

    @NonNull
    @Override
    public String toString() {
        return name + " (" + charCode + ")";
    }

    public double getRubleFromThis(double amount) {
        return amount * (value / nominal);
    }

    public double getThisFromRuble(double amount) {
        return amount / (value / nominal);
    }
}
