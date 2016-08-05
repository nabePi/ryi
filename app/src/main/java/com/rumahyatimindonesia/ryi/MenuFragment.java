package com.rumahyatimindonesia.ryi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MenuFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // COSTUME VAR
    String urlFeed = null;
//    CarouselView carouselView;
//    int[] sampleImages = {R.drawable.carousel1, R.drawable.carousel2, R.drawable.carousel3, R.drawable.carousel4};


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MenuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
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

//        DEFAULT FRAGMENT
//        ----------------
//        return inflater.inflate(R.layout.fragment_menu, container, false);
        View view = inflater.inflate(R.layout.fragment_menu, container, false);


//        carouselView = (CarouselView) view.findViewById(R.id.carouselView);
//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);


        final List<MenuList> rowMenuListItem = getMenuItemList();
        LinearLayoutManager lMenuLayout = new LinearLayoutManager(getActivity());
        RecyclerView rMenuView = (RecyclerView) view.findViewById(R.id.menu_rv);
        rMenuView.setLayoutManager(lMenuLayout);
        MenuAdapter mAdapter = new MenuAdapter(getActivity(), rowMenuListItem);
        rMenuView.setAdapter(mAdapter);

        rMenuView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), rMenuView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MenuList menuList = rowMenuListItem.get(position);
                String title = menuList.getTitle();
//                Toast.makeText(getContext(), menuList.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();

                switch (position){
                    case 0:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Kisah%20Inspiratif?alt=rss";
                        urlFeed = "Kisah%20Inspiratif";
                        break;
                    case 1:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Tafakur?alt=rss";
                        urlFeed = "Tafakur";
                        break;
                    case 2:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Tadabbur?alt=rss";
                        urlFeed = "Tadabbur";
                        break;
                    case 3:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Smart%20Family?alt=rss";
                        urlFeed = "Smart%20Family";
                        break;
                    case 4:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Generator%20Sukses?alt=rss";
                        urlFeed = "Generator%20Sukses";
                        break;
                    case 5:
//                        urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Event?alt=rss";
                        urlFeed = "Event";
                        break;
                }

                TextView TVTitle = (TextView) getActivity().findViewById(R.id.header_title);
                try {
                    TVTitle.setText(title);
                } catch (Exception e){
                    e.printStackTrace();
                }

                FeedFragment feedFragment = new FeedFragment();

                Bundle bundle = new Bundle();
                bundle.putString(FeedFragment.ARG_urlFeed, urlFeed);
                feedFragment.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment, feedFragment).addToBackStack(null).commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

//    ImageListener imageListener = new ImageListener() {
//        @Override
//        public void setImageForPosition(int position, ImageView imageView) {
//            imageView.setImageResource(sampleImages[position]);
//        }
//    };

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private List<MenuList> getMenuItemList(){

        List<MenuList> menuItems = new ArrayList<>();
        menuItems.add(new MenuList("Kisah Inspiratif", R.drawable.kisah_inspiratif));
        menuItems.add(new MenuList("Tafakkur", R.drawable.tafakkur));
        menuItems.add(new MenuList("Tadabbur", R.drawable.tadabbur));
        menuItems.add(new MenuList("Smart Family", R.drawable.family));
        menuItems.add(new MenuList("Generator Sukses", R.drawable.sukses));
        menuItems.add(new MenuList("Agenda", R.drawable.agenda));

        return menuItems;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private MenuFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MenuFragment.ClickListener clickListener) {
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
