package com.wedoops.platinumnobleclub;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ButlerServiceFragment extends Fragment {

    private View view;
    private WebView webView;
    private static AlertDialog alert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.butler_service_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        setupDeclaration();
    }

    private void setupDeclaration() {
        webView = view.findViewById(R.id.webView);

        webView.setBackgroundColor(Color.parseColor("#191919"));
        webView.loadUrl("http://platinumnobleclub.com/PJ_Services.html");
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

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

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
