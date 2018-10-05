package info.alexanderchen.represent;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import info.alexanderchen.represent.data.CongressMemberWrapper;
import info.alexanderchen.represent.fragment.ProfileTab;

public class DetailPageActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private CongressMemberWrapper congressMemberWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        congressMemberWrapper = getIntent().getExtras().getParcelable("congressMemberWrapper");
        setContentView(R.layout.activity_profile);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.viewPagerContainer);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        TextView textViewBackButton = findViewById(R.id.textViewBackButton);
        if(congressMemberWrapper.getDistrict().equals(""))
            textViewBackButton.setText(congressMemberWrapper.getState());
        else
            textViewBackButton.setText(congressMemberWrapper.getState() + "-" + congressMemberWrapper.getDistrict());
        ConstraintLayout backContainer = findViewById(R.id.backContainer);
        backContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView imageViewProfileImage = findViewById(R.id.imageViewProfileMemberImage);
        ImageView imageViewPartyLogo = findViewById(R.id.imageViewProfilePartyLogo);
        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.no_profile_img).centerCrop();
        Glide.with(this).setDefaultRequestOptions(requestOptions).load(congressMemberWrapper.getImageURL()).into(imageViewProfileImage);

        switch (congressMemberWrapper.getParty()) {
            case "Democratic":
                imageViewPartyLogo.setImageResource(R.drawable.democratic);
                break;

            case "Independent":
                imageViewPartyLogo.setImageResource(R.drawable.independent);
                break;

            case "Republican":
                imageViewPartyLogo.setImageResource(R.drawable.republican);
        }

        TextView textViewMemberName = findViewById(R.id.textViewProfileMemberName);
        TextView textViewMemberDetail = findViewById(R.id.textViewProfileDetails);

        textViewMemberName.setText(congressMemberWrapper.getName());
        if(congressMemberWrapper.isSenator())
            textViewMemberDetail.setText(congressMemberWrapper.getParty()+" | "+congressMemberWrapper.getShortTitle()+" | "+congressMemberWrapper.getFullState());
        else
            textViewMemberDetail.setText(congressMemberWrapper.getParty()+" | "+congressMemberWrapper.getShortTitle()+" | "+congressMemberWrapper.getFullState()+" | "+congressMemberWrapper.getFullDistrict());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_placeholder, container, false);
            return rootView;
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("congressMemberWrapper", congressMemberWrapper);
            switch (position) {
                case 0:
                    ProfileTab tab1 = new ProfileTab();
                    tab1.setArguments(bundle);
                    return tab1;
                case 1:
                    ProfileTab tab2 = new ProfileTab();
                    tab2.setArguments(bundle);
                    return tab2;
                case 2:
                    ProfileTab tab3 = new ProfileTab();
                    tab3.setArguments(bundle);
                    return tab3;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
