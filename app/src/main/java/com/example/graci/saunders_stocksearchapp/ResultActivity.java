package com.example.graci.saunders_stocksearchapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ResultActivity extends Activity {

    //TODO
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs000";
    public final static String EXTRA_FACEBOOK = "com.example.graci.saunders_stocksearchapp.FACEBOOK";
    PhotoViewAttacher mAttacher;
    AlertDialog.Builder builder;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.

    private GoogleApiClient client;*/

    ImageView img;
    Bitmap bitmap;
    String yahooImgSrc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //Get Intent that started the activity
        String symbol = getIntent().getExtras().getString("Symbol");


        //setTitle("test");

        //TODO
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Log.i("Counter Result: ", sharedpreferences.getAll().size() + "");



        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();

        //Create all tabs
        TabHost.TabSpec currentTab = host.newTabSpec("CURRENT");
        TabHost.TabSpec historicalTab = host.newTabSpec("HISTORICAL");
        TabHost.TabSpec newsTab = host.newTabSpec("NEWS");

        //Populate Current Stock Tab
        //String url = "http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/?getQuote=" + symbol;
        //new GetQuoteOperation().execute(url);
        JSONObject data = null;
        try {
            data = new JSONObject(getIntent().getExtras().getString("json"));
            try {
                generateStockInfo(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            //String yahooImgSrc = "http://chart.finance.yahoo.com/t?s=" + fb_symbol + "&lang=en-US";
           /* ImageView yahooImgView = (ImageView)findViewById(R.id.yahooMap);
            yahooImgView.setImageDrawable(drawable_from_url(yahooImgSrc));
            //String html = "<img src='" + yahooImgSrc + "' />";
            //Spanned spannedHtml = Html.fromHtml(html);
            //AAPImageView yahooImgView = (ImageView)findViewById(R.id.yahooMap);
*/
            //ImageLoaderTask imageLoaderTask = new ImageLoaderTask();
            //imageLoaderTask.execute(yahooImgSrc);

  //          new DownloadImageTask(yahooImgView).execute(yahooImgSrc);
            /*try {
                InputStream is = (InputStream) new URL(yahooImgSrc).getContent();
                Drawable d = Drawable.createFromStream(is, "src name");
                Drawable d2 = Drawable.
                yahooImgView.setImageDrawable(d);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ResultActivity.this, "NOT WORKING", Toast.LENGTH_SHORT).show();
            }


            //Uri yahooUri = Uri.parse(yahooImgSrc);
            //yahooImgView.setImageURI(Uri.parse(yahooImgSrc));


            //Bitmap yahooImgMap = BitmapFactory.decodeFile(yahooImgSrc);
            //Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(yahooImgSrc).getContent());
            //yahooImgView.setImageBitmap(yahooImgMap);
            //yahooImgView.invalidate();
            URL url = new URL(yahooImgSrc);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            yahooImgView.setImageBitmap(bmp);*/
            //Bitmap yahooMap = drawable_from_url(yahooImgSrc);
            //ImageView imgview = new ImageView(getBaseContext());
            //imgview.setImageDrawable(yahooMap);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ResultActivity.this, "NOT WORKING", Toast.LENGTH_SHORT).show();
        }


        //Populate Historical Data Tab
        WebView webView = (WebView) findViewById(R.id.historical);
        webView.getSettings().setJavaScriptEnabled(true);

        //Create string of markit time series javascript/html code
        String historicalHtml = "<!doctype html>\n" +
                "<html>\n" +
                "<head>\n" +
                "<meta charset=\"utf-8\">\n" +
                "<title>HW9 Historical</title>\n" +
                "\n" +
                "    <link rel=\"stylesheet\" href=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\">\n" +
                "\t<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap-theme.min.css\">\n" +
                "    <script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js\"></script>\n" +
                "\n" +
                "    <script src=\"http://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js\"></script>\n" +
                "\n" +
                "    <link href=\"http://code.jquery.com/ui/1.10.2/themes/smoothness/jquery-ui.css\" rel=\"Stylesheet\"></link>\n" +
                "    \n" +
                "    <script src=\"http://code.jquery.com/ui/1.10.2/jquery-ui.js\" ></script>\n" +
                "    <link href=\"https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css\" rel=\"stylesheet\">\n" +
                "    <script src=\"https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js\"></script>\n" +
                "    <script src=\"https://code.highcharts.com/stock/highstock.js\"></script>\n" +
                "    <script src=\"https://code.highcharts.com/stock/modules/exporting.js\"></script>\n" +
                "\n" +
                "<script>\n" +
                "$(document).ready(function(e) {\n" +
                "   \n" +
                "\tvar symbol = \"" + symbol + "\";\n" +
                "\thistoricalInit(symbol);\n" +
                "\t\t\t\n" +
                "\tfunction historicalInit(symbol){\n" +
                "\t\tsymbol = symbol.toUpperCase();\n" +
                "\t\tPlotChart();\n" +
                "\t};\n" +
                "\t\t\t\t\n" +
                "\tfunction PlotChart(){\n" +
                "\t\n" +
                "\t\t$.ajax({\n" +
                "\t\t\tbeforeSend:function(){\n" +
                "\t\t\t\t$(\"#historyContainer\").text(\"Loading chart...\");\n" +
                "\t\t\t},\n" +
                "\t\t\t'url': \"http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/\",\n" +
                "\t\t\t'type': 'GET',\n" +
                "\t\t\t'dataType': \"json\",\n" +
                "\t\t\t'data': {\n" +
                "\t\t\t\t'iChart': symbol\n" +
                "\t\t\t},\n" +
                "\t\t\tsuccess: function(json){\n" +
                "\t\t\t\tif (!json || json.Message){\n" +
                "\t\t\t\t\treturn;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\tjson = $.parseJSON(json);\n" +
                "\t\t\t\trender(json);\n" +
                "\t\t\t},\n" +
                "\t\t\terror: function(response,txtStatus){\n" +
                "\t\t\t}\n" +
                "\t\t});\n" +
                "\t};\n" +
                "\t\n" +
                "\tfunction _fixDate(dateIn) {\n" +
                "\t\tvar dat = new Date(dateIn);\n" +
                "\t\treturn Date.UTC(dat.getFullYear(), dat.getMonth(), dat.getDate());\n" +
                "\t};\n" +
                "\t\n" +
                "\tfunction _getOHLC(json) {\n" +
                "\t\tvar dates = json.Dates || [];\n" +
                "\t\tvar elements = json.Elements || [];\n" +
                "\t\tvar chartSeries = [];\n" +
                "\t\t\n" +
                "\t\tif (elements[0]){\n" +
                "\t\t\t\n" +
                "\t\t\tfor (var i = 0, datLen = dates.length; i < datLen; i++) {\n" +
                "\t\t\t\tvar dat = _fixDate( dates[i] );\n" +
                "\t\t\t\tvar pointData = [\n" +
                "\t\t\t\tdat,\n" +
                "\t\t\t\telements[0].DataSeries['open'].values[i],\n" +
                "\t\t\t\telements[0].DataSeries['high'].values[i],\n" +
                "\t\t\t\telements[0].DataSeries['low'].values[i],\n" +
                "\t\t\t\telements[0].DataSeries['close'].values[i]\n" +
                "\t\t\t\t];\n" +
                "\t\t\t\tchartSeries.push( pointData );\n" +
                "\t\t\t};\n" +
                "\t\t}\n" +
                "\t\treturn chartSeries;\n" +
                "\t};\n" +
                "\t\t\t\t\n" +
                "\tfunction _getVolume(json) {\n" +
                "\t\tvar dates = json.Dates || [];\n" +
                "\t\tvar elements = json.Elements || [];\n" +
                "\t\tvar chartSeries = [];\n" +
                "\t\t\n" +
                "\t\tif (elements[1]){\n" +
                "\t\t\t\n" +
                "\t\t\tfor (var i = 0, datLen = dates.length; i < datLen; i++) {\n" +
                "\t\t\t\tvar dat = _fixDate( dates[i] );\n" +
                "\t\t\t\tvar pointData = [\n" +
                "\t\t\t\tdat,\n" +
                "\t\t\t\telements[1].DataSeries['volume'].values[i]\n" +
                "\t\t\t\t];\n" +
                "\t\t\t\tchartSeries.push( pointData );\n" +
                "\t\t\t};\n" +
                "\t\t}\n" +
                "\t\treturn chartSeries;\n" +
                "\t};\n" +
                "\t\t\t\t\n" +
                "\tfunction render(data) {\n" +
                "\t\tvar ohlc = _getOHLC(data),\n" +
                "\t\tvolume = _getVolume(data);\n" +
                "\t\t\n" +
                "\t\tvar groupingUnits = [[\n" +
                "\t\t'week',                         \n" +
                "\t\t[1]                             \n" +
                "\t\t], [\n" +
                "\t\t'month',\n" +
                "\t\t[1, 2, 3, 4, 6]\n" +
                "\t\t]];\n" +
                "\t\t\n" +
                "\t\t$('#historyContainer').highcharts('StockChart', {\n" +
                "\t\t\t\n" +
                "\t\t\texporting: {\n" +
                "\t\t\t\tenabled: false\n" +
                "\t\t\t},\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\trangeSelector: {\n" +
                "\t\t\t\tselected: 1\n" +
                "\t\t\t},\n" +
                "\t\t\t\n" +
                "\t\t\t\t\n" +
                "\t\t\ttitle: {\n" +
                "\t\t\t\ttext: symbol + ' Historical Price'\n" +
                "\t\t\t},\n" +
                "\t\t\tyAxis: {\n" +
                "\t\t\t\ttitle: {\n" +
                "\t\t\t\t\tenabled: true,\n" +
                "\t\t\t\t\ttext: 'Stock Value',\n" +
                "\t\t\t\t\tstyle: {\n" +
                "\t\t\t\t\t\tfontWeight: 'normal'\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\tseries: [{\n" +
                "\t\t\t\ttype: 'area',\n" +
                "\t\t\t\tname: symbol,\n" +
                "\t\t\t\tthreshold: null,\n" +
                "\t\t\t\tdata: ohlc,\n" +
                "\t\t\t\tdataGrouping: {\n" +
                "\t\t\t\t\tunits: groupingUnits\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\tfillColor: {\n" +
                "\t\t\t\t\tlinearGradient: {\n" +
                "\t\t\t\t\t\tx1: 0,\n" +
                "\t\t\t\t\t\ty1: 0,\n" +
                "\t\t\t\t\t\tx2: 0,\n" +
                "\t\t\t\t\t\ty2: 1\n" +
                "\t\t\t\t\t},\n" +
                "\t\t\t\t\tstops: [\n" +
                "\t\t\t\t\t[0, Highcharts.getOptions().colors[0]],\n" +
                "\t\t\t\t\t[1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]\n" +
                "\t\t\t\t\t]\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}],\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\tcredits: {\n" +
                "\t\t\t\tenabled:false\n" +
                "\t\t\t},\n" +
                "\t\t\ttooltip: {\n" +
                "\t\t\t\tformatter: function () {\n" +
                "\t\t\t\t\tvar s = Highcharts.dateFormat('%A, %b %d, %Y', x);\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\t$.each(points, function (i, point) {\n" +
                "\t\t\t\t\t\ts += '<br/>' + point.series.name + ': <b>$' + point.y + '</b>';\n" +
                "\t\t\t\t\t});\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\treturn s;\n" +
                "\t\t\t\t}, shared: true\n" +
                "\t\t\t},\n" +
                "\t\t\t\n" +
                "\t\t\t\n" +
                "\t\t\ttitle : {\n" +
                "\t\t\t\ttext : symbol + ' Stock Value'\n" +
                "\t\t\t\t\n" +
                "\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\n" +
                "\t\t\trangeSelector : {\n" +
                "\t\t\t\tallButtonsEnabled: true,\n" +
                "\t\t\t\tbuttons: [\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'week',\n" +
                "\t\t\t\t\tcount: 1,\n" +
                "\t\t\t\t\ttext: '1w',\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'month',\n" +
                "\t\t\t\t\tcount: 1,\n" +
                "\t\t\t\t\ttext: '1m',\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'month',\n" +
                "\t\t\t\t\tcount: 3,\n" +
                "\t\t\t\t\ttext: '3m',\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'month',\n" +
                "\t\t\t\t\tcount: 6,\n" +
                "\t\t\t\t\ttext: '6m',\n" +
                "\t\t\t\t}, \n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'ytd',\n" +
                "\t\t\t\t\tcount: 1,\n" +
                "\t\t\t\t\ttext: 'YTD',\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t}, \n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'year',\n" +
                "\t\t\t\t\tcount: 1,\n" +
                "\t\t\t\t\ttext: '1y',\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t}, \n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\ttype: 'all',\n" +
                "\t\t\t\t\ttext: 'All',\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t}\n" +
                "\t\t\t\t],\n" +
                "\t\t\t\t\n" +
                "\t\t\t\tselected: 0,\n" +
                "\t\t\t\tinputEnabled:false\n" +
                "\t\t\t\t\n" +
                "\t\t\t},\n" +
                "\t\t\t\n" +
                "\t\t});\n" +
                "\t};\n" +
                "});\n" +
                "</script>\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body>\n" +
                " \n" +
                " <div class=\"row\" id=\"history\">                                \t\n" +
                " \t<div id=\"historyContainer\"  class=\"col-md-10\" style=\"width:70% !important\"></div>\n" +
                " </div>\n" +
                " \n" +
                "</body>\n" +
                "</html>\n";

        webView.loadData(historicalHtml, "text/html", "UTF-8");

        String newsUrl = "http://rijutakapoor-env.us-west-2.elasticbeanstalk.com/?news=" + symbol;
        new GetNewsOperation().execute(newsUrl);

        //Set tab contents and indicators
        currentTab.setContent(R.id.current);
        currentTab.setIndicator("CURRENT");
        historicalTab.setContent(R.id.historical);
        historicalTab.setIndicator("HISTORICAL");
        newsTab.setContent(R.id.news);
        newsTab.setIndicator("NEWS");

        //Add all tabs
        host.addTab(currentTab);
        host.addTab(historicalTab);
        host.addTab(newsTab);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /*private class GetQuoteOperation extends AsyncTask<String, Void, JSONObject> {
        String[] stockHeaders = {"NAME", "SYMBOL", "LASTPRICE", "CHANGE", "TIMESTAMP", "MARKETCAP", "VOLUME", "CHANGEYTD", "HIGH", "LOW", "OPEN"};
        private ProgressDialog progressDialog;
        String fb_name, fb_symbol, fb_lastPrice;
        ArrayList<String> fbvalues; //TODO - modify

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            //super.onPreExecute();
            progressDialog = new ProgressDialog(ResultActivity.this);
            progressDialog.setCancelable(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... urls) {

            json_object_parser myParser = new json_object_parser();
            JSONObject myObject = myParser.getJSONFromUrl(urls[0]);

            Log.d("dIB: URL", urls[0]);

            return myObject;
        }

        @Override
        protected void onPostExecute(JSONObject result) {

            progressDialog.dismiss();

            if(result == null ) {
                //TODO
                Log.i("Error", "****Json is null... API may be deprecated.");
            } else {

                try {
                    generateStockInfo(result);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }*/


    String[] stockHeaders = {"NAME", "SYMBOL", "LASTPRICE", "CHANGE", "TIMESTAMP", "MARKETCAP", "VOLUME", "CHANGEYTD", "HIGH", "LOW", "OPEN"};
    private ProgressDialog progressDialog;
    String fb_name, fb_symbol, fb_lastPrice;
    ArrayList<String> fbvalues; //TODO - modify

    //Helper Functions
    protected void generateStockInfo(JSONObject data) throws JSONException, IOException {


        String name = data.getString("Name");
        fb_name = name;

        //TODO MODIFY! (put in a helper function)
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha); //gives me the back button

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });


        String sym = data.getString("Symbol");
        fb_symbol = sym;
        String lastPrice = data.getString("LastPrice");
        fb_lastPrice = lastPrice;

        double change = Double.parseDouble(data.getString("Change"));
        change = Math.round(change * 100.0) / 100.0;

        double changePercent = Double.parseDouble(data.getString("ChangePercent"));
        changePercent = Math.round(changePercent * 100.0) / 100.0;

        String comboChange = getComboChangePercentage(change, changePercent);
        String timeDate = getTimeDate(data.getString("Timestamp"));

        String marketCap = getMarketCap(data.getString("MarketCap"));
        String volume = data.getString("Volume");

        double changeYTD = Double.parseDouble(data.getString("ChangeYTD"));
        changeYTD = Math.round(changeYTD * 100.0) / 100.0;

        double changeYTDPercent = Double.parseDouble(data.getString("ChangePercentYTD"));
        changeYTDPercent = Math.round(changeYTDPercent * 100.0) / 100.0;

        String comboYTD = getComboYTDPercentage(changeYTD, changeYTDPercent);

        String high = data.getString("High");
        String low = data.getString("Low");
        String open = data.getString("Open");

        Log.i("HELPER results:", name + "; " + sym + "; " + lastPrice + "; " + comboChange
                + "; " +
                timeDate + "; " + marketCap + "; " + volume + "; " + comboYTD
                + "; " + high + "; " + low + "; " + open + "; ");

        List<HashMap<String, String>> fillStockTab = new ArrayList<HashMap<String, String>>();

        //TODO: Clean up this code!!!
        HashMap<String, String> stockMap0 = new HashMap<String, String>();
        stockMap0.put("header", stockHeaders[0]);
        stockMap0.put("detail", name);
        fillStockTab.add(stockMap0);

        HashMap<String, String> stockMap1 = new HashMap<String, String>();
        stockMap1.put("header", stockHeaders[1]);
        stockMap1.put("detail", sym);
        fillStockTab.add(stockMap1);

        HashMap<String, String> stockMap2 = new HashMap<String, String>();
        stockMap2.put("header", stockHeaders[2]);
        stockMap2.put("detail", lastPrice);
        fillStockTab.add(stockMap2);

        HashMap<String, String> stockMap3 = new HashMap<String, String>();
        stockMap3.put("header", stockHeaders[3]);
        stockMap3.put("detail", comboChange);
        if (change > 0 || changePercent > 0) {
            stockMap3.put("image", Integer.toString(R.drawable.up));
        } else if (change < 0 || changePercent < 0) {
            stockMap3.put("image", Integer.toString(R.drawable.down));
        }
        fillStockTab.add(stockMap3);

        HashMap<String, String> stockMap4 = new HashMap<String, String>();
        stockMap4.put("header", stockHeaders[4]);
        stockMap4.put("detail", timeDate);
        fillStockTab.add(stockMap4);

        HashMap<String, String> stockMap5 = new HashMap<String, String>();
        stockMap5.put("header", stockHeaders[5]);
        stockMap5.put("detail", marketCap);
        fillStockTab.add(stockMap5);

        HashMap<String, String> stockMap6 = new HashMap<String, String>();
        stockMap6.put("header", stockHeaders[6]);
        stockMap6.put("detail", volume);
        fillStockTab.add(stockMap6);

        HashMap<String, String> stockMap7 = new HashMap<String, String>();
        stockMap7.put("header", stockHeaders[7]);
        stockMap7.put("detail", comboYTD);
        if (changeYTD > 0 || changeYTDPercent > 0) {
            stockMap7.put("image", Integer.toString(R.drawable.up));
        } else if (changeYTD < 0 || changeYTDPercent < 0) {
            stockMap7.put("image", Integer.toString(R.drawable.down));
        }

        fillStockTab.add(stockMap7);

        HashMap<String, String> stockMap8 = new HashMap<String, String>();
        stockMap8.put("header", stockHeaders[8]);
        stockMap8.put("detail", high);
        fillStockTab.add(stockMap8);

        HashMap<String, String> stockMap9 = new HashMap<String, String>();
        stockMap9.put("header", stockHeaders[9]);
        stockMap9.put("detail", low);
        fillStockTab.add(stockMap9);

        HashMap<String, String> stockMap10 = new HashMap<String, String>();
        stockMap10.put("header", stockHeaders[10]);
        stockMap10.put("detail", open);
        fillStockTab.add(stockMap10);

        //takes the value and passes it to stock_detail
        SimpleAdapter stockAdapter = new SimpleAdapter(getBaseContext(), fillStockTab, R.layout.stock_detail,
                new String[]{"header", "detail", "image"}, new int[]{R.id.header, R.id.detail, R.id.imageIcon});

        //put stock detail into proper tab
        ListView currentTabListView = (ListView) findViewById(R.id.current);
        currentTabListView.setAdapter(stockAdapter);

        //img = (ImageView)findViewById(R.id.yahooMap);

        yahooImgSrc = "http://chart.finance.yahoo.com/t?s=" + fb_symbol + "&lang=en-US&width=450&height=350";
        /*bitmap = BitmapFactory.decodeStream((InputStream)new URL(yahooImgSrc).getContent());
        ImageView newView = new ImageView(getBaseContext());
        newView.setImageBitmap(bitmap);
        currentTabListView.addFooterView(newView);
        stockAdapter.notifyDataSetChanged();*/

        new LoadImage2().execute(currentTabListView);
        //new LoadImage().execute(yahooImgSrc);

        stockAdapter.notifyDataSetChanged();

        //String yahooImgSrc = "http://chart.finance.yahoo.com/t?s=" + fb_name + "&lang=en-US";
        //new lastResort().execute(yahooImgSrc);
        /*String yahooImgSrc = "http://chart.finance.yahoo.com/t?s=" + fb_name + "&lang=en-US";
        URL url = null;
        try {
            url = new URL(yahooImgSrc);

            ImageView yahooImgView = (ImageView) findViewById(R.id.yahooMap);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            ImageView imageView = new ImageView(getBaseContext());
            imageView.setImageBitmap(bmp);

            currentTabListView.addFooterView(imageView);
            stockAdapter.notifyDataSetChanged();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }*/


        /*try {
            yahooImgView.setImageBitmap(drawable_from_url(yahooImgSrc));
        } catch (IOException e) {
            e.printStackTrace();
        }*/


            /*String yahooImgSrc = "http://chart.finance.yahoo.com/t?s=" + fb_name + "&lang=en-US";
            try {
                currentTabListView.addFooterView(new ImageView(drawable_from_url(yahooImgSrc)));
            } catch (IOException e) {
                e.printStackTrace();
            }*/


        //TODO
        Button facebookButton = (Button) findViewById(R.id.fb_button);

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(ResultActivity.this, "Sharing " + fb_name + " !!", Toast.LENGTH_SHORT).show();
                Log.i("In fb", "please work");
                Intent intent1 = new Intent(v.getContext(), FacebookActivity.class);
                fbvalues = new ArrayList<String>();
                fbvalues.add(fb_name);
                fbvalues.add(fb_lastPrice);
                String fbURL = "http://finance.yahoo.com/q?s=" + fb_symbol;
                String fbURL1 = "http://chart.finance.yahoo.com/t?s=" + fb_symbol + "&lang=en-US&width=400&height=300";
                fbvalues.add(fbURL);
                fbvalues.add(fbURL1);
                intent1.putStringArrayListExtra(EXTRA_FACEBOOK, fbvalues);
                startActivity(intent1);

            }
        });

        //TODO
        Button favoriteButton = (Button) findViewById(R.id.fav_button);

        //determine if this is in the shared preferences already
        for (int i = 0; i < sharedpreferences.getAll().size(); i++) {
            String syCheck = sharedpreferences.getString("Symbol" + i, "");
            if (syCheck.equals(fb_symbol)) {
                favoriteButton.setBackgroundResource(R.drawable.yellow_star);
                favoriteButton.setTag("filled");
                break;
            }
        }

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button favButt = (Button) findViewById(R.id.fav_button);
                //Log.i("Empty Tag",favButt.getTag().toString());
                SharedPreferences.Editor editor = sharedpreferences.edit(); //used to edit shared pref
                if (favButt.getTag().toString().equals("empty")) {
                    favButt.setBackgroundResource(R.drawable.yellow_star);
                    favButt.setTag("filled");
                    Log.i("Inside if yellow", "Made yellow");
                    Log.i("Filed Tag", favButt.getTag().toString());
                    Log.i("In Fav", "Added");
                    Toast.makeText(ResultActivity.this, "Bookmarked " + fb_name + " !!", Toast.LENGTH_SHORT).show();
                    editor.putString("Symbol" + sharedpreferences.getAll().size(), fb_symbol);

                    editor.commit();
                    Log.i("Shared in Result", sharedpreferences.getString("Symbol" + (sharedpreferences.getAll().size() - 1), ""));


                } else if (favButt.getTag().toString().equals("filled")) {
                    favButt.setBackgroundResource(R.drawable.emptystar);
                    favButt.setTag("empty");

                    for (int i = 0; i < sharedpreferences.getAll().size(); i++) {
                        String syCheck = sharedpreferences.getString("Symbol" + i, "");
                        if (syCheck.equals(fb_symbol)) {
                            //editor.remove("Symbol"+i);
                            //editor.apply();
                            for (int j = i + 1; j < sharedpreferences.getAll().size(); j++) {
                                int pos = j - 1;
                                editor.putString("Symbol" + pos, sharedpreferences.getString("Symbol" + j, ""));
                                editor.commit();
                            }
                            editor.remove("Symbol" + (sharedpreferences.getAll().size() - 1));
                            editor.apply();
                            editor.commit();
                        } else {
                            continue;
                        }
                    }
                }

            }
        });
    }

    protected String getComboChangePercentage(double change, double changePercent) {
        String combo = "";

        if (changePercent > 0) {
            combo = change + "(+" + changePercent + "%)";
        } else {
            combo = change + "(" + changePercent + "%)";
        }

        return combo;
    }

    protected String getComboYTDPercentage(double changeYTD, double changeYTDPercent) {
        String combo = "";

        if (changeYTDPercent > 0) {
            combo = changeYTD + "(+" + changeYTDPercent + "%)";
        } else {
            combo = changeYTD + "(" + changeYTDPercent + "%)";
        }

        return combo;
    }

    protected String getTimeDate(String timestamp) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zZ yyyy");
        try {
            Date date = inputFormat.parse(timestamp);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
            timestamp = outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    protected String getMarketCap(String marketCap) {
        Double mc = Double.parseDouble(marketCap);

        if ((mc / 1000000000) > 1) {
            mc = Math.round((mc / 1000000000) * 100.0) / 100.0;
            marketCap = mc + " Billion";
        } else if ((mc / 1000000) > 1) {
            mc = Math.round((mc / 1000000) * 100.0) / 100.0;
            marketCap = mc + " Million";
        }
        return marketCap;
    }

    /*Bitmap drawable_from_url(String url) throws MalformedURLException, IOException {
        Bitmap x;

        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        connection.connect();
        InputStream input = connection.getInputStream();

        x = BitmapFactory.decodeStream(input);
        return x;
    }*/

   /* @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Result Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.graci.saunders_stocksearchapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Result Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.example.graci.saunders_stocksearchapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
    //}*/

    private class GetNewsOperation extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            json_news_parser newsParser = new json_news_parser();
            String newsString = newsParser.getNewsFromUrl(urls[0]);

            Log.d("dIB: URL", urls[0]);

            return newsString;

        }

        @Override
        protected void onPostExecute(String result) {


            //TODO change code

            Log.i("bing before", result);
            result = result.substring(1, result.length());
            Log.i("bing after", result);

            JSONArray jsonN = null;
            List<HashMap<String, String>> newsTabFiller = new ArrayList<HashMap<String, String>>();

            try {
                jsonN = new JSONArray(result);
                for (int i = 0; i < 4; i++) {
                    JSONObject jsonNobj1 = jsonN.getJSONObject(i);
                    Log.i("News object", jsonNobj1.toString());
                    String URL = jsonNobj1.getString("Url");
                    String title = jsonNobj1.getString("Title");
                    String content = jsonNobj1.getString("Description");
                    String publisher = jsonNobj1.getString("Source");
                    String publisherDate = jsonNobj1.getString("Date");

                    String timestamp = " ";
                    try {
                        SimpleDateFormat inputFormat = new SimpleDateFormat("dd MMMMM yyyy HH:mm:ss");
                        Date date;
                        date = inputFormat.parse(publisherDate);
                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss");
                        timestamp = outputFormat.format(date);
                    } catch (Exception e) {
                        //cannot happen in this example
                    }

                    Spanned urlS = Html.fromHtml(URL);
                    Spanned titleS = Html.fromHtml(title);
                    Spanned contentS = Html.fromHtml(content);
                    Spanned pubS = Html.fromHtml(publisher);
                    Spanned pubDateS = Html.fromHtml(timestamp);

                    HashMap<String, String> newsMap = new HashMap<String, String>();
                    newsMap.put("newsUrl", urlS.toString());
                    newsMap.put("newsTitle", titleS.toString());
                    newsMap.put("newsContent", contentS.toString());
                    newsMap.put("newsPublisher", "Publisher: " + pubS.toString());
                    newsMap.put("newsDate", "Date: " + pubDateS.toString());
                    newsTabFiller.add(newsMap);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            //takes the value and passes it to stock_detail
            SimpleAdapter newsAdapter = new SimpleAdapter(getBaseContext(), newsTabFiller, R.layout.news,
                    new String[]{"newsUrl", "newsTitle", "newsContent", "newsPublisher", "newsDate"}, new int[]{R.id.newsUrl, R.id.newsTitle, R.id.newsContent, R.id.newsPublisher, R.id.newsDate});

            //put stock detail into proper tab
            ListView newsTabListView = (ListView) findViewById(R.id.news);
            newsTabListView.setAdapter(newsAdapter);

            //TODO: MODIFY!!
            newsTabListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View rowView,
                                        int position, long id) {

                    TextView newsUrl = (TextView) rowView.findViewById(R.id.newsUrl);
                    String newsUrlString = newsUrl.getText().toString();

                    Uri uri = Uri.parse(newsUrlString); //add this to get the http
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            Log.i("result", result.toString());

        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    /*private class lastResort extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected Bitmap doInBackground(String... imageUrls) {

            Bitmap bmp = null;
            URL url = null;
            try {
                url = new URL(imageUrls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("User-agent", "Mozilla/4.0");

                connection.connect();
                try ( InputStream is = url.openStream() ) {
                     bmp = BitmapFactory.decodeStream( is );
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if(result == null) {
                Toast.makeText(ResultActivity.this, "Null bitmap", Toast.LENGTH_SHORT).show();

            } else {
                ImageView yahooImgView = (ImageView) findViewById(R.id.yahooMap);

                ImageView imageView = new ImageView(getBaseContext());
                imageView.setImageBitmap(result);
            }

        }

    }
    private class ImageLoaderTask extends AsyncTask<String, Void, Drawable> {
        @Override
        protected Drawable doInBackground(String... imageUrls) {
            Drawable image = null;
            try {
                String url = imageUrls[0];
                if (null != url) {
                    image = getDrawable(url);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
            return image;
        }

        protected void onPostExecute(Drawable drawable) {
            if (drawable != null) {
                ImageView imageView = (ImageView) findViewById(R.id.yahooMap);
                imageView.setBackgroundDrawable(drawable);
            }
        }

        private Drawable getDrawable(String address) {
            try {
                URL url = new URL(address);
                InputStream is = (InputStream) url.getContent();
                Drawable d = Drawable.createFromStream(is, "src");
                return d;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }
    }*/

    /*private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        protected Bitmap doInBackground(String... args) {
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                img.setImageBitmap(image);
                img.setImageBitmap(image);

            }else{
                Toast.makeText(ResultActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }
    }*/

    private class LoadImage2 extends AsyncTask<ListView, Void, Bitmap> {
        ListView currentView;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(ResultActivity.this, yahooImgSrc, Toast.LENGTH_SHORT).show();

        }
        protected Bitmap doInBackground(ListView... args) {
            currentView = args[0];
            try {
                bitmap = BitmapFactory.decodeStream((InputStream)new URL(yahooImgSrc).getContent());

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {

            if(image != null){
                //img.setImageBitmap(image);
                ImageView newView = new ImageView(getBaseContext());
                newView.setImageBitmap(bitmap);
                newView.setScaleType(ImageView.ScaleType.FIT_XY);

                currentView.addFooterView(newView);

                //LayoutInflater factory = LayoutInflater.from(ResultActivity.this);
                final ImageView view = newView;

                newView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ResultActivity.this, "clicked!", Toast.LENGTH_SHORT).show();
                        builder = new AlertDialog.Builder(ResultActivity.this);

                        ImageView view2 = new ImageView(getBaseContext());
                        view2.setImageDrawable(view.getDrawable());
                        view2.setScaleType(ImageView.ScaleType.CENTER_CROP);

                        builder.setView(view2);

                        mAttacher = new PhotoViewAttacher(view2);
                        builder.show();

                        //builder.getWindow().setLayout(600, 400);



                        /*AlertDialog dialog = builder.create();
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogLayout = inflater.inflate(R.layout.dialog_layout, null);
                        ((ImageView)dialogLayout.findViewById(R.id.image)).setImageDrawable(view.getDrawable());
                        dialog.setView(dialogLayout);*/

                    }


                });


                //mAttacher = new PhotoViewAttacher(newView);

                //mAttacher.update();


            }else{
                Toast.makeText(ResultActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();

            }
        }

       /* @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Get the layout inflater
            LayoutInflater inflater = getActivity().getLayoutInflater();

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(inflater.inflate(R.layout.dialog_signin, null))
                    // Add action buttons
                    .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            LoginDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }*/
    }
}
