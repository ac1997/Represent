package info.alexanderchen.represent;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import java.util.List;

import info.alexanderchen.represent.adapter.SearchResultsListAdapter;
import info.alexanderchen.represent.data.CongressMemberWrapper;
import info.alexanderchen.represent.data.DataHelper;
import info.alexanderchen.represent.data.ZipCodeSuggestion;
import info.alexanderchen.represent.decoration.InsetDividerItemDecoration;

public class HomepageActivity extends AppCompatActivity {

    private static final String CURRENT_LOCATION = "Current Location";
    private static final String RANDOM_LOCATION = "Random Location";

    private ImageView mCogressImageView;
    private TextView mNoDataTitleTextView;
    private Button mShowMeSomethingButton;

    private final String TAG = "BlankFragment";

    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;

    private AppBarLayout appBarLayoutMain;
    private LinearLayout linearLayoutMask;
    private FloatingSearchView mSearchView;
    private RecyclerView mSearchResultsList;
    private SearchResultsListAdapter mSearchResultsAdapter;
    private ImageView imageViewLoader;
    private ValueAnimator colorAnim;

    private final String SAVED_RECYCLER_VIEW_STATUS_ID = "recycler_view_status";
    private final String SAVED_RECYCLER_VIEW_DATASET_ID = "recycler_view_dataset";
    private static Bundle mBundleRecyclerViewState;

    private boolean mSuggestionClicked = false;

    private String mLastQuery = "";

    private static RequestQueue volleyRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
    }

    @Override
    protected void onStart() {
        super.onStart();

        linearLayoutMask = findViewById( R.id.linearLayoutMask);
        linearLayoutMask.getForeground().setAlpha(0);
        linearLayoutMask.setVisibility(View.GONE);
        appBarLayoutMain = findViewById(R.id.appBarLayoutMain);
        appBarLayoutMain.getForeground().setAlpha(0);
        mSearchView = findViewById(R.id.floating_search_view);
        mSearchResultsList = findViewById(R.id.search_results_list);
        imageViewLoader = findViewById(R.id.imageViewLoader);
        imageViewLoader.setVisibility(View.GONE);

        mCogressImageView = findViewById(R.id.imageViewCongressBg);
        mNoDataTitleTextView = findViewById(R.id.textViewNoDataTitle);
        mShowMeSomethingButton = findViewById(R.id.buttonShowMeSomething);

        mShowMeSomethingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLastQuery = RANDOM_LOCATION;
                mSearchView.setSearchText(mLastQuery);
                showLoadingView();
                mSuggestionClicked = true;
                queryResults(mLastQuery);
            }
        });

        volleyRequestQueue = Volley.newRequestQueue(this);

        setupFloatingSearch();
        setupResultsList();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = mSearchResultsList.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, listState);
        mBundleRecyclerViewState.putParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID, mSearchResultsAdapter.getmDataSet());
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            showLoadingView();
            Parcelable listState = mBundleRecyclerViewState.getParcelable(SAVED_RECYCLER_VIEW_STATUS_ID);
            mSearchResultsAdapter.setmDataSet(mBundleRecyclerViewState.getParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID));
            mSearchResultsList.getLayoutManager().onRestoreInstanceState(listState);
            restoreViewAfterLoading();
        }
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
                    mLastQuery = newQuery;
                    mSearchView.showProgress();

                    DataHelper.findSuggestions(null, newQuery, 5,
                            FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {
                                @Override
                                public void onResults(List<ZipCodeSuggestion> results) {
                                    mSearchView.swapSuggestions(results);
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
                mSuggestionClicked = true;
                mLastQuery = searchSuggestion.getBody();
                mSearchView.setSearchText(mLastQuery);
                mSearchView.clearSearchFocus();
                showLoadingView();
                ZipCodeSuggestion zipCodeSuggestion = (ZipCodeSuggestion) searchSuggestion;
                queryResults(zipCodeSuggestion.getBody());
                Log.d(TAG, "onSuggestionClicked()");
            }

            @SuppressLint("MissingPermission")
            @Override
            public void onSearchAction(String query) {
                mSuggestionClicked = true;
                mLastQuery = query;
                mSearchView.setSearchText(mLastQuery);
                mSearchView.clearSearchFocus();
                showLoadingView();
                queryResults(query);

                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                mSearchView.swapSuggestions(DataHelper.getHistory(getApplicationContext(), 5));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {
                mSearchView.setSearchText(mLastQuery);

                Log.d(TAG, "onFocusCleared()");
            }
        });

        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                showLoadingView();
                if (item.getItemId() == R.id.action_location)
                    mLastQuery = CURRENT_LOCATION;
                else if (item.getItemId() == R.id.action_random)
                    mLastQuery = RANDOM_LOCATION;

                Log.d(TAG, "onSearchAction() query: " + mLastQuery);

                mSuggestionClicked = true;
                mSearchView.setSearchText(mLastQuery);
                queryResults(mLastQuery);
            }
        });

        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                ZipCodeSuggestion zipCodeSuggestion = (ZipCodeSuggestion) item;
                String text;

                if (zipCodeSuggestion.getmZipCode().equals(CURRENT_LOCATION)) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_gps_fixed_black_24dp, null));
                    Util.setIconColor(leftIcon, Color.parseColor("#6b94fa"));
                    leftIcon.setAlpha(.36f);

                    textView.setTextColor(Color.parseColor("#6b94fa"));
                    text = zipCodeSuggestion.getBody()
                            .replaceFirst(mSearchView.getQuery(),
                                    "<font color=\"#215ff8\">" + mSearchView.getQuery() + "</font>");
                } else if (zipCodeSuggestion.getmZipCode().equals(RANDOM_LOCATION)) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_dice, null));
                    Util.setIconColor(leftIcon, Color.parseColor("#fdab40"));
                    leftIcon.setAlpha(.36f);

                    textView.setTextColor(Color.parseColor("#fdab40"));
                    text = zipCodeSuggestion.getBody()
                            .replaceFirst(mSearchView.getQuery(),
                                    "<font color=\"#c77c02\">" + mSearchView.getQuery() + "</font>");
                } else if (zipCodeSuggestion.getIsHistory()) {
                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
                            R.drawable.ic_history_black_24dp, null));
                    leftIcon.setAlpha(.36f);

                    textView.setTextColor(Color.parseColor("#000000"));
                    text = zipCodeSuggestion.getBody()
                            .replaceFirst(mSearchView.getQuery(),
                                    "<font color=\"#787878\">" + mSearchView.getQuery() + "</font>");
                } else {
                    leftIcon.setAlpha(0.0f);
                    leftIcon.setImageDrawable(null);

                    textView.setTextColor(Color.parseColor("#000000"));
                    text = zipCodeSuggestion.getBody()
                            .replaceFirst(mSearchView.getQuery(),
                                    "<font color=\"#787878\">" + mSearchView.getQuery() + "</font>");
                }

                textView.setText(Html.fromHtml(text));
            }

        });

        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                mSearchResultsList.setTranslationY(newHeight);
            }
        });

        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {
                mLastQuery = "";

                DataHelper.findSuggestions(null, "", 5,
                        FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {
                            @Override
                            public void onResults(List<ZipCodeSuggestion> results) {
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
        mSearchResultsList.addItemDecoration(new InsetDividerItemDecoration(getApplicationContext(), R.id.member_name));
    }

    private void queryResults(String query) {
        DataHelper.findResults(HomepageActivity.this, query, volleyRequestQueue,
                new DataHelper.OnFindResultsListener() {

                    @Override
                    public void onResults(List<CongressMemberWrapper> results, boolean isCompleted) {
                        if (results != null) {
                            mSearchResultsAdapter.swapData(results);
                            if (isCompleted)
                                restoreViewAfterLoading();
                        } else {
                            showErrorView();
                            restoreViewAfterLoading();
                        }
                    }

                },
                new DataHelper.OnZipcodeResultListener() {
                    @Override
                    public void onResults(String zipcode) {
                        mLastQuery = zipcode;
                        mSearchView.setSearchText(zipcode);
                    }
                });
    }

    private void showLoadingView() {
        findViewById( R.id.relativeLayoutMain).setBackgroundColor(Color.WHITE);
        appBarLayoutMain.getForeground().setAlpha(150);
        linearLayoutMask.getForeground().setAlpha(150);
        linearLayoutMask.setVisibility(View.VISIBLE);

        mCogressImageView.setVisibility(View.GONE);
        mNoDataTitleTextView.setVisibility(View.GONE);
        mShowMeSomethingButton.setVisibility(View.GONE);
        imageViewLoader.setVisibility(View.VISIBLE);
        animateImageView(imageViewLoader);
    }

    private void restoreViewAfterLoading() {
        appBarLayoutMain.getForeground().setAlpha(0);
        linearLayoutMask.getForeground().setAlpha(0);
        linearLayoutMask.setVisibility(View.GONE);
        imageViewLoader.setVisibility(View.GONE);
        colorAnim.end();
    }

    private void showErrorView() {
        mCogressImageView.setVisibility(View.GONE);
        mNoDataTitleTextView.setText("Unable to load data. Please try again later.");
        mNoDataTitleTextView.setVisibility(View.VISIBLE);
    }

    public void animateImageView(final ImageView v) {
        final int accent = ContextCompat.getColor(v.getContext(), R.color.colorLoadingImage);

        colorAnim = ObjectAnimator.ofFloat(0f, 1f);
        colorAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float mul = (Float) animation.getAnimatedValue();
                int alphaOrange = adjustAlpha(accent, mul);
                v.setColorFilter(alphaOrange, PorterDuff.Mode.SRC_ATOP);
                if (mul == 0.0) {
                    v.setColorFilter(null);
                }
            }
        });

        colorAnim.setDuration(1500);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.setRepeatCount(-1);
        colorAnim.start();
    }

    public int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}