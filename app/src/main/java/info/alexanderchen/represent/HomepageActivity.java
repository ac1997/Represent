package info.alexanderchen.represent;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

public class HomepageActivity extends AppCompatActivity {

    FloatingSearchView mSearchView;

    private static final List<String> SOME_HARDCODED_DATA;
    static {
        SOME_HARDCODED_DATA = new ArrayList<>();
        SOME_HARDCODED_DATA.add("One");
        SOME_HARDCODED_DATA.add("Two");
        SOME_HARDCODED_DATA.add("Three");
        SOME_HARDCODED_DATA.add("Four");
    }
    private static class SimpleSuggestions implements SearchSuggestion {
        private final String mData;
        public SimpleSuggestions(String string) {
            mData = string;
        }
        @Override
        public String getBody() {
            return mData;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(mData);
        }

        public static final Parcelable.Creator<SimpleSuggestions> CREATOR
                = new Parcelable.Creator<SimpleSuggestions>() {
            public SimpleSuggestions createFromParcel(Parcel in) {
                return new SimpleSuggestions(in);
            }

            public SimpleSuggestions[] newArray(int size) {
                return new SimpleSuggestions[size];
            }
        };

        private SimpleSuggestions(Parcel in) {
            mData = in.readString();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        mSearchView = findViewById(R.id.floating_search_view);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                List<SearchSuggestion> list = new ArrayList<SearchSuggestion>();
                //emulating search on dummy data
                for (String item : SOME_HARDCODED_DATA) {
                    if (item.contains(newQuery)) {
                        list.add(new SimpleSuggestions(item));
                    }
                }
                mSearchView.swapSuggestions(list);
            }
        });
    }
}