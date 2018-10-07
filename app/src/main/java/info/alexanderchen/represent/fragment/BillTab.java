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
import info.alexanderchen.represent.adapter.BillListAdapter;
import info.alexanderchen.represent.adapter.CommitteeListAdapter;
import info.alexanderchen.represent.data.BillWrapper;
import info.alexanderchen.represent.data.CommitteeWrapper;
import info.alexanderchen.represent.decoration.InsetDividerItemDecoration;

public class BillTab extends Fragment {

    private List<BillWrapper> billWrappers;

    private RecyclerView recyclerViewBill;
    private BillListAdapter billListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assert getArguments() != null;
        billWrappers = getArguments().getParcelableArrayList("billWrappers");
        return inflater.inflate(R.layout.fragment_bill, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.recyclerViewBill).setBackgroundColor(Color.WHITE);

        recyclerViewBill = view.findViewById(R.id.recyclerViewBill);
        billListAdapter = new BillListAdapter();
        recyclerViewBill.setAdapter(billListAdapter);
        recyclerViewBill.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewBill.addItemDecoration(new InsetDividerItemDecoration(view.getContext(), R.id.textViewBillName));

        billListAdapter.swapData(billWrappers);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
