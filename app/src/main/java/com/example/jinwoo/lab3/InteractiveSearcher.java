package com.example.jinwoo.lab3;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListPopupWindow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jinwoo on 2016-03-17.
 */
public class InteractiveSearcher extends EditText {

    private int searchIndex, lengthLongestString ;
    private Context context;
    private HashMap<Integer, ArrayList<String>> nameHashMap;    // På varje key (searchIndex) finns en array (results) med alla matchande namn.
    private ListPopupWindow listPopupWindow;
    private boolean isClicked;
    private NameAdapter nameAdapter;
    public final static String noMatchingName = "No matching names found"; // Används i klassen NameList för att färga texten röd.
    private HttpURLConnection urlConnection;
    private int nrNameSuggestions;
    private boolean noMatch;
    private float xdpi;

    public InteractiveSearcher(Context theContext) {
        super(theContext);
        context = theContext;
        init();
    }

    private void init() {
        searchIndex = 0; // Första indexet.

        nameHashMap = new HashMap<Integer, ArrayList<String>>();

        // Behövs för att textfältet ska täckas över hela skärmen.
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Hämta skärmstorlek, dpi osv till att rita ut bredd/höjd på popuplistan.
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metrics);
        xdpi = metrics.xdpi;

        // Skapar ett tomt popup fönster för att visa namn från en ListAdapter.
        listPopupWindow = new ListPopupWindow(context);
        listPopupWindow.setAnchorView(this); // Placera viewen under edittext.

        // Lyssnare till popupfönstret.
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isClicked = true;
                // Om ett namn klickats så sätt namnet i fältet för text.
                setText(nameAdapter.getItem(position).toString());
            }
        });

        // Lyssnare för om texten ändras.
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Visa inte popupfönstret om inget skrivits i textfältet eller
                // om ett namn klickats på.
                if (s.toString().length() == 0 || isClicked == true) {
                    listPopupWindow.dismiss();
                    isClicked = false;
                } else {
                    // Skapa en ny AsyncTask som körs i bakgrunden.
                    new NetworkAsyncTask().execute(s.toString());
                }
            }
        });

    }

    public class NetworkAsyncTask extends AsyncTask<String, Void, String> {
        // Startas direkt när AsyncTask kallas.
        protected String doInBackground(String... theSearchString){
            String returnedText = "";

            // Försöka få data från hemsidan.
            try{
                URL url = new URL("http://flask-afteach.rhcloud.com/getnames/" + searchIndex++ + "/" + theSearchString[0]);
                System.out.println("searchIndex = " + searchIndex);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                returnedText = retrieveNamesFromStream(inputStream);
                urlConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return returnedText;
        }

        /**
         * Läser in alla namn från servern och returnerar dessa i en String.
         * @param inputStream En InputStream från anslutningen till url.
         * @return En String med alla namn från servern.
         */
        private String retrieveNamesFromStream(InputStream inputStream){
            StringBuilder stringBuilder = new StringBuilder();
            // Konverterar bytes till chars.
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            int sizeOfBuffer = 1000;

            BufferedReader bufferedReader = new BufferedReader(inputStreamReader, sizeOfBuffer);

            try {
                // Lägger till varje rad som inte är null till stringBuilder.
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    stringBuilder.append(line);
                }
                inputStream.close();

            } catch (Exception e) {
                System.out.println("Error reading the lines in the buffer." );
                e.printStackTrace();
            }
            finally {
                // Stänga readers.
                try{
                    if(bufferedReader == null) bufferedReader.close();
                    if(inputStreamReader == null) inputStreamReader.close();
                }catch (Exception e){
                    System.out.println("Error closing the readers.");
                    e.printStackTrace();
                }
            }
            return stringBuilder.toString();
        }


        /**
         * Hitta den längsta strängen i en array. Kallas i funktionen parseJSONObject.
         * @param resultsArray Strängen med alla namn i.
         * @return Int som beskriver längden av den längsta strängen.
         */
        private int getLengthOfLongestString(JSONArray resultsArray) {
            try{
                int maxLength = 0;
                for (int idx = 0; idx < resultsArray.length(); idx++) {
                    try{
                        int string = resultsArray.get(idx).toString().length();
                        if (string > maxLength) maxLength = string;
                    }catch (Exception e){
                        System.out.println("Error getting the length of a String.");
                        e.printStackTrace();
                    }
                }
                return maxLength;

            }catch (Exception e){
                System.out.println("Error getting the length of a String.");
                e.printStackTrace();
            }
            return 600; // TODO: ful kod, borde returna ett standard värde.
        }

        /**
         * parses JSON and initializes searchMap TODO:ändra kommentaren här
         * @param resultsArray
         */
        private void addNamesToHashmap(JSONArray resultsArray) {
            try {
                ArrayList<String> namesList = new ArrayList<String>();

                // Lägg alla namn från JSON-array till vår hashmap och listan med namn.
                if (resultsArray.length() == 0) {
                    namesList.add(noMatchingName);
                    nameHashMap.put(searchIndex, namesList);
                    noMatch = true;
                }
                else {
                    for (int idx = 0; idx < resultsArray.length(); idx++) {
                        // Lägg namnen från Json-array till vår temp array.
                        namesList.add(resultsArray.get(idx).toString());
                    }
                    nameHashMap.put(searchIndex, namesList);
                    noMatch = false;
                }
            }
            catch(Exception e) {
                System.out.println("Error when parsing JSON.");
                e.printStackTrace();
            }
        }

        /**
         * onPostExecute kallas efter all data hämtats från servern.
         * @param theNames
         */
        protected void onPostExecute(String theNames) {
            try{
                JSONObject jsonObject = new JSONObject(theNames);

                // Ta JSON-array som returnerades från servern som heter "result".
                JSONArray resultsArray = jsonObject.getJSONArray("result");

                // Lägga dit namnen till en hashmap.
                addNamesToHashmap(resultsArray);

                // Hitta hur lång den längsta strängen i JSON-array är. Används när ritar ut listan.
                lengthLongestString = getLengthOfLongestString(resultsArray);

                // Antal förslagna namn.
                nrNameSuggestions = resultsArray.length();

            }catch (Exception e){
                System.out.println("Error in onPostExecute.");
                e.printStackTrace();
            }

            // Initiera adaptern till alla namn.
            nameAdapter = new NameAdapter(context, nameHashMap.get(searchIndex));

            // Popup-listan ska vara lika bred som det längsta namnförslaget.
            if(noMatch){
                listPopupWindow.setWidth(noMatchingName.length() * (int)xdpi / 16);
            }else{
                listPopupWindow.setWidth(lengthLongestString * (int)xdpi / 16);
            }

            listPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

            // Visa adaptern.
            listPopupWindow.setAdapter(nameAdapter);
            listPopupWindow.show();
        }
    }
}






