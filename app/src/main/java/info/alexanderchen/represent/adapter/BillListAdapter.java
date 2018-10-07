package info.alexanderchen.represent.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;

import info.alexanderchen.represent.R;
import info.alexanderchen.represent.data.BillWrapper;
import info.alexanderchen.represent.data.CongressMemberWrapper;

public class BillListAdapter extends RecyclerView.Adapter<BillListAdapter.ViewHolder> {

    private ViewGroup parent;
    private List<BillWrapper> mDataSet = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textViewBillIcon;
        public final TextView textViewBillName;
        public final TextView textViewBillDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewBillIcon = itemView.findViewById(R.id.textViewBillIcon);
            textViewBillName = itemView.findViewById(R.id.textViewBillName);
            textViewBillDescription = itemView.findViewById(R.id.textViewBillDescription);
        }
    }

    @NonNull
    @Override
    public BillListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.parent = parent;
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_bill, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillListAdapter.ViewHolder holder, int position) {
        final BillWrapper billWrapper = mDataSet.get(position);

        holder.textViewBillName.setText(billWrapper.getBillNumber()+": "+billWrapper.getShortTitle());
        holder.textViewBillDescription.setText(billWrapper.getDate()+" | "+billWrapper.getCommittee());

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public interface OnItemClickListener{
        void onClick(CongressMemberWrapper congressMemberWrapper);
    }

    public void swapData(List<BillWrapper> mNewDataSet) {
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

