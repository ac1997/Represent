package info.alexanderchen.represent.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import info.alexanderchen.represent.R;
import info.alexanderchen.represent.adapter.CommitteeListAdapter;
import info.alexanderchen.represent.data.CommitteeWrapper;
import info.alexanderchen.represent.decoration.InsetDividerItemDecoration;

public class CommitteeTab extends Fragment {

    private List<CommitteeWrapper> committeeWrappers;

    private RecyclerView recyclerViewCommittees;
    private CommitteeListAdapter committeeListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        committeeWrappers = getArguments().getParcelableArrayList("committeeWrappers");
        return inflater.inflate(R.layout.fragment_committee, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.recyclerViewCommittee).setBackgroundColor(Color.WHITE);

        recyclerViewCommittees = view.findViewById(R.id.recyclerViewCommittee);
        committeeListAdapter = new CommitteeListAdapter();
        recyclerViewCommittees.setAdapter(committeeListAdapter);
        recyclerViewCommittees.setLayoutManager(new LinearLayoutManager(view.getContext()));

        committeeListAdapter.swapData(committeeWrappers);
        recyclerViewCommittees.addItemDecoration(new InsetDividerItemDecoration(view.getContext(), R.id.textViewCommitteeName));
        recyclerViewCommittees.setAdapter(committeeListAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
