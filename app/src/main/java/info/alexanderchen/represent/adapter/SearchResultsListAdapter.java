package info.alexanderchen.represent.adapter;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;

import info.alexanderchen.represent.R;
import info.alexanderchen.represent.Typefaces;
import info.alexanderchen.represent.data.CongressMemberWrapper;

public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<CongressMemberWrapper> mDataSet = new ArrayList<>();

    private int mLastAnimatedItemPosition = -1;

    public interface OnItemClickListener{
        void onClick(CongressMemberWrapper congressMemberWrapper);
    }

    private OnItemClickListener mItemsOnClickListener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMemberPicture;
        public final ImageView mMemberParty;
        public final TextView mMemberName;
        public final TextView mMemberDesc;

        public ViewHolder(View view) {
            super(view);
            mMemberPicture = view.findViewById(R.id.member_image);
            mMemberParty = view.findViewById(R.id.member_party);
            mMemberName = view.findViewById(R.id.member_name);
            mMemberDesc = view.findViewById(R.id.member_basic_desc);
        }
    }

    public void swapData(List<CongressMemberWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
    }

    public void setItemsOnClickListener(OnItemClickListener onClickListener){
        this.mItemsOnClickListener = onClickListener;
    }

    @Override
    public SearchResultsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_results_list_item, parent, false);

        Typeface font = Typefaces.get(parent.getContext(), "fa-regular-400.ttf");
        Button button;

        button = view.findViewById(R.id.button_email);
        button.setTypeface(font);
        button = view.findViewById(R.id.button_website);
        button.setTypeface(font);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchResultsListAdapter.ViewHolder holder, final int position) {

        CongressMemberWrapper congressMember = mDataSet.get(position);
        holder.mMemberName.setText(congressMember.getName());
        holder.mMemberDesc.setText(congressMember.getParty()+" | "+congressMember.getRole());

        if(mLastAnimatedItemPosition < position){
            animateItem(holder.itemView);
            mLastAnimatedItemPosition = position;
        }

        if(mItemsOnClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemsOnClickListener.onClick(mDataSet.get(position));
                }
            });
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
}

