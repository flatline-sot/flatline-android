package nz.flatline.flatline;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoticesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoticesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticesFragment extends HomepageFragment {

    private final List<Notice> MOCK_DATA = new ArrayList<Notice>(){{
        add(new Notice("Example Title", "Look at all the different text you can stick in a notice"));
        add(new Notice("The Truth", "Team Flatline is awesome!"));
        add(new Notice("Oops", "The dishwasher broke this morning :("));
        add(new Notice("Don't Forget", "Team presentation this sunday at Powershop. Don't panic!!!"));
    }};

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NoticesFragment.
     */
    public static Fragment newInstance() {
        HomepageFragment fragment = new NoticesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_notices, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.notices_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new NoticesAdapter(MOCK_DATA);
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    public class Notice{
        protected String title;
        protected String noticeBody;

        public Notice(String title, String noticeBody) {
            this.title = title;
            this.noticeBody = noticeBody;

        }
    }

    public class NoticesAdapter extends RecyclerView.Adapter<NoticesAdapter.NoticeViewHolder> {
        private List<Notice> noticeList;

        public class NoticeViewHolder extends RecyclerView.ViewHolder {

            protected TextView titleTextView;
            protected TextView noticeBodyTextView;

            public NoticeViewHolder(View v) {
                super(v);
                titleTextView =  (TextView) v.findViewById(R.id.title);
                noticeBodyTextView = (TextView)  v.findViewById(R.id.notice_body);

            }
        }

        public NoticesAdapter(List<Notice> notices) {
            noticeList = notices;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public NoticeViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
            // create a new view
            View NoticeCardView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notice_card, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new NoticeViewHolder(NoticeCardView);
        }

        @Override
        public void onBindViewHolder(NoticeViewHolder NoticeViewHolder, int position) {

            Notice notice = noticeList.get(position);

            NoticeViewHolder.titleTextView.setText(notice.title);
            NoticeViewHolder.noticeBodyTextView.setText(notice.noticeBody);

        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return noticeList.size();
        }
    }

}
