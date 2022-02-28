package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;
import static com.solodroid.ads.sdk.util.Constant.UNITY;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.TemplateView;
import com.solodroid.ads.sdk.util.Tools;

public class NativeAdViewPager {

    public static class Builder {

        private static final String TAG = "AdNetwork";
        private final Activity activity;

        View view;
        MediaView mediaView;
        TemplateView admob_native_ad;
        LinearLayout admob_native_background;
        View startapp_native_ad;
        ImageView startapp_native_image;
        ImageView startapp_native_icon;
        TextView startapp_native_title;
        TextView startapp_native_description;
        Button startapp_native_button;
        LinearLayout startapp_native_background;

        FrameLayout applovin_native_ad;

        ProgressBar progress_bar_ad;

        private String adStatus = "";
        private String adNetwork = "";
        private String backupAdNetwork = "";
        private String adMobNativeId = "";
        private String appLovinNativeId = "";
        private int placementStatus = 1;
        private boolean darkTheme = false;
        private boolean legacyGDPR = false;

        public Builder(Activity activity, View view) {
            this.activity = activity;
            this.view = view;
        }

        public Builder build() {
            loadNativeAd();
            return this;
        }

        public Builder setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Builder setAdNetwork(String adNetwork) {
            this.adNetwork = adNetwork;
            return this;
        }

        public Builder setBackupAdNetwork(String backupAdNetwork) {
            this.backupAdNetwork = backupAdNetwork;
            return this;
        }

        public Builder setAdMobNativeId(String adMobNativeId) {
            this.adMobNativeId = adMobNativeId;
            return this;
        }

        public Builder setAppLovinNativeId(String appLovinNativeId) {
            this.appLovinNativeId = appLovinNativeId;
            return this;
        }

        public Builder setPlacementStatus(int placementStatus) {
            this.placementStatus = placementStatus;
            return this;
        }

        public Builder setDarkTheme(boolean darkTheme) {
            this.darkTheme = darkTheme;
            return this;
        }

        public Builder setLegacyGDPR(boolean legacyGDPR) {
            this.legacyGDPR = legacyGDPR;
            return this;
        }

        public void loadNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                admob_native_ad = view.findViewById(R.id.admob_native_ad_container);
                mediaView = view.findViewById(R.id.media_view);
                admob_native_background = view.findViewById(R.id.background);
                startapp_native_ad = view.findViewById(R.id.startapp_native_ad_container);
                startapp_native_image = view.findViewById(R.id.startapp_native_image);
                startapp_native_icon = activity.findViewById(R.id.startapp_native_icon);
                startapp_native_title = view.findViewById(R.id.startapp_native_title);
                startapp_native_description = view.findViewById(R.id.startapp_native_description);
                startapp_native_button = view.findViewById(R.id.startapp_native_button);
                startapp_native_button.setOnClickListener(v1 -> startapp_native_ad.performClick());
                startapp_native_background = view.findViewById(R.id.startapp_native_background);
                applovin_native_ad = view.findViewById(R.id.applovin_native_ad_container);
                progress_bar_ad = view.findViewById(R.id.progress_bar_ad);
                progress_bar_ad.setVisibility(View.VISIBLE);

                switch (adNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        progress_bar_ad.setVisibility(View.GONE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            loadBackupNativeAd();
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                            progress_bar_ad.setVisibility(View.GONE);
                        }
                        break;

                    case STARTAPP:
                    case APPLOVIN_MAX:
                    case APPLOVIN:
                    case UNITY:
                        //do nothing
                        break;

                }

            }

        }

        public void loadBackupNativeAd() {

            if (adStatus.equals(AD_STATUS_ON) && placementStatus != 0) {

                admob_native_ad = view.findViewById(R.id.admob_native_ad_container);
                mediaView = view.findViewById(R.id.media_view);
                admob_native_background = view.findViewById(R.id.background);
                startapp_native_ad = view.findViewById(R.id.startapp_native_ad_container);
                startapp_native_image = view.findViewById(R.id.startapp_native_image);
                startapp_native_icon = activity.findViewById(R.id.startapp_native_icon);
                startapp_native_title = view.findViewById(R.id.startapp_native_title);
                startapp_native_description = view.findViewById(R.id.startapp_native_description);
                startapp_native_button = view.findViewById(R.id.startapp_native_button);
                startapp_native_button.setOnClickListener(v1 -> startapp_native_ad.performClick());
                startapp_native_background = view.findViewById(R.id.startapp_native_background);
                progress_bar_ad = view.findViewById(R.id.progress_bar_ad);
                progress_bar_ad.setVisibility(View.VISIBLE);

                switch (backupAdNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(activity, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(activity, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        progress_bar_ad.setVisibility(View.GONE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admob_native_ad.setVisibility(View.GONE);
                                            progress_bar_ad.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest(activity, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob Native Ad has been loaded");
                            progress_bar_ad.setVisibility(View.GONE);
                        }
                        break;

                    case STARTAPP:
                    case APPLOVIN_MAX:
                    case APPLOVIN:
                    case UNITY:
                    case NONE:
                        //do nothing
                        break;

                }
            }
        }
    }
}
