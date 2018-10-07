package info.alexanderchen.represent.adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;

import info.alexanderchen.represent.R;
import info.alexanderchen.represent.data.CommitteeWrapper;
import info.alexanderchen.represent.data.CongressMemberWrapper;

public class CommitteeListAdapter extends RecyclerView.Adapter<CommitteeListAdapter.ViewHolder> {

    private ViewGroup parent;
    private List<CommitteeWrapper> mDataSet = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewCommitteeIcon;
        public final TextView textViewCommitteeName;
        public final TextView textViewCommitteeDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewCommitteeIcon = itemView.findViewById(R.id.textViewCommitteeIcon);
            textViewCommitteeName = itemView.findViewById(R.id.textViewCommitteeName);
            textViewCommitteeDescription = itemView.findViewById(R.id.textViewCommitteeDescription);
        }
    }

    @NonNull
    @Override
    public CommitteeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_committee, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommitteeListAdapter.ViewHolder holder, int position) {
        final CommitteeWrapper committeeWrapper = mDataSet.get(position);

        holder.textViewCommitteeName.setText(committeeWrapper.getName());
        holder.textViewCommitteeDescription.setText(committeeWrapper.getTitle()+" | "+committeeWrapper.getSide()+" | "+committeeWrapper.getEndDate()+" | "+committeeWrapper.getCode());
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener{
        void onClick(CongressMemberWrapper congressMemberWrapper);
    }

    public void swapData(List<CommitteeWrapper> mNewDataSet) {
        mDataSet = mNewDataSet;
        notifyDataSetChanged();
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
