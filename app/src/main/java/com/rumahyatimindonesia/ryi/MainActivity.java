package com.rumahyatimindonesia.ryi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MenuFragment.OnFragmentInteractionListener, ReaderFragment.OnFragmentInteractionListener,
        FeedFragment.OnFragmentInteractionListener, FeedReaderFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        TOOLBAR
//        -------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if(savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass= null;
            fragmentClass = MenuFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            }catch (Exception e) {
                e.printStackTrace();
            }
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment, fragment).commit();

        }

//        CHECK ONLINE OFFLINE
//        --------------------
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // ONLINE
        } else {
            Toast.makeText(this, "OFFLINE MODE !", Toast.LENGTH_LONG).show();
        }


//        WEBVIEW
//        -------
//        WebView myWebView = (WebView) findViewById(R.id.webview);
//        myWebView.loadUrl("file:///android_asset/pages/tentang_kita.html");
//        WebSettings webSettings = myWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);

//        CARDVIEW MENU LIST
//        ------------------
//        List<MenuList> rowMenuListItem = getMenuItemList();
//        LinearLayoutManager lMenuLayout = new LinearLayoutManager(MainActivity.this);
//        RecyclerView rMenuView = (RecyclerView)findViewById(R.id.menu_rv);
//        rMenuView.setLayoutManager(lMenuLayout);
//        MenuAdapter mAdapter = new MenuAdapter(MainActivity.this, rowMenuListItem);
//        rMenuView.setAdapter(mAdapter);

//        CARDVIEW FEED LIST
//        ------------------
//        List<FeedList> rowFeedListItem = getFeedItemList();
//        LinearLayoutManager lFeedLayout = new LinearLayoutManager(MainActivity.this);
//        RecyclerView rFeedView = (RecyclerView)findViewById(R.id.feed_rv);
//        rFeedView.setHasFixedSize(true);
//        rFeedView.setLayoutManager(lFeedLayout);
//        rFeedView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        rFeedView.setItemAnimator(new DefaultItemAnimator());
//        FeedAdapter fAdapter = new FeedAdapter(MainActivity.this, rowFeedListItem);
//        rFeedView.setAdapter(fAdapter);

//        FLOATING ACTION BUTTON
//        ----------------------
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        DRAWER LAYOUT
//        -------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

//        NAVIGATION VIEW
//        ---------------
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    OPTION MENU
//    -----------
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        String title = null;
        String url = null;
        TextView TVTitle = (TextView) findViewById(R.id.header_title);
        Fragment fragment = null;

        Bundle bundle = new Bundle();

        if (id == R.id.nav_Beranda) {
            title = "Rumah Yatim Indonesia";
            fragment = new MenuFragment();
        }  else if (id == R.id.nav_Program) {
            title = "Program";
//            String urlFeed = "http://www.rumahyatimindonesia.com/feeds/posts/default/-/Program?alt=rss";
            String urlFeed = "Program";
            fragment = new FeedFragment();
            bundle.putString(FeedFragment.ARG_urlFeed, urlFeed);
        } else if (id == R.id.nav_TentangKita) {
            title = "Tentang Kita";
            url = "file:///android_asset/pages/tentang_kita.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        } else if (id == R.id.nav_Sejarah) {
            title = "Sejarah";
            url = "file:///android_asset/pages/sejarah.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        } else if (id == R.id.nav_Manajemen) {
            title = "Manajemen";
            url = "file:///android_asset/pages/manajemen.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        } else if (id == R.id.nav_VisiMisi) {
            title = "Visi & Misi";
            url = "file:///android_asset/pages/visi_misi.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        } else if (id == R.id.nav_SaudaraKita) {
            title = "Saudara Kita";
            url = "file:///android_asset/pages/saudara_kita.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        } else if (id == R.id.nav_Donasi) {
            title = "Donasi";
            url = "file:///android_asset/pages/donasi.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        }else if (id == R.id.nav_Kontak) {
            title = "Kontak";
            url = "file:///android_asset/pages/kontak.html";
            fragment = new ReaderFragment();
            bundle.putString(ReaderFragment.ARG_URL, url);
        }

        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();

        TVTitle.setText(title);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
