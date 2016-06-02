package com.example.graci.saunders_stocksearchapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs000";
    ArrayList<String> autoCompleteList;
    AutoCompleteTextView actv;
    final Context context = this;
    AlertDialog.Builder adb;
    public ArrayList<HashMap<String, String>> favMaps = new ArrayList<HashMap<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("   Stock Market Viewer");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        //TODO
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.trends);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);
        actv = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);

        //todo: old shared pref
        for(int i=0;i<sharedpreferences.getAll().size();i++)
        {
            String symbol = sharedpreferences.getString("Symbol"+i,"");
            String url = "http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/?getQuote=" + symbol;
            new GetFavoriteOperation().execute(url);

        }


        //Autocomplete Functionality
        actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //check to make sure that we are at the 3rd character or higher
                String currentChars = "";
                String url = "";

                if(s.length() >= 3) {
                    Log.i("oTC: CharSequence", s.toString());
                    if (actv.isPerformingCompletion()) {
                        // An item has been selected from the list. Ignore.
                        return;
                    }

                    currentChars = s.toString().trim().replace(" ", "+");
                    url = "http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/?autocomplete=" + currentChars;
                    new AutocompleteOperation().execute(url);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //unused for this homework
            }
        });


    }

    /*@Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Toast.makeText(getApplicationContext(),
                sharedpreferences.toString(), Toast.LENGTH_SHORT).show();

        if(sharedpreferences.getAll().size() < favMaps.size()) {
            Toast.makeText(getApplicationContext(),
                    "mismatch!", Toast.LENGTH_SHORT).show();
            for(int i = 0; i < favMaps.size(); i++) {
                if(!sharedpreferences.contains(favMaps.get(i).keySet().toString())) {
                    Toast.makeText(getApplicationContext(),
                            "shared pref doesn't have " + favMaps.get(i).get("fSymbol"), Toast.LENGTH_SHORT).show();
                }
            }
        }


    }*/

    public void getQuote(View view) {

        if(actv.getText().toString() == null || actv.getText().toString().trim().length() == 0) {

            //TODO: make the 'OK' button red
            //create alert
            //context = this;
            adb = new AlertDialog.Builder(context);
            adb.setMessage("Please enter a Stock Name/Symbol").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog alert  = adb.create();
            alert.show();


        } else {
            //TODO: CALL THIS function below to get the json instead of using the test String array
            String url = "http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/?getQuote=" + actv.getText().toString();
            new GetQuoteOperation().execute(url);

           /* Intent getQuoteIntent = new Intent(MainActivity.this, ResultActivity.class);
            getQuoteIntent.putExtra("Symbol", actv.getText().toString());
            startActivity(getQuoteIntent);*/
        }


    }

    public void clear(View view) {
        AutoCompleteTextView quoteView= (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        quoteView.setText("");

        //TODO: clean up the above and add functionality to clear any errors that may be showing

    }

    public void refresh(View view) {
        Toast.makeText(getApplicationContext(),
                "Refresh clicked", Toast.LENGTH_SHORT).show();
    }

    private class AutocompleteOperation extends AsyncTask<String, Void, JSONArray> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONArray doInBackground(String... urls) {

            json_array_parser myParser = new json_array_parser();
            JSONArray myArray = myParser.getJSONFromUrl(urls[0]);

            Log.d("dIB: URL", urls[0]);

            return myArray;
        }

        @Override
        protected void onPostExecute(JSONArray result) {
            //Log.i("JsonArray", result.toString());
            autoCompleteList = new ArrayList<String>();

            //Getting the instance of AutoCompleteTextView
            actv.setThreshold(3);//will start working from third character
            actv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView) view.findViewById(R.id.quoteSymbol);
                    actv.setText(textView.getText().toString());

                }
            });

            // create the grid item mapping
            String[] from = new String[] {"qSymbol", "qDetail"};
            int[] to = new int[] { R.id.quoteSymbol, R.id.quoteDetail };

            // prepare the list of all records
            ArrayList<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
            String searchString;
            for(int i = 0; i < result.length(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();

                try {
                    searchString = result.getString(i);
                    Log.i("searchString",searchString);
                    String s[] = searchString.split("-",2);
                    s[0] = s[0].trim();
                    s[1]=s[1].trim();
                    String ex[]=s[1].split("\\(");
                    ex[0] = ex[0].trim();
                    Log.i("ex length",ex.length + "");
                    String detail="";

                    ex[1] = ex[1].trim();
                    ex[1]=ex[1].substring(0,ex[1].length()-1);
                    detail = ex[0] + " ("+ex[1]+")";

                    map.put("qSymbol", s[0]);
                    map.put("qDetail", detail);
                    fillMaps.add(map);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } //end for loop

            // fill in the grid_item layout
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), fillMaps, R.layout.autocomplete_dropdown, from, to);
            actv.setAdapter(adapter);

        }
    }

    private class GetQuoteOperation extends AsyncTask<String, Void, JSONObject> {
        String[] stockHeaders = {"NAME", "SYMBOL", "LASTPRICE", "CHANGE", "TIMESTAMP", "MARKETCAP", "VOLUME", "CHANGEYTD", "HIGH", "LOW", "OPEN"};
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            super.onPreExecute();


            //super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
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

                if(result.has("Message")) {
                    adb = new AlertDialog.Builder(context);
                    adb.setMessage("Invalid Symbol").setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alert  = adb.create();
                    alert.show();
                }
                //String msg = data.getString("Message");
                else {
                    String status = null;
                    try {
                        status = result.getString("Status");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(status.contains("Failure")) {
                        adb = new AlertDialog.Builder(context);
                        adb.setMessage("No stock information available for " + actv.getText().toString().toUpperCase()).setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert  = adb.create();
                        alert.show();

                    } else {
                        Intent getQuoteIntent = new Intent(MainActivity.this, ResultActivity.class);
                        getQuoteIntent.putExtra("json", result.toString());
                        getQuoteIntent.putExtra("Symbol", actv.getText().toString());
                        startActivity(getQuoteIntent);
                    }
                }
            }
        }
    }

    private class GetFavoriteOperation extends AsyncTask<String, Void, JSONObject> {

        String fb_name, fb_symbol, fb_lastPrice;
        ArrayList<String> fbvalues; //TODO - modify

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
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

            if(result == null ) {
                //TODO
                Log.i("Error", "****Json is null... API may be deprecated.");
            } else {

                // create the grid item mapping
                String[] from = new String[] {"fName", "fSymbol", "fPrice", "fMarketCap", "fChangePer"};
                int[] to = new int[] { R.id.favName, R.id.favSymbol, R.id.favPrice, R.id.favMarketCap, R.id.favChangePer };

                //draw favorites list
                HashMap<String, String> map = new HashMap<String, String>();
                try {
                    map.put("fName", result.getString("Name"));
                    map.put("fSymbol", result.getString("Symbol"));
                    map.put("fPrice", "$ " + result.getString("LastPrice"));

                    double changePercent = Double.parseDouble(result.getString("ChangePercent"));
                    changePercent = Math.round(changePercent*100.0)/100.0;

                    map.put("fChangePer", "" + getChangePercentage(changePercent));

                    //TODO green/red
                    /*TextView changePer = (TextView)findViewById(R.id.favChangePer);

                    if(changePer.getTag().toString().equals("positive")) {
                        changePer.setBackgroundColor(Color.parseColor("#00ff00"));
                    } else if(changePer.getTag().toString().equals("negative")) {
                        changePer.setBackgroundColor(Color.parseColor("#ff0000"));
                    }*/

                    String marketCap = getMarketCap(result.getString("MarketCap"));

                    map.put("fMarketCap", "Market Cap : " + marketCap);
                    favMaps.add(map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SimpleAdapter favAdapter = new SimpleAdapter(getBaseContext(), favMaps, R.layout.favorites, from, to);

                DynamicListView favListView = (DynamicListView)findViewById(R.id.fav_details);
                favListView.setAdapter(favAdapter);

                favListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<? > adapterView, View rowView,
                                            int position, long id) {

                        TextView symbol = (TextView)rowView.findViewById(R.id.favSymbol);
                        String quoteSymbol = symbol.getText().toString();
                        String url = "http://graciela-stockphp-env.us-west-2.elasticbeanstalk.com/?getQuote=" + quoteSymbol;
                        new GetQuoteOperation().execute(url);
                    }
                });
            }
        }
        protected String getMarketCap(String marketCap) {
            Double mc = Double.parseDouble(marketCap);

            if( (mc / 1000000000) > 1 ) {
                mc = Math.round((mc /1000000000)*100.0)/100.0;
                marketCap =  mc + " Billion";
            } else if ( (mc / 1000000) > 1) {
                mc = Math.round((mc /1000000)*100.0)/100.0;
                marketCap =  mc + " Million";
            }
            return marketCap;
        }

        protected String getChangePercentage(double changePercent) {
            String cp = "";
            //TextView changePer = (TextView)findViewById(R.id.favChangePer);

            if(changePercent > 0) {
                cp = "+"+ changePercent + "%";
                //changePer.setTag("positive");
            } else {
                cp = changePercent + "%";
                if(changePercent < 0) {
                    //changePer.setTag("negative");
                } else {
                    //changePer.setTag("zero");
                }
            }

            return cp;
        }
    }
}
