package nz.flatline.flatline;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import nz.flatline.flatline.api.model.Bill;
import nz.flatline.flatline.api.model.BillUI;
import nz.flatline.flatline.tools.AppConstants;
import nz.flatline.flatline.tools.RecyclerItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BillsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BillsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BillsFragment extends HomepageFragment implements BillUI{


    private final List<BillModel> MOCK_DATA = new ArrayList<BillModel>(){{
        add(new BillModel("$24.39", "Powershop", "$97.58 total due","7/08/15", new ArrayList<Drawable>()));
        add(new BillModel("$27.25", "Vodafone", "$109.00 total due","15/08/15", new ArrayList<Drawable>()));
    }};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button powershopButton;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BillsFragment.
     */
    public static HomepageFragment newInstance() {
        HomepageFragment fragment = new BillsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BillsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_bills, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.bills_recycler_view);
        powershopButton = (Button) v.findViewById(R.id.connect_with_powershop);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        boolean connectedToPowershop = prefs.getBoolean(AppConstants.POWERSHOP_CONNECTED, false);

        if (!connectedToPowershop) { //flat not connected to powershop
            mRecyclerView.setVisibility(View.GONE);
            powershopButton.setVisibility(View.VISIBLE);
            powershopButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomepageActivity homepageActivity =(HomepageActivity)getActivity();
                    homepageActivity.powershopSignInService.requestAuthorizationURL();
                }
            });
        }

        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.i("RecyclerView", "item clicked, postion:" + position);
                        buildAlert(view, position);
                    }
                })
        );

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return v;
    }

    private void buildAlert(View view, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_title);
        // Add the buttons
        builder.setPositiveButton(R.string.paid, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Mark as paid
            }
        });
        builder.setNegativeButton(R.string.not_paid, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // leave as not paid
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onBillsReceived(List<Bill> bills) {
        mAdapter = new BillsAdapter(bills);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        powershopButton.setVisibility(View.GONE);

    }

    private class BillModel {
        protected String youOwe;
        protected String company;
        protected String totalDue;
        protected String dateDue;
        protected List<Drawable> flatmatesBilled;

        public BillModel(String youOwe, String company, String totalDue, String dateDue, List<Drawable> flatmatesBilled) {
            this.company = company;
            this.dateDue = dateDue;
            this.flatmatesBilled = flatmatesBilled;
            this.totalDue = totalDue;
            this.youOwe = youOwe;
        }
    }

    public class BillsAdapter extends RecyclerView.Adapter<BillsAdapter.BillViewHolder> {
        private List<Bill> billsList;

        public class BillViewHolder extends RecyclerView.ViewHolder {

            protected TextView youOweTextView;
            protected TextView companyTextView;
            protected TextView totalDueTextView;
            protected TextView dateDue;

            public BillViewHolder(View v) {
                super(v);
                youOweTextView =  (TextView) v.findViewById(R.id.you_owe);
                companyTextView = (TextView)  v.findViewById(R.id.company);
                totalDueTextView = (TextView)  v.findViewById(R.id.total_due);
                dateDue = (TextView) v.findViewById(R.id.date_due);
            }
        }

        public BillsAdapter(List<Bill> bills) {
            billsList = bills;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public BillViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
            // create a new view
            View billCardView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bill_card, parent, false);

            // set the view's size, margins, paddings and layout parameters

            return new BillViewHolder(billCardView);
        }

        @Override
        public void onBindViewHolder(BillViewHolder billViewHolder, int position) {

            Bill bill = billsList.get(position);

            billViewHolder.youOweTextView.setText(String.valueOf(bill.costPerUser));
            billViewHolder.companyTextView.setText("Powershop");
            billViewHolder.totalDueTextView.setText(String.valueOf(bill.cost));
            billViewHolder.dateDue.setText(bill.getReadableEndDate());

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return billsList.size();
        }
    }
}
