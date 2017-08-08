package com.jkrajniak.almamenu.almaparser;

import android.util.Log;

import com.jkrajniak.almamenu.preferences.AppPreferences;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/***
 * Main class to parse the data from the Alma web page.
 */
public class AlmaParser {
    private static final String TAG = AppPreferences.APPLOG;

    public static ArrayList<AlmaMenu> parse(String webUrl, int almaCode) throws
            URISyntaxException, ParseException, IOException {

        URL url = new URL(webUrl);
        url.openConnection();


        Document docj = Jsoup.connect(url.toURI().toString()).get();
        ArrayList<AlmaMenu> menus = new ArrayList<AlmaMenu>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", new Locale("nl"));

        // Parse data for today. #block-2-1 > div > div #block-2-1 > div > div > div
        Elements todayItems = docj.select("div#block-2-1 > div > div > div");
        if (todayItems.size() > 0) {
            Date todayDate = dateFormat.parse(todayItems.get(0).text().trim());

            AlmaMenu am = new AlmaMenu(almaCode, todayDate);
            menus.add(am);

            Elements todayMenuElements = docj.select("div#block-2-1 tr");
            int elementIndex = 0;
            for (Element trEl : todayMenuElements) {
                AlmaMenuItem item = new AlmaMenuItem();
                item.name = trEl.select("td.table__text").text();
                item.isVeggie = trEl.select("td.table__label").text().toLowerCase().equals("veggie");
                item.price = trEl.select("td.table__price").text().trim().replace(" ", "").replace("€", "").replace(",", ".");
                am.menuItems.add(item);
            }
        }

        // Parse data for following days.
        Elements dayItems = docj.select("div#block-1-1 > div");

        for (Element dayItem : dayItems) {
            Elements dayEls = dayItem.select("div > div > div > div");
            if (dayEls.size() == 0)
                continue;

            Element dayEl = dayEls.get(0);
            Date menuDate = dateFormat.parse(dayEl.text().trim());

            AlmaMenu am = new AlmaMenu(3, menuDate);
            menus.add(am);

            // Fetch the menu per day.
            Elements tr = dayItem.select("table > tbody > tr");

            int elementIndex = 0;
            for (Element trEl : tr) {
                AlmaMenuItem item = new AlmaMenuItem();
                item.name = trEl.select("td.table__text").text();
                item.isVeggie = trEl.select("td.table__label").text().toLowerCase().equals("veggie");
                item.price = trEl.select("td.table__price").text().trim().replace(" ", "").replace("€", "").replace(",", ".");
                am.menuItems.add(item);
            }
        }
        return menus;
    }
}
