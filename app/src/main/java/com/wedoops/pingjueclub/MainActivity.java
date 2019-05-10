package com.wedoops.pingjueclub;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.CustomTypefaceSpan;
import com.wedoops.pingjueclub.helper.LocaleHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView textview_user_rank, textview_user_full_name, textview_username, textview_user_wallet;
    private ImageView imageview_user_rank;
    private Handler mHandler;
    private RoundedImageView imageview_user_profile;
    private ImageButton imagebutton_language;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;

    public static int navItemIndex = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static final String TAG_DASHBOARD = "DASHBOARD";
    private static final String TAG_ACCOUNT = "ACCOUNT";
    private static final String TAG_MY_BOOKING = "MYBOOKING";
    private static final String TAG_TRANSACTION_REPORT = "TRANSACTIONREPORT";
    private static final String TAG_LOGOUT = "LOGOUT";
    public static String CURRENT_TAG = TAG_DASHBOARD;
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();

        setContentView(R.layout.activity_main);

        if (checkAndRequestPermissions()) {
            mHandler = new Handler();

            setupViewByID();
            setupToolbar();
            setupNavigationDrawer();

            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);

        }


    }

    private boolean checkAndRequestPermissions() {
        int networkStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (networkStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();

                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {

                        mHandler = new Handler();

                        setupViewByID();
                        setupToolbar();
                        setupNavigationDrawer();

                        navigationView.getMenu().getItem(0).setChecked(true);
                        navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);

                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    this);
                            builder.setTitle(this.getString(R.string.error_title));
                            builder.setMessage("Internet and Mobile Data Permission required for this app");
                            builder.setCancelable(false);
                            builder.setPositiveButton(this.getString(R.string.Ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
                                            checkAndRequestPermissions();
                                        }
                                    });
                            builder.show();

                        } else {
                            Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
        }

    }

    private void setupViewByID() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF0000"), Color.parseColor("#0000FF"), Color.parseColor("#FFFF00"));

        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        swipeRefreshLayout.setRefreshing(true);
//                        myUpdateOperation();
                        loadHomeFragment();

                    }
                }
        );
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationview);
        drawer = findViewById(R.id.drawer_layout);
        navHeader = navigationView.getHeaderView(0);

        imagebutton_language = navHeader.findViewById(R.id.imagebutton_language);
        imageview_user_rank = navHeader.findViewById(R.id.imageview_user_rank);
        imageview_user_profile = navHeader.findViewById(R.id.imageview_user_profile);

        textview_user_rank = navHeader.findViewById(R.id.textview_user_rank);
        textview_user_full_name = navHeader.findViewById(R.id.textview_user_full_name);
        textview_username = navHeader.findViewById(R.id.textview_username);
        textview_user_wallet = navHeader.findViewById(R.id.textview_user_wallet);

    }

    private void setupToolbar() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.menu_dashboard:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_DASHBOARD;

                        break;
                    case R.id.menu_account:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_ACCOUNT;

                        break;
                    case R.id.menu_my_booking:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MY_BOOKING;

                        break;
                    case R.id.menu_my_transactions_report:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_TRANSACTION_REPORT;

                        break;
                    case R.id.menu_logout:

                        navItemIndex = 4;

                        drawer.closeDrawers();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle(MainActivity.this.getString(R.string.warning_title));
                        builder.setMessage(MainActivity.this.getString(R.string.logout_confirmation));
                        builder.setCancelable(false);
                        builder.setPositiveButton(MainActivity.this.getString(R.string.Ok),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();

                                        UserDetails.deleteAll(UserDetails.class);
                                        MemberDashboardTopBanner.deleteAll(MemberDashboardTopBanner.class);
                                        MemberDashboardTopBanner.deleteAll(MemberDashboardTopBanner.class);

                                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                                Intent.FLAG_ACTIVITY_NEW_TASK);

                                        startActivity(intent);

                                    }
                                });

                        builder.setNegativeButton(MainActivity.this.getString(R.string.cancel),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();

                                    }
                                });
                        builder.show();

                        return false;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state

                if (menuItem.getItemId() != R.id.menu_logout) {
                    if (menuItem.isChecked()) {
                        menuItem.setChecked(false);
                    } else {
                        menuItem.setChecked(true);
                    }
                }
                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setupNavigationDrawer() {
        //Navigation Header

        navigationView.setItemIconTintList(null);

        if (getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us")) {
            imagebutton_language.setImageResource(R.drawable.language_usa);
        } else {
            imagebutton_language.setImageResource(R.drawable.language_china);
        }

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.size() > 0) {

            if (ud.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
                imageview_user_rank.setImageResource(R.drawable.user_level_bronze);
                textview_user_rank.setText(getResources().getString(R.string.userrank_bronze));

            } else if (ud.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
                imageview_user_rank.setImageResource(R.drawable.user_level_gold);
                textview_user_rank.setText(getResources().getString(R.string.userrank_gold));

            } else {
                imageview_user_rank.setImageResource(R.drawable.user_level_platinum);
                textview_user_rank.setText(getResources().getString(R.string.userrank_platinum));
            }

            textview_user_full_name.setText(ud.get(0).getName());
            textview_username.setText(String.format("@%s", ud.get(0).getLoginID().toUpperCase(Locale.getDefault())));


            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String cash_wallet = decimalFormat.format(Double.parseDouble(ud.get(0).getCashWallet()));

            textview_user_wallet.setText(String.format("%s POINTS", cash_wallet));

            Glide.with(this).load(ud.get(0).getProfilePictureImagePath()).placeholder(R.drawable.default_profile).into(imageview_user_profile);

            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
            textview_user_full_name.setTypeface(typeface);

            Typeface typeface_crimson_700 = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-700.ttf");
            textview_username.setTypeface(typeface_crimson_700);
            textview_user_wallet.setTypeface(typeface_crimson_700);
            textview_user_rank.setTypeface(typeface_crimson_700);

            Menu m = navigationView.getMenu();
            for (int i = 0; i < m.size(); i++) {
                MenuItem mi = m.getItem(i);

                SubMenu subMenu = mi.getSubMenu();
                if (subMenu != null && subMenu.size() > 0) {
                    for (int j = 0; j < subMenu.size(); j++) {
                        MenuItem subMenuItem = subMenu.getItem(j);
                        applyFontToMenuItem(subMenuItem);
                    }
                }
                applyFontToMenuItem(mi);
            }
        }
    }

    private void loadHomeFragment() {

        // selecting appropriate nav menu item
        selectNavMenu();

        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.framelayout_fragment_container, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);

        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();

        swipeRefreshLayout.setRefreshing(false);

    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                MemberDashboardActivity dashboardFragment = new MemberDashboardActivity();
                return dashboardFragment;
            case 1:
                EditProfileActivity accountFragment = new EditProfileActivity();
                return accountFragment;
            case 2:
                MyBookingActivity bookingFragment = new MyBookingActivity();
                return bookingFragment;
            case 3:
                MyTransactionsReport transactionsReportFragment = new MyTransactionsReport();
                return transactionsReportFragment;

            default:
                return new MemberDashboardActivity();
        }
    }

    private void selectNavMenu() {
        if (navItemIndex != 4) {
            navigationView.getMenu().getItem(navItemIndex).setChecked(true);
        } else {
            navigationView.getMenu().getItem(navItemIndex).setChecked(false);
        }
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    public void button_change_language(View v) {

        String currentLanguage = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        if (currentLanguage.equals("en_us")) {
            saveLanguage("zh");
        } else {
            saveLanguage("en_us");

        }

    }


    private void loadLanguage() {
        String lang = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    private void saveLanguage(String lang) {

        new ApplicationClass().writeIntoSharedPreferences(this, KEY_LANG, lang);

        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        finish();

        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
        } else {
            if (navItemIndex != 0) {

                navItemIndex = 0;
                loadHomeFragment();

            } else {

                if (doubleBackToExitPressedOnce) {
                    finish();
                } else {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce = false;
                        }
                    }, 2000);
                }


            }

        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent objEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }
        return super.onKeyUp(keyCode, objEvent);
    }
}
