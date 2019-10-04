package com.wedoops.pingjueclub;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
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
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orm.StringUtil;
import com.wedoops.pingjueclub.database.MemberDashboardTopBanner;
import com.wedoops.pingjueclub.database.UserDetails;
import com.wedoops.pingjueclub.helper.ApplicationClass;
import com.wedoops.pingjueclub.helper.CONSTANTS_VALUE;
import com.wedoops.pingjueclub.helper.CustomTypefaceSpan;
import com.wedoops.pingjueclub.helper.DisplayAlertDialog;
import com.wedoops.pingjueclub.helper.IImagePickerLister;
import com.wedoops.pingjueclub.helper.ImagePickerEnum;
import com.wedoops.pingjueclub.webservices.Api_Constants;
import com.wedoops.pingjueclub.webservices.CallWebServices;
import com.yalantis.ucrop.UCrop;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

import static com.wedoops.pingjueclub.helper.CONSTANTS_VALUE.PICK_IMAGE_GALLERY_REQUEST_CODE;


public class MainActivity extends AppCompatActivity implements IImagePickerLister {

    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private LinearLayout navFooter, homeBottomNav, recordBottomNav;
    private FloatingActionButton floatingActionButton;
    private TextView textview_user_rank, textview_user_nickname, textview_user_wallet, toolbar_title ;
    private ImageView imageview_user_rank, toolbar_logo, toolbar_camera;
    private Handler mHandler;
    private RoundedImageView imageview_user_profile;
    private ImageButton imagebutton_language;
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;
    private String currentPhotoPath = "";
    private static ACProgressFlower progress;
    private CoordinatorLayout toolbar_heart;

    public static int navItemIndex = 0;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static final String TAG_DASHBOARD = "DASHBOARD";
    private static final String TAG_ACCOUNT = "ACCOUNT";
    private static final String TAG_MY_BOOKING = "MYBOOKING";
    private static final String TAG_TRANSACTION_REPORT = "TRANSACTIONREPORT";
    private static final String TAG_SERVICE = "SERVICE";
    private static final String TAG_GUIDE = "GUIDE";
    private static final String TAG_BENEFIT = "BENEFIT";
    private static final String TAG_TERMNCOND = "TERMCONDITIONS";
    private static final String TAG_LOGOUT = "LOGOUT";
    public static String CURRENT_TAG = TAG_DASHBOARD;
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        loadLanguage();

        setContentView(R.layout.activity_main);

        if (checkAndRequestPermissions()) {
            mHandler = new Handler();

            progress = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text(this.getResources().getString(R.string.loading_please_wait))
                    .petalThickness(5)
                    .textColor(Color.WHITE)
                    .textSize(30)
                    .fadeColor(Color.DKGRAY).build();

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
            case CONSTANTS_VALUE.CAMERA_STORAGE_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    new DisplayAlertDialog().displayImageSelectDialog(this, this);
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.storage_access_required, Toast.LENGTH_SHORT).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_access_required, Toast.LENGTH_SHORT).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.camera_and_storage_access_required, Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case CONSTANTS_VALUE.ONLY_CAMERA_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    new DisplayAlertDialog().displayImageSelectDialog(this, this);

                else {
                    Toast.makeText(this, R.string.camera_access_required, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CONSTANTS_VALUE.ONLY_STORAGE_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    new DisplayAlertDialog().displayImageSelectDialog(this, this);
                else {
                    Toast.makeText(this, R.string.storage_access_required, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    }

    private void setupViewByID() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setDistanceToTriggerSync(400);
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
        navHeader = findViewById(R.id.headerrr);
        navFooter = findViewById(R.id.navFooter);
        homeBottomNav = findViewById(R.id.bottom_nav_home);
        recordBottomNav=findViewById(R.id.bottom_nav_record);
        floatingActionButton=findViewById(R.id.bottom_nav_transaction);

        imagebutton_language = navHeader.findViewById(R.id.imagebutton_language);
        imageview_user_rank = navHeader.findViewById(R.id.imageview_user_rank);
        imageview_user_profile = navHeader.findViewById(R.id.imageview_user_profile);

        textview_user_rank = navHeader.findViewById(R.id.textview_user_rank);
        textview_user_nickname = navHeader.findViewById(R.id.textview_user_nickname);
        textview_user_wallet = navHeader.findViewById(R.id.textview_user_wallet);

        toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_logo=findViewById(R.id.toolbar_logo);
        toolbar_heart=findViewById(R.id.toolbar_heart);
        toolbar_camera=findViewById(R.id.toolbar_camera);


        navFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawers();
                }

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

            }
        });

        homeBottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(0).setChecked(true);
                navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);
            }
        });

        recordBottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(3).setChecked(true);
                navigationView.getMenu().performIdentifierAction(R.id.menu_my_transactions_report, 0);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigationView.getMenu().getItem(2).setChecked(true);
                navigationView.getMenu().performIdentifierAction(R.id.menu_my_booking, 0);
            }
        });

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
                    case R.id.menu_services:

                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SERVICE;
                        break;

                    case R.id.menu_guide:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_GUIDE;

                        break;

                    case R.id.menu_benefit:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_BENEFIT;

                        break;

                    case R.id.menu_termNCond:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_TERMNCOND;

                        break;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state

//                if (menuItem.getItemId() != R.id.menu_logout) {
//                    if (menuItem.isChecked()) {
//                        menuItem.setChecked(false);
//                    } else {
//                        menuItem.setChecked(true);
//                    }
//                }
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

        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textview_sidebar));

        //Setting the actionbarToggle to drawer layout
        //drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    private void setupNavigationDrawer() {
        //Navigation Header

        if (getResources().getConfiguration().locale.toString().toLowerCase().equals("en_us")) {
            imagebutton_language.setImageResource(R.drawable.language_usa);
        } else {
            imagebutton_language.setImageResource(R.drawable.language_china);
        }

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.size() > 0) {

            if (ud.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
                imageview_user_rank.setImageResource(R.drawable.bronze_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_bronze));

            } else if (ud.get(0).getUserLevelCode().equals(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
                imageview_user_rank.setImageResource(R.drawable.gold_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_gold));

            } else {
                imageview_user_rank.setImageResource(R.drawable.platinum_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_platinum));
            }

            textview_user_nickname.setText(ud.get(0).getNickName());


            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String cash_wallet = decimalFormat.format(Double.parseDouble(ud.get(0).getCashWallet()));

            textview_user_wallet.setText(String.format("%s POINTS", cash_wallet));

            Glide.with(this).load(ud.get(0).getProfilePictureImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).into(imageview_user_profile);

            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
            textview_user_nickname.setTypeface(typeface);

            Typeface typeface_crimson_700 = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-700.ttf");
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

    public void button_change_profile_image(View v) {
        Toast.makeText(this, "SomethingSomething", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (new ApplicationClass().checkSelfPermissions(this)) {
                new DisplayAlertDialog().displayImageSelectDialog(this, this);

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
        this.callQuickProfileWebService();


    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.VISIBLE);
                toolbar_heart.setVisibility(View.VISIBLE);
                toolbar_camera.setVisibility(View.GONE);
                MemberDashboardActivity dashboardFragment = new MemberDashboardActivity();
                return dashboardFragment;
            case 1:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("Account");
                EditProfileActivity accountFragment = new EditProfileActivity();
                return accountFragment;
            case 2:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("My Booking");
                MyBookingActivity bookingFragment = new MyBookingActivity();
                return bookingFragment;
            case 3:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("My Transaction Report");
                MyTransactionsReport transactionsReportFragment = new MyTransactionsReport();
                return transactionsReportFragment;
            case 4:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("Services");
                ServicesFragment servicesFragment = new ServicesFragment();
                return servicesFragment;
            case 5:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("Guide");
                GuideFragment guideFragment = new GuideFragment();
                return guideFragment;
            case 6:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("Benefit");
                BenefitFragment benefitFragment = new BenefitFragment();
                return benefitFragment;
            case 7:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_heart.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText("Term & Conditions");
                TermNCondFragment termNCondFragment = new TermNCondFragment();
                return termNCondFragment;

            default:
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.VISIBLE);
                toolbar_heart.setVisibility(View.VISIBLE);
                toolbar_camera.setVisibility(View.GONE);
                return new MemberDashboardActivity();
        }
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //mNewTitle.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER),0,mNewTitle.length(),0);
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
    protected void onResume() {
        super.onResume();
        if (progress != null) {
            new ApplicationClass().closeProgressDialog(progress);

        }
    }

    private void callChangeProfilePictureWebService(String base64_string) {

        new ApplicationClass().showProgressDialog(progress);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        String table_name = UserDetails.getTableName(UserDetails.class);
        String loginid_field = StringUtil.toSQLName("LoginID");

        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

        Bundle b = new Bundle();
        b.putString("access_token", ud_list.get(0).getAccessToken());
        b.putString("ProfilePictureBase64", base64_string);
        b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_CHANGE_PROFILE_PICTURE);

        new CallWebServices(Api_Constants.API_MEMBER_CHANGE_PROFILE_PICTURE, MainActivity.this, true).execute(b);

    }

    private void callQuickProfileWebService() {

        new ApplicationClass().showProgressDialog(progress);

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.size() > 0) {
            String table_name = UserDetails.getTableName(UserDetails.class);
            String loginid_field = StringUtil.toSQLName("LoginID");

            List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud.get(0).getLoginID());

            Bundle b = new Bundle();
            b.putString("access_token", ud_list.get(0).getAccessToken());
            b.putInt(Api_Constants.COMMAND, Api_Constants.API_MEMBER_QUICK_PROFILE);

            new CallWebServices(Api_Constants.API_MEMBER_QUICK_PROFILE, MainActivity.this, true).execute(b);
        }

    }

    public void processWSData(JSONObject returnedObject, int command) {
        new ApplicationClass().closeProgressDialog(progress);

        if (command == Api_Constants.API_MEMBER_CHANGE_PROFILE_PICTURE) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {
                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");
                        JSONObject success_object = response_object.getJSONObject("SuccessMessage");
                        JSONObject profile_object = response_object.getJSONObject("UserQuickProfile");

                        String Srno = String.valueOf(profile_object.getInt("Srno"));
                        String LoginID = profile_object.getString("LoginID");
                        String Name = profile_object.getString("Name");
                        String DOB = profile_object.getString("DOB");
                        String Email = profile_object.getString("Email");
                        String Phone = profile_object.getString("Phone");
                        String CountryCode = profile_object.getString("CountryCode");
                        String StateCode = profile_object.getString("StateCode");
                        String Address = profile_object.getString("Address");
                        String Gender = profile_object.getString("Gender");
                        String ProfilePictureImagePath = profile_object.getString("ProfilePictureImagePath");
                        String UserLevelCode = profile_object.getString("UserLevelCode");
                        String JoinedDate = profile_object.getString("JoinedDate");
                        String CashWallet = String.valueOf(profile_object.getString("CashWallet"));

                        List<UserDetails> ud_listall = UserDetails.listAll(UserDetails.class);

                        String table_name = UserDetails.getTableName(UserDetails.class);
                        String loginid_field = StringUtil.toSQLName("LoginID");

                        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud_listall.get(0).getLoginID());

                        UserDetails ud = UserDetails.find(UserDetails.class, loginid_field + " = ?", ud_listall.get(0).getLoginID()).get(0);

                        ud.setAccessToken(ud_list.get(0).getAccessToken());
                        ud.setRefreshToken(ud_list.get(0).getRefreshToken());
                        ud.setSrno(Srno);
                        ud.setLoginID(LoginID);
                        ud.setName(Name);
                        ud.setDOB(DOB);
                        ud.setEmail(Email);
                        ud.setPhone(Phone);
                        ud.setCountryCode(CountryCode);
                        ud.setStateCode(StateCode);
                        ud.setAddress(Address);
                        ud.setGender(Gender);
                        ud.setProfilePictureImagePath(ProfilePictureImagePath);
                        ud.setUserLevelCode(UserLevelCode);
                        ud.setJoinedDate(JoinedDate);
                        ud.setCashWallet(CashWallet);
                        ud.save();

                        new DisplayAlertDialog().displayAlertDialogSuccess(success_object.getInt("Code"), MainActivity.this);

                        setupNavigationDrawer();

//                        navItemIndex = 0;
//                        loadHomeFragment();

//                        displayResult();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MainActivity.this);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }
        } else if (command == Api_Constants.API_MEMBER_QUICK_PROFILE) {
            boolean isSuccess = false;
            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {
                    if (returnedObject.getInt("StatusCode") == 200) {
                        JSONObject response_object = returnedObject.getJSONObject("ResponseData");

                        String Srno = String.valueOf(response_object.getInt("Srno"));
                        String LoginID = response_object.getString("LoginID");
                        String Name = response_object.getString("Name");
                        String NickName = response_object.getString("NickName");
                        String ProfilePictureImagePath = response_object.getString("ProfilePictureImagePath");
                        String UserLevelCode = response_object.getString("UserLevelCode");
                        String JoinedDate = response_object.getString("JoinedDate");
                        String CashWallet = String.valueOf(response_object.getString("CashWallet"));

                        List<UserDetails> ud_listall = UserDetails.listAll(UserDetails.class);

                        String table_name = UserDetails.getTableName(UserDetails.class);
                        String loginid_field = StringUtil.toSQLName("LoginID");

                        List<UserDetails> ud_list = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name + " where " + loginid_field + " = ?", ud_listall.get(0).getLoginID());

                        UserDetails ud = UserDetails.find(UserDetails.class, loginid_field + " = ?", ud_listall.get(0).getLoginID()).get(0);

                        ud.setAccessToken(ud_list.get(0).getAccessToken());
                        ud.setRefreshToken(ud_list.get(0).getRefreshToken());
                        ud.setSrno(Srno);
                        ud.setLoginID(LoginID);
                        ud.setName(Name);
                        ud.setNickName(NickName);
                        ud.setProfilePictureImagePath(ProfilePictureImagePath);
                        ud.setUserLevelCode(UserLevelCode);
                        ud.setJoinedDate(JoinedDate);
                        ud.setCashWallet(CashWallet);
                        ud.save();

                        setupNavigationDrawer();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MainActivity.this);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONSTANTS_VALUE.CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            Uri uri = Uri.parse(currentPhotoPath);
            openCropActivity(uri, uri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                showImage(uri);
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                File file = getImageFile();
                Uri destinationUri = Uri.fromFile(file);
                openCropActivity(sourceUri, destinationUri);
            } catch (Exception e) {
                Log.e("Error Image", e.toString());
            }
        }
    }

    private void showImage(Uri imageUri) {
        try {
            File file;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                file = com.wedoops.pingjueclub.helper.FileUtils.getFile(this, imageUri);
            } else {
                file = new File(currentPhotoPath);
            }

            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//            imageView.setImageBitmap(bitmap);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            callChangeProfilePictureWebService(encoded);

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private void openCamera() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file;
        try {
            file = getImageFile(); // 1
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
        else
            uri = Uri.fromFile(file); // 3
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
        startActivityForResult(pictureIntent, CONSTANTS_VALUE.CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private File getImageFile() throws IOException {
        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");

        System.out.println(storageDir.getAbsolutePath());
        if (storageDir.exists()) {
            System.out.println("File exists");
        } else {
            System.out.println("File not exists");
            storageDir.mkdirs();
        }
        File file = File.createTempFile(
                imageFileName, ".jpg", storageDir
        );
        currentPhotoPath = "file:" + file.getAbsolutePath();
        return file;
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setDimmedLayerColor(Color.parseColor("#50ffffff"));
        options.setHideBottomControls(true);
        options.setShowCropGrid(false);
        options.setCropFrameStrokeWidth(5);
        options.setCropFrameColor(Color.WHITE);
        options.setShowCropFrame(false);
        UCrop.of(sourceUri, destinationUri).withOptions(options)
                .withAspectRatio(1, 1)
                .withMaxResultSize(300, 300)
                .start(this);
    }
}
