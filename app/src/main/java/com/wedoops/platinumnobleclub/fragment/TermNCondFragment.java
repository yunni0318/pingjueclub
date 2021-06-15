package com.wedoops.platinumnobleclub.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.wedoops.platinumnobleclub.R;
import com.wedoops.platinumnobleclub.helper.ApplicationClass;

import java.util.Locale;

public class TermNCondFragment extends Fragment {

    private Context get_context;
    private View view;
    private WebView webView;
    private static final String KEY_LANG = "key_lang";
    private static AlertDialog alert;
    private String lang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.term_cond_fragment, container, false);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        get_context = getContext();
        this.view = view;
        loadLanguage();
        setupDeclaration();
        displayResult();
    }

    private void loadLanguage() {
        lang = new ApplicationClass().readFromSharedPreferences(get_context, KEY_LANG);
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

    private void setupDeclaration() {
        webView = view.findViewById(R.id.webView);
    }

    private void displayResult() {

        webView.setBackgroundColor(Color.parseColor("#191919"));
        if (lang.equals("en_us") || lang.equals("en_gb") || lang.equals("")) {
            webView.loadUrl("https://web.platinumnobleclub.com/tnc");
        } else {
            webView.loadUrl("https://web.platinumnobleclub.com/tnc");
        }
//        webView.loadUrl("http://103.198.194.228:54153/tnc");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                    view.getContext().startActivity(
                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                view.setBackgroundColor(Color.parseColor("#191919"));
                view.loadUrl("about:blank");

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext());

                View customLayout = getLayoutInflater().inflate(R.layout.dialog_ok_only_custom_layout, null);
                TextView textview_title = customLayout.findViewById(R.id.textview_title);
                TextView textview_message = customLayout.findViewById(R.id.textview_message);
                Button button_ok = customLayout.findViewById(R.id.button_ok);

                textview_title.setText("Warning");
                textview_message.setText("something went wrong, please try again later");
                button_ok.setText(view.getContext().getString(R.string.Ok));

                builder.setView(customLayout);

                builder.setCancelable(false);


                button_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert = builder.create();

                alert.show();



//                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_Dialog_Alert);
//                builder.setTitle("Warning");
//                builder.setMessage("something went wrong, please try again later");
//                builder.setCancelable(false);
//                builder.setPositiveButton("OK",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//
//                                dialog.dismiss();
//
//                            }
//                        });
//                builder.show();
            }
        });
    }




}
