package info.alexanderchen.represent.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.util.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import info.alexanderchen.represent.DetailPageActivity;
import info.alexanderchen.represent.R;
import info.alexanderchen.represent.Typefaces;
import info.alexanderchen.represent.data.CongressMemberWrapper;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private ViewGroup parent;

    private List<CongressMemberWrapper> mDataSet = new ArrayList<>();

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener{
        void onClick(CongressMemberWrapper congressMemberWrapper);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMemberPicture;
        public final ImageView mMemberParty;
        public final TextView mMemberName;
        public final TextView mMemberDesc;
        public final Button mEmailButton;
        public final Button mWebsiteButton;
        public final Button mTwitterButton;
        public final Button mFacebookButton;
        public final Button mYoutubeButton;

        public ViewHolder(View view) {
            super(view);
            mMemberPicture = view.findViewById(R.id.imageViewProfileMemberImage);
            mMemberParty = view.findViewById(R.id.member_party);
            mMemberName = view.findViewById(R.id.member_name);
            mMemberDesc = view.findViewById(R.id.member_basic_desc);
            mEmailButton = view.findViewById(R.id.button_email);
            mWebsiteButton = view.findViewById(R.id.button_website);
            mTwitterButton = view.findViewById(R.id.button_twitter);
            mFacebookButton = view.findViewById(R.id.button_facebook);
            mYoutubeButton = view.findViewById(R.id.button_youtube);
        }
    }

    public void swapData(List<CongressMemberWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);

        Button button;
        Typeface font = Typefaces.get(parent.getContext(), "fa-solid-900.ttf");

        button = view.findViewById(R.id.button_email);
        button.setTypeface(font);
        button = view.findViewById(R.id.button_website);
        button.setTypeface(font);

        font = Typefaces.get(parent.getContext(), "fa-brands-400.ttf");
        button = view.findViewById(R.id.button_twitter);
        button.setTypeface(font);
        button = view.findViewById(R.id.button_facebook);
        button.setTypeface(font);
        button = view.findViewById(R.id.button_youtube);
        button.setTypeface(font);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsListAdapter.ViewHolder holder, final int position) {

        final CongressMemberWrapper congressMember = mDataSet.get(position);

        String party = congressMember.getParty();
        String id = congressMember.getId();
        String url = "http://bioguide.congress.gov/bioguide/photo/"+id.charAt(0)+"/"+id+".jpg";

        holder.mMemberName.setText(congressMember.getName());
        holder.mMemberDesc.setText(party+" | "+congressMember.getTitle());

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.no_profile_img).centerCrop();
        Glide.with(parent.getContext()).setDefaultRequestOptions(requestOptions).load(url).into(holder.mMemberPicture);

        switch (party) {
            case "Democratic":
                holder.mMemberParty.setImageResource(R.drawable.democratic);
                break;

            case "Independent":
                holder.mMemberParty.setImageResource(R.drawable.independent);
                break;

            case "Republican":
                holder.mMemberParty.setImageResource(R.drawable.republican);
        }

        setButtonListener(holder.mEmailButton, congressMember.getContact_form(), "contact form");
        setButtonListener(holder.mWebsiteButton, congressMember.getWebsite(), "website URL");
        setButtonListener(holder.mTwitterButton, congressMember.getTwitter_id(), "Twitter profile");
        setButtonListener(holder.mFacebookButton, congressMember.getFacebook_id(), "Facebook profile");
        setButtonListener(holder.mYoutubeButton, congressMember.getYoutube_id(), "YouTube profile");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailPageActivity.class).putExtra("congressMemberWrapper", mDataSet.get(position));
                v.getContext().startActivity(intent);
                ((Activity) parent.getContext()).overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    private void animateItem(View view) {
        view.setTranslationY(Util.getScreenHeight((Activity) view.getContext()));
        view.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .start();
    }

    private void setButtonListener(Button button, final String id, final String socialMedia) {
        if (id.equals("null"))
            button.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.round_button_invalid));

        String url = null;
        switch (socialMedia) {
            case "contact form":
            case "website URL":
                url = id;
                break;
            case "Twitter profile":
                url = "https://twitter.com/" + id;
                break;
            case "Facebook profile":
                url = "https://www.facebook.com/" + id;
                break;
            case "YouTube profile":
                url = "https://www.youtube.com/" + id;
                break;
        }

        final String actual_url = url;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (id.equals("null"))
                    Toast.makeText(v.getContext(), "Missing "+socialMedia, Toast.LENGTH_SHORT).show();
                else
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(actual_url)));
            }
        });

    }

    public ArrayList<? extends Parcelable> getmDataSet() {
        return new ArrayList<>(mDataSet);
    }

    public void setmDataSet(ArrayList<? extends Parcelable> mNewDataSet) {
        mDataSet = (List<CongressMemberWrapper>) mNewDataSet;
        notifyDataSetChanged();
    }
}

