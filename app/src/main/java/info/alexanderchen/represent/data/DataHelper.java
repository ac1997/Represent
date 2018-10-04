package info.alexanderchen.represent.data;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.util.Pair;
import android.widget.Filter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import info.alexanderchen.represent.HomepageActivity;

public class DataHelper {
    private static final String CURRENT_LOCATION = "Current Location";
    private static final String RANDOM_LOCATION = "Random Location";

    private static final String GEOCODIO_API_BASE_URL = "https://api.geocod.io/v1.3/";
    private static final String GEOCODIO_API_KEY = "&api_key=151b1577336556aec761babcbb5b11616b3ba0c";

    private static final String PROPUBLICA_API_BASE_URL = "https://api.propublica.org/congress/v1";
    private static final String PROPUBLICA_API_KEY = "wDLaqJO9eo46ODiznW024sRPIR1LBN6PBJgABNKT";

    private static List<Pair<Integer, Integer>> sValidZipCodes = new ArrayList<>(Arrays.asList(
            new Pair<>(99501, 99950), new Pair<>(35004, 36925),
            new Pair<>(71601, 72959), new Pair<>(75502, 75502),
            new Pair<>(85001, 86556), new Pair<>(90001, 96162),
            new Pair<>(80001, 81658), new Pair<>(6001, 6389),
            new Pair<>(6401, 6928), new Pair<>(20001, 20039),
            new Pair<>(20042, 20599), new Pair<>(20799, 20799),
            new Pair<>(19701, 19980), new Pair<>(32004, 34997),
            new Pair<>(30001, 31999), new Pair<>(39901, 39901),
            new Pair<>(96701, 96898), new Pair<>(50001, 52809),
            new Pair<>(68119, 68120), new Pair<>(83201, 83876),
            new Pair<>(60001, 62999), new Pair<>(46001, 47997),
            new Pair<>(66002, 67954), new Pair<>(40003, 42788),
            new Pair<>(70001, 71232), new Pair<>(71234, 71497),
            new Pair<>(1001, 2791), new Pair<>(5501, 5544),
            new Pair<>(20331, 20331), new Pair<>(20335, 20797),
            new Pair<>(20812, 21930), new Pair<>(3901, 4992),
            new Pair<>(48001, 49971), new Pair<>(55001, 56763),
            new Pair<>(63001, 65899), new Pair<>(38601, 39776),
            new Pair<>(71233, 71233), new Pair<>(59001, 59937),
            new Pair<>(27006, 28909), new Pair<>(58001, 58856),
            new Pair<>(68001, 69367), new Pair<>(3031, 3897), 
            new Pair<>(7001, 8989), new Pair<>(87001, 88441), 
            new Pair<>(88901, 89883), new Pair<>(6390, 6390), 
            new Pair<>(10001, 14975), new Pair<>(43001, 45999),
            new Pair<>(73401, 74966), new Pair<>(97001, 97920),
            new Pair<>(15001, 19640), new Pair<>(2801, 2940),
            new Pair<>(29001, 29948), new Pair<>(57001, 57799),
            new Pair<>(37010, 38589), new Pair<>(73301, 73301),
            new Pair<>(75001, 75501), new Pair<>(75503, 79999),
            new Pair<>(88510, 88589), new Pair<>(84001, 84784),
            new Pair<>(22001, 24658), new Pair<>(5601, 5907),
            new Pair<>(98001, 99403), new Pair<>(53001, 54990),
            new Pair<>(24701, 26886), new Pair<>(82001, 83128)));
    private static String actualAddress;
    private static List<String> congressionalDistricts = new ArrayList<>();

    private static List<ZipCodeWrapper> sZipCodeWrappers = new ArrayList<>();

    private static List<ZipCodeSuggestion> sZipCodeSuggestions =
            new ArrayList<>(Arrays.asList(
                    new ZipCodeSuggestion(CURRENT_LOCATION),
                    new ZipCodeSuggestion(RANDOM_LOCATION)));

    public interface OnFindResultsListener {
        void onResults(List<ZipCodeWrapper> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<ZipCodeSuggestion> results);
    }

    public static List<ZipCodeSuggestion> getHistory(Context context, int count) {

        List<ZipCodeSuggestion> suggestionList = new ArrayList<>();
        ZipCodeSuggestion zipCodeSuggestion;
        for (int i = 0; i < sZipCodeSuggestions.size(); i++) {
            zipCodeSuggestion = sZipCodeSuggestions.get(i);
            zipCodeSuggestion.setIsHistory(true);
            suggestionList.add(zipCodeSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (ZipCodeSuggestion zipCodeSuggestion : sZipCodeSuggestions) {
            if (zipCodeSuggestion.getmZipCode().equals(CURRENT_LOCATION) || zipCodeSuggestion.getmZipCode().equals(RANDOM_LOCATION))
                continue;
            else
                zipCodeSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<ZipCodeSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (ZipCodeSuggestion suggestion : sZipCodeSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase()) || suggestion.getBody().toUpperCase()
                                .equals(CURRENT_LOCATION.toUpperCase()) || suggestion.getBody().toUpperCase()
                                .equals(RANDOM_LOCATION.toUpperCase())) {
                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<ZipCodeSuggestion>() {
                    @Override
                    public int compare(ZipCodeSuggestion lhs, ZipCodeSuggestion rhs) {
                        if (lhs.getmZipCode().equals((CURRENT_LOCATION.toUpperCase())))
                            return -1;
                        else if (lhs.getmZipCode().equals((RANDOM_LOCATION.toUpperCase())))
                            return 0;
                        else
                            return 1;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<ZipCodeSuggestion>) results.values);
                }
            }
        }.filter(query);
    }


    public static void findResults(final Context context, String query, final RequestQueue queue, FusedLocationProviderClient fusedLocationClient, final OnFindResultsListener listener) {
//        String url = "https://api.geocod.io/v1.3/geocode?q=1109+N+Highland+St%2c+Arlington+VA&fields=cd,stateleg&api_key=151b1577336556aec761babcbb5b11616b3ba0c";
//
////         Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the first 500 characters of the response string.
//                        Log.d("VOLLEY: ","Response is: "+ response.substring(0,500));
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("VOLLEY: ","That didn't work!"+query);
//            }
//        });
//
////         Add the request to the RequestQueue.
//        queue.add(stringRequest);
        Log.d("VOLLEY: ",query);
        if (query.equals(CURRENT_LOCATION)) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                double latitude = location.getLatitude();
                                double longitude = location.getLongitude();
                                String latLong =  Double.toString(latitude) + "," + Double.toString(longitude);

                                String url = GEOCODIO_API_BASE_URL+"reverse?q="+latLong+GEOCODIO_API_KEY;

                                StringRequest reverseGeocodioRequest = new StringRequest(Request.Method.GET, url,
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObj = new JSONObject(response);
                                                    JSONArray array = jsonObj.getJSONArray("results");
                                                    actualAddress = array.getJSONObject(0).getString("formatted_address");
                                                    Log.d("VOLLEY: ","Response is: "+ actualAddress);
                                                    requestResults(context, queue, listener);
                                                } catch (JSONException e) {
                                                    Log.e("JSON Exception", e.toString());
                                                }
                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d("VOLLEY: ","That didn't work!");
                                    }
                                });
                                queue.add(reverseGeocodioRequest);
                            } else {
                                Log.d("Current Location:3", "NULL DETECTED");
                            }
                        }
                    });
        } else if (query.equals(RANDOM_LOCATION)) {
            Pair<Integer, Integer> pair = sValidZipCodes.get(ThreadLocalRandom.current().nextInt(0, sValidZipCodes.size()));
            actualAddress = Integer.toString(ThreadLocalRandom.current().nextInt(pair.first, pair.second+1));
            requestResults(context, queue, listener);


            Log.d("FINDRESULTS", "Random Location");
        } else {
            query = query.trim();
            if (query.length() != 5 || !query.matches("[0-9]+")) {
                Toast.makeText(context, "Invalid Zip Code Entered", Toast.LENGTH_LONG).show();
                return;
            } else {
                actualAddress = query;
                requestResults(context, queue, listener);
            }
        }
    }

    private static void getCongressionalDistricts(Context context, RequestQueue queue) {
        congressionalDistricts.clear();
        String url = GEOCODIO_API_BASE_URL+"geocode?q="+actualAddress+"&fields=cd115"+GEOCODIO_API_KEY;

        StringRequest reverseGeocodioRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            String state = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("address_components").getString("state");

                            Log.d("VOLLEY: ","State is: "+ state);

                            JSONArray array = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts");
                            for(int i = 0 ; i < array.length() ; i++){
                                congressionalDistricts.add(state + " " + array.getJSONObject(i).getString("district_number"));
                                Log.d("VOLLEY LOOP: ","Congressional District is: "+ congressionalDistricts.get(i));
                            }

                        } catch (JSONException e) {
                            Log.e("JSON Exception", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY: ","That didn't work!");
            }
        });
        queue.add(reverseGeocodioRequest);
    }

    public static void requestResults(Context context, RequestQueue queue, final OnFindResultsListener listener) {
        congressionalDistricts.clear();
        String url = GEOCODIO_API_BASE_URL+"geocode?q="+actualAddress+"&fields=cd115"+GEOCODIO_API_KEY;

        StringRequest reverseGeocodioRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);

                            String state = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("address_components").getString("state");

                            Log.d("VOLLEY: ","State is: "+ state);

                            JSONArray array = jsonObj.getJSONArray("results").getJSONObject(0).getJSONObject("fields").getJSONArray("congressional_districts");
                            for(int i = 0 ; i < array.length() ; i++){
                                congressionalDistricts.add(state + " " + array.getJSONObject(i).getString("district_number"));
                                Log.d("VOLLEY LOOP: ","Congressional District is: "+ congressionalDistricts.get(i));
                            }

                            sZipCodeWrappers.clear();
                            sZipCodeWrappers.add(new ZipCodeWrapper(CURRENT_LOCATION));
                            sZipCodeWrappers.add(new ZipCodeWrapper(RANDOM_LOCATION));
                            Log.d("FINDRESULTS", actualAddress);
                            new Filter() {
                                @Override
                                protected FilterResults performFiltering(CharSequence constraint) {


                                    List<ZipCodeWrapper> suggestionList = new ArrayList<>();

                                    if (!(constraint == null || constraint.length() == 0)) {

                                        for (ZipCodeWrapper zipCode : sZipCodeWrappers) {
                                            if (zipCode.getZipCode().toUpperCase()
                                                    .startsWith(constraint.toString().toUpperCase()) || zipCode.getZipCode().toUpperCase()
                                                    .equals(CURRENT_LOCATION.toUpperCase()) || zipCode.getZipCode().toUpperCase()
                                                    .equals(RANDOM_LOCATION.toUpperCase())) {
                                                suggestionList.add(zipCode);
                                            }
                                        }

                                    }

                                    FilterResults results = new FilterResults();
                                    results.values = suggestionList;
                                    results.count = suggestionList.size();

                                    return results;
                                }

                                @Override
                                protected void publishResults(CharSequence constraint, FilterResults results) {

                                    if (listener != null) {
                                        listener.onResults((List<ZipCodeWrapper>) results.values);
                                    }
                                }
                            }.filter("Location");

                        } catch (JSONException e) {
                            Log.e("JSON Exception", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY: ","That didn't work!");
            }
        });
        queue.add(reverseGeocodioRequest);
    }
}