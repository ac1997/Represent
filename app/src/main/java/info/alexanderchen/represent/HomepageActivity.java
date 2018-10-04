package info.alexanderchen.represent;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

import info.alexanderchen.represent.adapter.SearchResultsListAdapter;
import info.alexanderchen.represent.data.CongressMemberWrapper;
import info.alexanderchen.represent.data.DataHelper;
import info.alexanderchen.represent.data.ZipCodeSuggestion;
import info.alexanderchen.represent.data.ZipCodeWrapper;
import info.alexanderchen.represent.decoration.InsetDividerItemDecoration;

public class HomepageActivity extends AppCompatActivity {

    private static final String CURRENT_LOCATION = "Current Location";
    private static final String RANDOM_LOCATION = "Random Location";

    private FusedLocationProviderClient mFusedLocationClient;

    private ImageView mCogressImageView;
    private TextView mNoDataTitleTextView;
    private TextView mNoDataDetailTextView;
    private Button mShowMeSomethingButton;

    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private FloatingSearchView mSearchView;

    private RecyclerView mSearchResultsList;
    private SearchResultsListAdapter mSearchResultsAdapter;

    private boolean mSuggestionClicked = false;

    private String mLastQuery = "";
    private String actualQuery = "";

    private static RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mSearchView = findViewById(R.id.floating_search_view);
        mSearchResultsList = findViewById(R.id.search_results_list);
        mCogressImageView = findViewById(R.id.imageViewCongressBg);
        mNoDataTitleTextView = findViewById(R.id.textViewNoDataTitle);
        mNoDataDetailTextView = findViewById(R.id.textViewNoDataDetail);
        mShowMeSomethingButton = findViewById(R.id.buttonShowMeSomething);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        queue = Volley.newRequestQueue(this);

        setupFloatingSearch();
        setupResultsList();
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (mSuggestionClicked) {
                    mSuggestionClicked = false;
                    mSearchView.clearSuggestions();
                } else if (!oldQuery.equals("") && newQuery.equals("")) {
                    mLastQuery = "";
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mLastQuery = newQuery;
                    mSearchView.showProgress();

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(null, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                                @Override
                                public void onResults(List<ZipCodeSuggestion> results) {

                                    //this will swap the data and
                                    //render the collapse/expand animations as necessary
                                    mSearchView.swapSuggestions(results);

                                    //let the users know that the background
                                    //process has completed
                                    mSearchView.hideProgress();
                                }
                            });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                ZipCodeSuggestion zipCodeSuggestion = (ZipCodeSuggestion) searchSuggestion;
                DataHelper.findResults(HomepageActivity.this, zipCodeSuggestion.getBody(), queue, mFusedLocationClient,
                        new DataHelper.OnFindResultsListener() {

                            @Override
                            public void onResults(List<CongressMemberWrapper> results) {
                                hideBackground();
                                mSearchResultsAdapter.swapData(results);
                            }

                        });
                mSuggestionClicked = true;
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchText(mLastQuery);
                mSearchView.clearSearchFocus();
                Log.d(TAG, "onSuggestionClicked()");
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
                Log.d(TAG, "onSearchAction() query: " + query);

                DataHelper.findResults(HomepageActivity.this, query, queue, mFusedLocationClient,
                new DataHelper.OnFindResultsListener() {

                    @Override
                    public void onResults(List<CongressMemberWrapper> results) {
                        hideBackground();
                        mSearchResultsAdapter.swapData(results);
                    }

                });
                mSuggestionClicked = true;
                mSearchView.setSearchText(mLastQuery);
                mSearchView.clearSearchFocus();

                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(getApplicationContext(), 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchText(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_location)
                    mLastQuery = CURRENT_LOCATION;
                else if (item.getItemId() == R.id.action_random)
                    mLastQuery = RANDOM_LOCATION;

                Log.d(TAG, "onSearchAction() query: " + mLastQuery);

                DataHelper.findResults(HomepageActivity.this, mLastQuery, queue, mFusedLocationClient,
                        new DataHelper.OnFindResultsListener() {

                            @Override
                            public void onResults(List<CongressMemberWrapper> results) {
                                hideBackground();
                                mSearchResultsAdapter.swapData(results);
                            }

                        });
                mSuggestionClicked = true;
                mSearchView.setSearchText(mLastQuery);
            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ZipCodeSuggestion zipCodeSuggestion = (ZipCodeSuggestion) item;


                if (zipCodeSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));

                    leftIcon.setAlpha(.36f);
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);
                }

                String text = zipCodeSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"#000000\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                mLastQuery = "";

                DataHelper.findSuggestions(null, "", 5,
                        FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                            @Override
                            public void onResults(List<ZipCodeSuggestion> results) {

                                //this will swap the data and
                                //render the collapse/expand animations as necessary
                                mSearchView.swapSuggestions(results);
                            }
                        });
                Log.d(TAG, "onClearSearchClicked()");
            }
        });
    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListAdapter();
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSearchResultsList.addItemDecoration(new InsetDividerItemDecoration(
                getApplicationContext()
        ));
    }

    @Override
    public void onBackPressed() {
        //if mSearchView.setSearchFocused(false) causes the focused search
        //to close, then we don't want to close the activity. if mSearchView.setSearchFocused(false)
        //returns false, we know that the search was already closed so the call didn't change the focus
        //state and it makes sense to call supper onBackPressed() and close the activity
        super.onBackPressed();
//        if (!mSearchView.setSearchFocused(false)) {
//            return false;
//        }
//        return true;
    }

    private void hideBackground() {
        RelativeLayout rLayout = (RelativeLayout) findViewById(R.id.main_relative_layout);
        rLayout.setBackgroundColor(Color.WHITE);

        mCogressImageView.setVisibility(View.GONE);
        mNoDataTitleTextView.setVisibility(View.GONE);
        mNoDataDetailTextView.setVisibility(View.GONE);
        mShowMeSomethingButton.setVisibility(View.GONE);
    }
}