package ru.ilinykh_ie.currencyConverter.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DailyCourse {
    public final static String NAME = "Name";
    public final static String CHAR_CODE = "CharCode";
    public final static String NOMINAL = "Nominal";
    public final static String VALUE = "Value";
    public final static String VALUTE = "Valute";
    public URL url;

    public DailyCourse() {
        try {
            url = new URL("https://www.cbr.ru/scripts/XML_daily.asp");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public InputStream getInputStream() throws IOException {
        return url.openConnection().getInputStream();
    }

    public ArrayList<Currency> getCurrencyList() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        ArrayList<Currency> currencyList = new ArrayList<>();

        DocumentBuilder builder = factory.newDocumentBuilder();
        Document dom = builder.parse(getInputStream());

        Element element = dom.getDocumentElement();
        NodeList items = element.getElementsByTagName(VALUTE);

        for (int i = 0; i < items.getLength(); i++) {
            Currency currency = new Currency();

            Node item = items.item(i);
            NodeList properties = item.getChildNodes();

            for (int j = 0; j < properties.getLength(); j++) {
                Node property = properties.item(j);
                String name = property.getNodeName();

                if (name.equalsIgnoreCase(NAME)) {
                    currency.setName(property.getFirstChild().getNodeValue());
                } else if (name.equalsIgnoreCase(VALUE)) {
                    currency.setValue(Double.parseDouble(property.getFirstChild().getNodeValue().replace(",", ".")));
                } else if (name.equalsIgnoreCase(NOMINAL)) {
                    currency.setNominal(Integer.parseInt(property.getFirstChild().getNodeValue()));
                } else if (name.equalsIgnoreCase(CHAR_CODE)) {
                    currency.setCharCode(property.getFirstChild().getNodeValue());
                }
            }
            currencyList.add(currency);
        }

        Currency currency = new Currency();
        currency.setName("Российский рубль");
        currency.setNominal(1);
        currency.setCharCode("RUB");
        currency.setValue(1);

        currencyList.add(0, currency);

        return currencyList;
    }
}