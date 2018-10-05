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
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class DataHelper {
    private static final String CURRENT_LOCATION = "Current Location";
    private static final String RANDOM_LOCATION = "Random Location";

    private static final String GEOCODIO_API_BASE_URL = "https://api.geocod.io/v1.3/";
    private static final String GEOCODIO_API_KEY = "&api_key=151b1577336556aec761babcbb5b11616b3ba0c";

    private static final String PROPUBLICA_API_BASE_URL = "https://api.propublica.org/congress/v1/";
    private static final String PROPUBLICA_API_KEY = "wDLaqJO9eo46ODiznW024sRPIR1LBN6PBJgABNKT";

    private static List<Pair<Integer, Integer>> sValidZipCodes = new ArrayList<>(Arrays.asList(
            new Pair<>(35801, 35816), new Pair<>(99501, 99524),
            new Pair<>(85001, 85055), new Pair<>(72201, 72217),
            new Pair<>(94203, 94209), new Pair<>(90001, 90089),
            new Pair<>(90209, 90213), new Pair<>(80201, 80239),
            new Pair<>(6101, 6112), new Pair<>(19901, 19905),
            new Pair<>(20001, 20020), new Pair<>(32501, 32509),
            new Pair<>(33124, 33190), new Pair<>(32801, 32837),
            new Pair<>(30301, 30381), new Pair<>(96801, 96830),
            new Pair<>(83254, 83254), new Pair<>(60601, 60641),
            new Pair<>(62701, 62709), new Pair<>(46201, 46209),
            new Pair<>(52801, 52809), new Pair<>(50301, 50323),
            new Pair<>(67201, 67221), new Pair<>(41701, 41702),
            new Pair<>(70112, 70119), new Pair<>(4032, 4034),
            new Pair<>(21201, 21237), new Pair<>(2101, 2137),
            new Pair<>(49036, 49036), new Pair<>(49734, 49735),
            new Pair<>(55801, 55808), new Pair<>(39530, 39535),
            new Pair<>(63101, 63141), new Pair<>(59044, 59044),
            new Pair<>(68901, 68902), new Pair<>(89501, 89513),
            new Pair<>(3217, 3217), new Pair<>(7039, 7039),
            new Pair<>(87500, 87506), new Pair<>(10001, 10048),
            new Pair<>(27565, 27565), new Pair<>(58282, 58282),
            new Pair<>(44101, 44179), new Pair<>(74101, 74110),
            new Pair<>(97201, 97225), new Pair<>(15201, 15244),
            new Pair<>(2840, 2841), new Pair<>(29020, 29020),
            new Pair<>(57401, 57402), new Pair<>(37201, 37222),
            new Pair<>(78701, 78705), new Pair<>(84321, 84323),
            new Pair<>(5751, 5751), new Pair<>(24517, 24517),
            new Pair<>(98004, 98009), new Pair<>(25813, 25813),
            new Pair<>(53201, 53228), new Pair<>(82941, 82941)));
    private static String actualAddress;
    private static List<String> congressionalDistricts = new ArrayList<>();

    private static List<ZipCodeSuggestion> sZipCodeSuggestions =
            new ArrayList<>(Arrays.asList(
                    new ZipCodeSuggestion(CURRENT_LOCATION),
                    new ZipCodeSuggestion(RANDOM_LOCATION)));

    private static List<CongressMemberWrapper> congressMemberWrappers = new ArrayList<>();

    public interface OnFindResultsListener {
        void onResults(List<CongressMemberWrapper> results);
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
        Log.d("VOLLEY: ",query);
        switch (query) {
            case CURRENT_LOCATION:
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    double latitude = location.getLatitude();
                                    double longitude = location.getLongitude();
                                    String latLong = Double.toString(latitude) + "," + Double.toString(longitude);

                                    String url = GEOCODIO_API_BASE_URL + "reverse?q=" + latLong + GEOCODIO_API_KEY;

                                    JsonObjectRequest reverseGeocodioRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                                            new Response.Listener<JSONObject>() {
                                                @Override
                                                public void onResponse(JSONObject response) {
                                                    try {
                                                        JSONArray array = response.getJSONArray("results");
                                                        actualAddress = array.getJSONObject(0).getString("formatted_address");
                                                        Log.d("VOLLEY: ", "Response is: " + actualAddress);
                                                        requestResults(context, queue, listener);
                                                    } catch (JSONException e) {
                                                        Log.e("JSON Exception", e.toString());
                                                    }
                                                }
                                            },
                                            new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("VOLLEY: ", "That didn't work!");
                                                }
                                            });
                                    queue.add(reverseGeocodioRequest);
                                } else {
                                    Log.e("Location Service", "Returned null");
                                }
                            }
                        });
                break;
            case RANDOM_LOCATION:
                Pair<Integer, Integer> pair = sValidZipCodes.get(ThreadLocalRandom.current().nextInt(0, sValidZipCodes.size()));
                Log.d("RANDOM LOCATION", pair.toString());
                actualAddress = Integer.toString(ThreadLocalRandom.current().nextInt(pair.first, pair.second + 1));
                requestResults(context, queue, listener);


                Log.d("RANDOM LOCATION", actualAddress);
                break;
            default:
                query = query.trim();
                if (query.length() != 5 || !query.matches("[0-9]+")) {
                    Toast.makeText(context, "Invalid Zip Code Entered", Toast.LENGTH_LONG).show();
                } else {
                    actualAddress = query;
                    requestResults(context, queue, listener);
                }
                break;
        }
    }

    public static void requestResults(Context context, final RequestQueue queue, final OnFindResultsListener listener) {
        congressMemberWrappers.clear();
        congressionalDistricts.clear();
        String geocodioUrl = GEOCODIO_API_BASE_URL+"geocode?q="+actualAddress+"&fields=cd115"+GEOCODIO_API_KEY;

        JsonObjectRequest geocodeGeocodioRequest = new JsonObjectRequest(Request.Method.GET, geocodioUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Set<String> memberId = new HashSet<>();
                            Set<String> states = new HashSet<>();
                            Set<String> stateAndDistricts = new HashSet<>();

                            JSONArray resultsArr = response.getJSONArray("results");

                            for (int i = 0; i < resultsArr.length(); i++) {
                                JSONObject result = resultsArr.getJSONObject(i);
                                String state = result.getJSONObject("address_components").getString("state");
                                states.add(state);

                                JSONArray districtsArray = result.getJSONObject("fields").getJSONArray("congressional_districts");

                                for (int j = 0; j < districtsArray.length(); j++) {
                                    String districtNumber = districtsArray.getJSONObject(j).getString("district_number");
                                    if(districtNumber.equals("0"))
                                        districtNumber = "1";
                                    stateAndDistricts.add(state+"/"+districtNumber);
                                }
                            }

                            for (String s : states) {
                                String url = PROPUBLICA_API_BASE_URL+"members/senate/"+s+"/current.json";
                                JsonObjectRequest proPublicaSenateRequest = new JsonObjectRequest(Request.Method.GET,
                                        url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                requestMemberDetails(response, queue, listener);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("VOLLEY: ","That didn't work!");
                                            }
                                        }
                                ) {
                                    @Override
                                    public Map getHeaders() {
                                        HashMap headers = new HashMap();
                                        headers.put("X-API-Key", "wDLaqJO9eo46ODiznW024sRPIR1LBN6PBJgABNKT");
                                        return headers;
                                    }
                                };
                                queue.add(proPublicaSenateRequest);
                            }

                            for (String s : stateAndDistricts) {
                                String url = PROPUBLICA_API_BASE_URL+"members/house/"+s+"/current.json";
                                JsonObjectRequest proPublicaSenateRequest = new JsonObjectRequest(Request.Method.GET,
                                        url, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                requestMemberDetails(response, queue, listener);
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                Log.d("VOLLEY: ","That didn't work!");
                                            }
                                        }
                                ) {
                                    @Override
                                    public Map getHeaders() {
                                        HashMap headers = new HashMap();
                                        headers.put("X-API-Key", "wDLaqJO9eo46ODiznW024sRPIR1LBN6PBJgABNKT");
                                        return headers;
                                    }
                                };
                                queue.add(proPublicaSenateRequest);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("VOLLEY: ","That didn't work!");
                    }
                });
        queue.add(geocodeGeocodioRequest);
    }

    public static void requestMemberDetails(JSONObject jsonObj, final RequestQueue queue, final OnFindResultsListener listener) {
        try {
            JSONArray resultsArr = jsonObj.getJSONArray("results");
            for (int i = 0; i < resultsArr.length(); i++) {
                final String fullName = resultsArr.getJSONObject(i).getString("name");
                final String url = resultsArr.getJSONObject(i).getString("api_uri");

                JsonObjectRequest proPublicaSenateRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("PROPUBLICA SUCCESS 1", response.toString());
                                parseCongressMemberJSON(response, fullName, url, listener);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY: ","That didn't work!");
                            }
                        }
                ) {
                    @Override
                    public Map getHeaders() {
                        HashMap headers = new HashMap();
                        headers.put("X-API-Key", "wDLaqJO9eo46ODiznW024sRPIR1LBN6PBJgABNKT");
                        return headers;
                    }
                };
                queue.add(proPublicaSenateRequest);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void parseCongressMemberJSON(JSONObject jsonObj, String fullName, String api_uri, final OnFindResultsListener listener) {
        try {
            JSONObject result = jsonObj.getJSONArray("results").getJSONObject(0);

            CongressMemberWrapper congressMemberWrapper = new CongressMemberWrapper(result.getString("member_id"),
                    fullName, result.getString("url"), result.getString("twitter_account"),
                    result.getString("facebook_account"), result.getString("youtube_account"), api_uri);

            result = result.getJSONArray("roles").getJSONObject(0);

            if (result.getString("short_title").equals("Sen.")) {
                congressMemberWrapper.roleSectionUpdates(result.getString("chamber"), result.getString("title"), result.getString("party"),
                        result.getString("state"), result.getString("start_date"), result.getString("end_date"), result.getString("office"), result.getString("phone"),
                        result.getString("contact_form"));
            } else {
                congressMemberWrapper.roleSectionUpdates(result.getString("chamber"), result.getString("title"), result.getString("party"),
                        result.getString("state"), result.getString("district"), result.getString("start_date"),
                        result.getString("end_date"), result.getString("office"), result.getString("phone"),
                        result.getString("contact_form"));
            }


            List<CommitteeWrapper> committeeWrappers = new ArrayList<>();
            JSONArray committees = result.getJSONArray("committees");
            for (int i = 0; i < committees.length(); i++) {
                JSONObject committee = committees.getJSONObject(i);
                committeeWrappers.add(new CommitteeWrapper(committee.getString("name"),
                        committee.getString("code"), committee.getString("side"), committee.getString("title"),
                        committee.getString("end_date"), committee.getString("api_uri")));
            }
            congressMemberWrapper.setCommittees(committeeWrappers);

            List<SubCommitteeWrapper> subCommitteeWrappers = new ArrayList<>();
            JSONArray subCommittees = result.getJSONArray("subcommittees");
            for (int i = 0; i < subCommittees.length(); i++) {
                JSONObject subCommittee = subCommittees.getJSONObject(i);
                subCommitteeWrappers.add(new SubCommitteeWrapper(subCommittee.getString("name"),
                        subCommittee.getString("code"), subCommittee.getString("parent_committee_id"),
                        subCommittee.getString("side"), subCommittee.getString("title"),
                        subCommittee.getString("end_date"), subCommittee.getString("api_uri")));
            }
            congressMemberWrapper.setSubCommittees(subCommitteeWrappers);
            Log.e("MEMBERS", congressMemberWrapper.toString());
            congressMemberWrappers.add(congressMemberWrapper);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (listener != null) {
            listener.onResults(congressMemberWrappers);
        }
    }
}