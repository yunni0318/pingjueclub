package com.wedoops.platinumnobleclub;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.BuildConfig;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.orm.StringUtil;
import com.wedoops.platinumnobleclub.Dialog.QRcodeDialog;
import com.wedoops.platinumnobleclub.database.MemberDashboardTopBanner;
import com.wedoops.platinumnobleclub.database.MemberIDEncryted;
import com.wedoops.platinumnobleclub.database.UserDetails;
import com.wedoops.platinumnobleclub.fragment.BenefitFragment;
import com.wedoops.platinumnobleclub.fragment.ButlerServiceFragment;
import com.wedoops.platinumnobleclub.fragment.EditProfileFragment;
import com.wedoops.platinumnobleclub.fragment.GuideFragment;
import com.wedoops.platinumnobleclub.fragment.MemberDashboardFragment;
import com.wedoops.platinumnobleclub.fragment.MyBookingFragment;
import com.wedoops.platinumnobleclub.fragment.MyReservationFragment;
import com.wedoops.platinumnobleclub.fragment.NewsFragment;
import com.wedoops.platinumnobleclub.fragment.PayFragment;
import com.wedoops.platinumnobleclub.fragment.RecordsFragment;
import com.wedoops.platinumnobleclub.fragment.RecordsListPtsFragment;
import com.wedoops.platinumnobleclub.fragment.ScanFragment;
import com.wedoops.platinumnobleclub.fragment.ServicesFragment;
import com.wedoops.platinumnobleclub.fragment.TermNCondFragment;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;
import com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE;
import com.wedoops.platinumnobleclub.helper.CustomTypefaceSpan;
import com.wedoops.platinumnobleclub.helper.DisplayAlertDialog;
import com.wedoops.platinumnobleclub.helper.IImagePickerLister;
import com.wedoops.platinumnobleclub.helper.ImagePickerEnum;
import com.wedoops.platinumnobleclub.webservices.Api_Constants;
import com.wedoops.platinumnobleclub.webservices.CallWebServices;
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


import static com.wedoops.platinumnobleclub.helper.CONSTANTS_VALUE.PICK_IMAGE_GALLERY_REQUEST_CODE;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
    private static final String TAG_DASHBOARD = "DASHBOARD";
    private static final String TAG_MEMBERQR = "MEMBER QR";

    private static final String TAG_ACCOUNT = "ACCOUNT";
    private static final String TAG_NOTIFICATION = "NOTIFICATION";
    private static final String TAG_MY_BOOKING = "MYBOOKING";
    private static final String TAG_MY_RESERVATION = "MYRESERVATION";
    private static final String TAG_TRANSACTION_REPORT = "TRANSACTIONREPORT";
    private static final String TAG_SERVICE = "SERVICE";
    private static final String TAG_BUTLER_SERVICE = "BUTLER_SERVICE";
    private static final String TAG_GUIDE = "GUIDE";
    private static final String TAG_BENEFIT = "BENEFIT";
    private static final String TAG_TERMNCOND = "TERMCONDITIONS";
    private static final String TAG_LOGOUT = "LOGOUT";
    private static final String FILE_NAME = "file_lang"; // preference file name
    private static final String KEY_LANG = "key_lang"; // preference key
    public static int navItemIndex = 0;
    public static String CURRENT_TAG = TAG_DASHBOARD;
    private static Context app;
    boolean doubleBackToExitPressedOnce = false;
    private Toolbar toolbar;
    private static NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private LinearLayout navFooter, homeBottomNav, recordBottomNav, navigationLayout;
    private FloatingActionButton floatingActionButton;
    private TextView textview_user_rank, textview_user_nickname, textview_user_wallet, textview_e_wallet, toolbar_title, textview_e_wallet1, textview_user_wallet1;
    private ImageView imageview_user_rank, toolbar_logo, toolbar_camera;
    private Handler mHandler;
    private RoundedImageView imageview_user_profile;
    private ImageButton imagebutton_language;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String currentPhotoPath = "";
    private static CustomProgressDialog customDialog;
    private AlertDialog alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);
        loadLanguage();
        setupProgressDialog();
        setupAdvertisement();
        setContentView(R.layout.activity_main);
        app = this;
        mHandler = new Handler();
        setupViewByID();
        setupToolbar();
        setupNavigationDrawer();

        navigationView.getMenu().getItem(1).setChecked(true);
        navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    private void setupProgressDialog() {
        customDialog = new CustomProgressDialog();

    }

    private void setupAdvertisement() {
//        MobileAds.initialize(this, "ca-app-pub-2772663182449117/8882399941");
//        MobileAds.initialize(this, "ca-app-pub-4167585096532227~2561120906");

//        MobileAds.initialize(this, "ca-app-pub-3940256099942544/2247696110");

    }


    private boolean checkAndRequestPermissions() {
        int networkStatePermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE);
        int internetPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        int readcalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
        int writecalPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (networkStatePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (internetPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (writecalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (readcalPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
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
                perms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);


                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED) {


                        mHandler = new Handler();

                        setupViewByID();
                        setupToolbar();
                        setupNavigationDrawer();

                        navigationView.getMenu().getItem(1).setChecked(true);
                        navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);

                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(
                                    this);
                            builder.setTitle(this.getString(R.string.error_title));
                            builder.setMessage(R.string.Recommended_Allow_Permission);
                            builder.setCancelable(false);
                            builder.setPositiveButton(this.getString(R.string.Ok),
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();
//                                            checkAndRequestPermissions();
                                        }
                                    });
                            builder.show();

                        } else {
                            Toast.makeText(this, R.string.Enable_permission_setting, Toast.LENGTH_LONG)
                                    .show();
                        }
                    }
                }
            }
            case CONSTANTS_VALUE.CAMERA_STORAGE_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                }
//                    new DisplayAlertDialog().displayImageSelectDialog(this, this);
                else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.storage_access_required, Toast.LENGTH_SHORT).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, R.string.camera_access_required, Toast.LENGTH_SHORT).show();
                } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(this, R.string.camera_and_storage_access_required, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CONSTANTS_VALUE.CALENDAR_REQUEST_CODE: {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, R.string.calendar_access_required, Toast.LENGTH_SHORT).show();
                break;
            }


            case CONSTANTS_VALUE.ONLY_CAMERA_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ImagePicker.with(this)
                            .crop(1f, 1f)                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(620, 620)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
                else {
                    Toast.makeText(this, R.string.camera_access_required, Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case CONSTANTS_VALUE.ONLY_STORAGE_REQUEST_CODE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    ImagePicker.with(this)
                            .crop(1f, 1f)                    //Crop image(Optional), Check Customization for more option
                            .compress(1024)            //Final image size will be less than 1 MB(Optional)
                            .maxResultSize(620, 620)    //Final image resolution will be less than 1080 x 1080(Optional)
                            .start();
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
        recordBottomNav = findViewById(R.id.bottom_nav_record);
        floatingActionButton = findViewById(R.id.bottom_nav_transaction);
        navigationLayout = findViewById(R.id.navigationLayout);

        imagebutton_language = navHeader.findViewById(R.id.imagebutton_language);
        imageview_user_rank = navHeader.findViewById(R.id.imageview_user_rank);
        imageview_user_profile = navHeader.findViewById(R.id.imageview_user_profile);

        textview_user_rank = navHeader.findViewById(R.id.textview_user_rank);
        textview_user_nickname = navHeader.findViewById(R.id.textview_user_nickname);
        textview_user_wallet = navHeader.findViewById(R.id.textview_user_wallet);
        textview_user_wallet1 = navHeader.findViewById(R.id.textview_user_wallet1);
        textview_e_wallet = navHeader.findViewById(R.id.textview_e_wallet);
        textview_e_wallet1 = navHeader.findViewById(R.id.textview_e_wallet1);


        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_logo = findViewById(R.id.toolbar_logo);
        toolbar_camera = findViewById(R.id.toolbar_camera);

        int width = (getResources().getDisplayMetrics().widthPixels * 2) / 3;
        DrawerLayout.LayoutParams params = (DrawerLayout.LayoutParams) navigationLayout.getLayoutParams();
        params.width = width;
        navigationLayout.setLayoutParams(params);

        navFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawers();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                View customLayout = getLayoutInflater().inflate(R.layout.dialog_custom_layout, null);
                TextView textview_title = customLayout.findViewById(R.id.textview_title);
                TextView textview_message = customLayout.findViewById(R.id.textview_message);
                Button button_cancel = customLayout.findViewById(R.id.button_cancel);
                Button button_ok = customLayout.findViewById(R.id.button_ok);

                textview_title.setText(getString(R.string.logout_title));
                textview_message.setText(MainActivity.this.getString(R.string.logout_confirmation));
                builder.setView(customLayout);

                builder.setCancelable(false);

                button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });


                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
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

                alert = builder.create();

                alert.show();


            }
        });

        homeBottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Menu menu = navigationView.getMenu();

                if (!menu.getItem(0).isChecked()) {
                    navigationView.getMenu().getItem(1).setChecked(true);
                    navigationView.getMenu().performIdentifierAction(R.id.menu_dashboard, 0);
                }

            }
        });

        recordBottomNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Menu menu = navigationView.getMenu();

                if (!menu.getItem(5).isChecked()) {
                    navigationView.getMenu().getItem(6).setChecked(true);
                    navigationView.getMenu().performIdentifierAction(R.id.menu_my_transactions_report, 0);
                }

            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QRcodeDialog qRcodeDialog = new QRcodeDialog(MainActivity.this);
                qRcodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                qRcodeDialog.show();
            }
        });


        toolbar_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < navigationView.getMenu().size(); i++) {
                    navigationView.getMenu().getItem(i).setChecked(false);
                }
                cameraScan();
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
                    case R.id.menu_memberQR:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_MEMBERQR;
                        break;

                    case R.id.menu_dashboard:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_DASHBOARD;

                        break;
                    case R.id.menu_account:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_ACCOUNT;

                        break;

                    case R.id.menu_my_booking:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_MY_BOOKING;

                        break;
                    case R.id.menu_my_reservation:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_MY_RESERVATION;

                        break;
                    case R.id.menu_my_transactions_report:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_TRANSACTION_REPORT;

                        break;
                    case R.id.menu_services:

                        navItemIndex = 6;
                        CURRENT_TAG = TAG_SERVICE;
                        break;
                    case R.id.menu_butler_services:

                        navItemIndex = 7;
                        CURRENT_TAG = TAG_BUTLER_SERVICE;
                        break;

                    case R.id.menu_guide:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_GUIDE;

                        break;

                    case R.id.menu_benefit:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_BENEFIT;

                        break;

                    case R.id.menu_termNCond:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_TERMNCOND;

                        break;

//                    case R.id.menu_notification:
//                        navItemIndex = 11;
//                        CURRENT_TAG = TAG_NOTIFICATION;
//
//                        break;

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
        String lang = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            imagebutton_language.setImageResource(R.drawable.language_usa);
        } else {
            imagebutton_language.setImageResource(R.drawable.language_china);
        }

        List<UserDetails> ud = UserDetails.listAll(UserDetails.class);

        if (ud.size() > 0) {


            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_BRONZE)) {
                imageview_user_rank.setImageResource(R.drawable.bronze_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_bronze));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_SILVER)) {
                imageview_user_rank.setImageResource(R.drawable.silver_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_silver));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_GOLD)) {
                imageview_user_rank.setImageResource(R.drawable.gold_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_gold));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_PLATINUM)) {
                imageview_user_rank.setImageResource(R.drawable.user_level_platinum);
                textview_user_rank.setText(getResources().getString(R.string.userrank_platinum));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_KNIGHT)) {
                imageview_user_rank.setImageResource(R.drawable.knight_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_knight));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_MARQUESS)) {
                imageview_user_rank.setImageResource(R.drawable.marquess_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_marquess));
            }

            if (ud.get(0).getUserLevelCode().contains(CONSTANTS_VALUE.USER_LEVEL_CODE_DUKE)) {
                imageview_user_rank.setImageResource(R.drawable.duke_medal);
                textview_user_rank.setText(getResources().getString(R.string.userrank_duke));
            }

            textview_user_nickname.setText(ud.get(0).getNickName());


            List<UserDetails> uds = UserDetails.listAll(UserDetails.class);
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
            String cash_wallet = decimalFormat.format(Double.parseDouble(uds.get(0).getCashWallet()));
            String e_wallet = decimalFormat.format(Double.parseDouble(uds.get(0).geteWallet()));

            textview_e_wallet.setText(String.format("%s ", e_wallet));
            textview_user_wallet.setText(String.format("%s ", cash_wallet));

            Glide.with(this).load(ud.get(0).getProfilePictureImagePath()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).placeholder(R.drawable.default_profile).timeout(10000).into(imageview_user_profile);

            Typeface typeface = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-regular.ttf");
            textview_user_nickname.setTypeface(typeface);

            Typeface typeface_crimson_700 = Typeface.createFromAsset(this.getAssets(), "fonts/crimson-text-v9-latin-700.ttf");
//            textview_user_wallet.setTypeface(typeface_crimson_700);
            textview_user_rank.setTypeface(typeface_crimson_700);
//            textview_e_wallet.setTypeface(typeface_crimson_700);
            textview_e_wallet1.setTypeface(typeface_crimson_700);
            textview_user_wallet1.setTypeface(typeface_crimson_700);
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (new ApplicationClass().checkSelfPermissions(this)) {
                ImagePicker.with(this)
                        .crop(1f, 1f)                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(620, 620)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        }


    }

    public void payScreen(final String scanned_value) {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {

                String[] split_scanned_value = scanned_value.split("\\|");

                if (split_scanned_value.length == 7) {

                    PayFragment payFragment = new PayFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("TRANSACTION_ID", split_scanned_value[0]);
                    bundle.putString("REMARKS", split_scanned_value[1]);
                    bundle.putString("ACTUAL_AMOUNT_POINTS", split_scanned_value[2]);
                    bundle.putString("ACTUAL_AMOUNT_MONEY", split_scanned_value[3]);
                    bundle.putString("DISCOUNTED_AMOUNT_POINTS", split_scanned_value[4]);
                    bundle.putString("DISCOUNTED_AMOUNT_MONEY", split_scanned_value[5]);
                    bundle.putString("SELECTED_CURRENCY", split_scanned_value[6]);

                    payFragment.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    fragmentTransaction.replace(R.id.framelayout_fragment_container, payFragment, "PAY");
                    fragmentTransaction.commitAllowingStateLoss();

                }


            }
        };

        if (mPendingRunnable != null) {
            toolbar_title.setText("Pay");
            mHandler.post(mPendingRunnable);
        }
    }

    private void cameraScan() {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                ScanFragment scanFragment = new ScanFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.framelayout_fragment_container, scanFragment, "SCAN");
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        if (mPendingRunnable != null) {
            toolbar_title.setVisibility(View.GONE);
            toolbar_logo.setVisibility(View.VISIBLE);
            mHandler.post(mPendingRunnable);

        }
    }


    public void loadHomeFragment() {

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
                QRcodeDialog qRcodeDialog = new QRcodeDialog(MainActivity.this);
                qRcodeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                qRcodeDialog.show();
            case 1:
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.VISIBLE);
                toolbar_camera.setVisibility(View.VISIBLE);
                MemberDashboardFragment dashboardFragment = new MemberDashboardFragment();
                checkAndRequestPermissions();
                return dashboardFragment;
            case 2:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_account));
                EditProfileFragment accountFragment = new EditProfileFragment();
                return accountFragment;

            case 3:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_my_booking));
                MyBookingFragment bookingFragment = new MyBookingFragment();
                return bookingFragment;

            case 4:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_my_reservation));
                MyReservationFragment reservationFragment = new MyReservationFragment();
                return reservationFragment;
            case 5:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_transactions_report));
                RecordsFragment transactionsReportFragment = new RecordsFragment();
                return transactionsReportFragment;
            case 6:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_service));
                ServicesFragment servicesFragment = new ServicesFragment();
                return servicesFragment;
            case 7:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_butler_service));
                ButlerServiceFragment butler_service = new ButlerServiceFragment();
                return butler_service;
            case 8:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_guide));
                GuideFragment guideFragment = new GuideFragment();
                return guideFragment;
            case 9:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_benefit));
                BenefitFragment benefitFragment = new BenefitFragment();
                return benefitFragment;
            case 10:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_term_condition));
                TermNCondFragment termNCondFragment = new TermNCondFragment();
                return termNCondFragment;

            case 11:
                toolbar_title.setVisibility(View.VISIBLE);
                toolbar_logo.setVisibility(View.GONE);
                toolbar_camera.setVisibility(View.VISIBLE);
                toolbar_title.setText(getString(R.string.nav_menu_Notification));
                NewsFragment newsFragment = new NewsFragment();
                return newsFragment;


            default:
                toolbar_title.setVisibility(View.GONE);
                toolbar_logo.setVisibility(View.VISIBLE);
                toolbar_camera.setVisibility(View.VISIBLE);

                return new MemberDashboardFragment();
        }
    }

    private static void selectNavMenu() {
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
        if (currentLanguage.equals("en_us") || currentLanguage.equals("en_gb") || currentLanguage.equals("")) {
            saveLanguage("zh");
        } else {
            saveLanguage("en_us");
        }

    }


    private void loadLanguage() {
        String lang = new ApplicationClass().readFromSharedPreferences(this, KEY_LANG);
        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            lang = "en_us";
        } else {
            lang = "zh";
        }
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
            if (navItemIndex != 1) {

                navItemIndex = 1;
                loadHomeFragment();

            } else {

                if (doubleBackToExitPressedOnce) {
                    finish();
                } else {
                    this.doubleBackToExitPressedOnce = true;
                    Toast.makeText(this, getString(R.string.close_app_msg), Toast.LENGTH_SHORT).show();

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

    private void callChangeProfilePictureWebService(String base64_string) {
//        customProgress.show();
        customDialog.showDialog(this);

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
//        customProgress.show();
//        customDialog.showDialog(this);

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
//        customProgress.dismiss();
        customDialog.hideDialog();

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

                        new DisplayAlertDialog().displayAlertDialogSuccess(success_object.getInt("Code"), MainActivity.this, MainActivity.this);

                        setupNavigationDrawer();


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MainActivity.this, MainActivity.this);
                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
                new DisplayAlertDialog().displayAlertDialogString(0, "Something Went Wrong, Please Try Again", false, MainActivity.this, MainActivity.this);
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
                        String eWallet = String.valueOf(response_object.getString("eWallet"));

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
                        ud.seteWallet(eWallet);
                        ud.save();

                        setupNavigationDrawer();
                        List<UserDetails> userd = UserDetails.listAll(UserDetails.class);

                        String table_name2 = UserDetails.getTableName(UserDetails.class);
                        String loginid = StringUtil.toSQLName("LoginID");

                        List<UserDetails> ud_list2 = UserDetails.findWithQuery(UserDetails.class, "SELECT * from " + table_name2 + " where " + loginid + " = ?", userd.get(0).getLoginID());

                        Bundle b = new Bundle();
                        b.putString("access_token", ud_list2.get(0).getAccessToken());
                        b.putString("memberID", ud_list2.get(0).getCardID()); //put memberID to process encryption
                        b.putInt(Api_Constants.COMMAND, Api_Constants.API_GET_ENCRYPTED_STRING);

                        new CallWebServices(Api_Constants.API_GET_ENCRYPTED_STRING, MainActivity.this, true).execute(b);


                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MainActivity.this, MainActivity.this);

                    }

                }
            } catch (Exception e) {
                Log.e("Error", e.toString());
            }

        } else if (command == Api_Constants.API_GET_ENCRYPTED_STRING) {

            boolean isSuccess = false;

            try {
                isSuccess = returnedObject.getBoolean("Success");

                if (isSuccess) {

                    if (returnedObject.getInt("StatusCode") == 200) {

                        List<MemberIDEncryted> mem = MemberIDEncryted.listAll(MemberIDEncryted.class);
                        if (mem.size() > 0) {
                            MemberIDEncryted.deleteAll(MemberIDEncryted.class);
                        }

                        String response_object = returnedObject.getString("ResponseData");

                        MemberIDEncryted memberIDEncryted = new MemberIDEncryted(response_object);
                        memberIDEncryted.save();

                    } else {
                        new DisplayAlertDialog().displayAlertDialogError(returnedObject.getInt("StatusCode"), MainActivity.this, MainActivity.this);

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
        if (resultCode == Activity.RESULT_OK) {
            Uri imageUri = data.getData();
            showImage(imageUri);
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.Task_Cancelled, Toast.LENGTH_SHORT).show();
        }
    }

    private void showImage(Uri imageUri) {
        try {

            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            imageview_user_profile.setImageURI(imageUri);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);

            callChangeProfilePictureWebService(encoded);

        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

//    @Override
//    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
//        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
//            openCamera();
//        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
//            openImagesDocument();
//    }
//
//    private void openImagesDocument() {
//        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        pictureIntent.setType("image/*");
//        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
//            pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
//        }
//        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
//    }
//
//    private void openCamera() {
//        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        File file;
//        try {
//            file = getImageFile(); // 1
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//        Uri uri;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) // 2
//            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID.concat(".provider"), file);
//        else
//            uri = Uri.fromFile(file); // 3
//        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 4
//        startActivityForResult(pictureIntent, CONSTANTS_VALUE.CAMERA_ACTION_PICK_REQUEST_CODE);
//    }
//
//    private File getImageFile() throws IOException {
//        String imageFileName = "JPEG_" + System.currentTimeMillis() + "_";
//        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
//
//        System.out.println(storageDir.getAbsolutePath());
//        if (storageDir.exists()) {
//            System.out.println("File exists");
//        } else {
//            System.out.println("File not exists");
//            storageDir.mkdirs();
//        }
//        File file = File.createTempFile(
//                imageFileName, ".jpg", storageDir
//        );
//        currentPhotoPath = "file:" + file.getAbsolutePath();
//        return file;
//    }
//
//    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
//        UCrop.Options options = new UCrop.Options();
//        options.setCircleDimmedLayer(true);
//        options.setDimmedLayerColor(Color.parseColor("#50ffffff"));
//        options.setHideBottomControls(true);
//        options.setShowCropGrid(false);
//        options.setCropFrameStrokeWidth(5);
//        options.setCropFrameColor(Color.WHITE);
//        options.setShowCropFrame(false);
//        UCrop.of(sourceUri, destinationUri).withOptions(options)
//                .withAspectRatio(1, 1)
//                .withMaxResultSize(300, 300)
//                .start(this);
//    }

    public static void redirectDashboard() {
        navItemIndex = 1;
        CURRENT_TAG = TAG_DASHBOARD;
        navigationView.getMenu().getItem(1).setChecked(true);
    }

}
