package com.rumahyatimindonesia.ryi;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.github.ybq.android.spinkit.style.DoubleBounce;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FeedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FeedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // CUSTOME VAR
    final static String ARG_urlFeed = null;
    String urlFeed;
    RecyclerView rFeedView;
    ProgressBar progressBar;
    List<FeedList> listRssFeed = new ArrayList<FeedList>();
    List<List<String>> originalRssFeed = new ArrayList<>();


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FeedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_feed, container, false);
//        final List<FeedList> rowFeedListItem = getFeedItemList();
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        LinearLayoutManager lFeedLayout = new LinearLayoutManager(getActivity());
        rFeedView = (RecyclerView) view.findViewById(R.id.feed_rv);
        rFeedView.setHasFixedSize(true);
        rFeedView.setLayoutManager(lFeedLayout);
        rFeedView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        rFeedView.setItemAnimator(new DefaultItemAnimator());


        new GetRYIRssFeedTask().execute();

        rFeedView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rFeedView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                FeedList feedList = listRssFeed.get(position);
//                String title = feedList.getTitle();
//                Toast.makeText(getContext(), feedList.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                String orgRssFeedDate = originalRssFeed.get(position).get(0);
                String orgRssFeedTitle = originalRssFeed.get(position).get(1);
                String orgRssFeedDescription = removeStuff( originalRssFeed.get(position).get(2)).toString();
                String htmlRssFeed = "";
                htmlRssFeed += "<html><head><title>Feed Reader</title>";
                htmlRssFeed += "<style>" +
                        "html,body{padding:0;margin:0;}" +
                        ".top{background-color:#4caf50;margin:0;color:#fff;padding:5px 10px;}" +
                        "p.date{font-size:11px;}" +
                        "h3{margin:0;}" +
                        "img{max-width:100% !important;height:auto;display:block;margin: 0 auto;}" +
                        "</style>";
                htmlRssFeed += "</head><body>";
                htmlRssFeed += "<div class=\"top\">";
                htmlRssFeed += "<p class=\"date\">"+fixDate(orgRssFeedDate)+"</p>";
                htmlRssFeed += "<h3>"+orgRssFeedTitle+"</h3>";
                htmlRssFeed += "</div>";
                htmlRssFeed += "<div style=\"padding:5px 10px;\">";
                htmlRssFeed += orgRssFeedDescription;
                htmlRssFeed += "</div>";
                htmlRssFeed += "</body></html>";

//                Log.d("HTML : ", htmlRssFeed);

                FeedReaderFragment feedReaderFragment = new FeedReaderFragment();
                Bundle bundle = new Bundle();
                bundle.putString(FeedReaderFragment.ARG_HTML, htmlRssFeed);
                feedReaderFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, feedReaderFragment).addToBackStack(null).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<FeedList> getFeedItemList(){

        List<FeedList> feedItems = new ArrayList<>();
        feedItems.add(new FeedList("April 24, 2016", "Lima Menit Bersama Bidadari","foo"));
        feedItems.add(new FeedList("April 15, 2016","Harga Semangkuk Bakmi", "foo"));

        return feedItems;
    }

    private String getRYIRssFeed() throws IOException {
        InputStream inputStream = null;
        String rssFeed = null;

        Bundle bundle = getArguments();
        try {
            urlFeed = bundle.getString(ARG_urlFeed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("urlFeed : ",urlFeed);

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                String newUrlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/"+urlFeed+"?alt=rss";
                URL url = new URL(newUrlFeed);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setReadTimeout(1000);
                httpURLConnection.setConnectTimeout(1500);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                int responseCode = httpURLConnection.getResponseCode();
                Log.d("DEBUG_TAG", "The response is: " + responseCode);

                inputStream = httpURLConnection.getInputStream();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[2048];
                for (int count; (count = inputStream.read(buffer)) != -1; ) {
                    byteArrayOutputStream.write(buffer, 0, count);
                }
                byte[] response = byteArrayOutputStream.toByteArray();

                rssFeed = new String(response, "UTF-8");

                writeXMLOffline(urlFeed,rssFeed);

            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        } else {
            Log.d("OFFLINE MODE", "OFFLINE MODE TRUE");
            Log.d("URL FEED = ", urlFeed);
            rssFeed = readXMLOffline(urlFeed);
            Log.d("RSS FEED = ", rssFeed);
        }

        return rssFeed;
    }

    private class GetRYIRssFeedTask extends AsyncTask<Void, Void, List<List<String>>> {

        @Override
        protected List<List<String>> doInBackground(Void... voids) {
            List<List<String>> result = null;
            try {
                String feed = getRYIRssFeed();
                result = parse(feed);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        private List<List<String>> parse(String rssFeed) throws XmlPullParserException, IOException {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(rssFeed));
            xpp.nextTag();
            return readRss(xpp);
        }

        private List<List<String>> readRss(XmlPullParser parser)
                throws XmlPullParserException, IOException {
            List<List<String>> items = new ArrayList<>();
            parser.require(XmlPullParser.START_TAG, null, "rss");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("channel")) {
                    items.addAll(readChannel(parser));
                } else {
                    skip(parser);
                }
            }
            return items;
        }

        private List<List<String>> readChannel(XmlPullParser parser)
                throws IOException, XmlPullParserException {
            List<List<String>> items = new ArrayList<>();
            parser.require(XmlPullParser.START_TAG, null, "channel");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")) {
                    items.addAll(readItem(parser));
                } else {
                    skip(parser);
                }
            }
            return items;
        }

        private List<List<String>> readItem(XmlPullParser parser) throws XmlPullParserException, IOException {
            List<List<String>> result = new ArrayList<>();
            List<String> valResult = new ArrayList<>();
            parser.require(XmlPullParser.START_TAG, null, "item");
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {
                    valResult.add(readTitle(parser));
                } else if (name.equals("pubDate")) {
                    valResult.add(readPubDate(parser));
                } else if (name.equals("description")) {
                    valResult.add(readDescription(parser));
                } else {
                    skip(parser);
                }
            }
            result.add(valResult);
            return result;
        }

        private String readPubDate (XmlPullParser parser)
                throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "pubDate");
            String pubDate = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "pubDate");
            return pubDate;
        }

        private String readTitle(XmlPullParser parser)
                throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "title");
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "title");
            return title;
        }

        private String readDescription(XmlPullParser parser)
                throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, "description");
            String description = readText(parser);
            parser.require(XmlPullParser.END_TAG, null, "description");
            return description;
        }

        private String readText(XmlPullParser parser)
                throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }

        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

        @Override
        protected void onPostExecute(List<List<String>> rssFeed) {
            if (rssFeed != null) {
                Log.d("rssFeed : ",rssFeed.get(0).get(1));

                originalRssFeed = rssFeed;

                listRssFeed = new ArrayList<FeedList>();

                for (int i = 0; i < rssFeed.size(); i++) {
                    String date =  fixDate(rssFeed.get(i).get(0));
                    String title = rssFeed.get(i).get(1);
                    String image = null;
                    try {
                        image = getSourceImage(rssFeed.get(i).get(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    listRssFeed.add(new FeedList(date, title, image));
                }

                progressBar.setVisibility(View.GONE);
                FeedAdapter fAdapter = new FeedAdapter(getActivity(), listRssFeed);
                rFeedView.setAdapter(fAdapter);
            }

        }
    }

    public String getSourceImage(String html) {
        Document document = Jsoup.parse(html);
        String srcImage = document.select("img").first().attr("src");
        return srcImage;
    }

    public Document removeStuff(String html) {
        Document document = Jsoup.parse(html);
        try {
            document.select("a").first().removeAttr("style");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return document;
    }

    public void writeXMLOffline(String name, String value) {
        try {
            File file = Environment.getExternalStorageDirectory();
            File filename = new File(file, name);
            FileOutputStream fos = new FileOutputStream(filename);
            fos.write(value.getBytes());
            fos.close();
            Log.d("WRITE OFFLINE RSS : ", "TRUE");
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    public String readXMLOffline(String name)  {
        String fileRssFeed = null;

        File file = Environment.getExternalStorageDirectory();
        File filename = new File(file, name);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        fileRssFeed = text.toString();
        return fileRssFeed;
    }

    public String fixDate(String date){
        SimpleDateFormat inFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        SimpleDateFormat outFormatter = new SimpleDateFormat("d MMM yyyy HH:mm:ss");
        String fixedDate = date;
        try {
            fixedDate = outFormatter.format(inFormatter.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return fixedDate;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private FeedFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final FeedFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
