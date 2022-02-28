package com.solodroid.ads.sdk.format;

import static com.solodroid.ads.sdk.util.Constant.ADMOB;
import static com.solodroid.ads.sdk.util.Constant.AD_STATUS_ON;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN;
import static com.solodroid.ads.sdk.util.Constant.APPLOVIN_MAX;
import static com.solodroid.ads.sdk.util.Constant.NONE;
import static com.solodroid.ads.sdk.util.Constant.STARTAPP;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.solodroid.ads.sdk.R;
import com.solodroid.ads.sdk.util.NativeTemplateStyle;
import com.solodroid.ads.sdk.util.TemplateView;
import com.solodroid.ads.sdk.util.Tools;

public class NativeAdViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AdNetwork";
    LinearLayout native_ad_view_container;

    //AdMob
    MediaView mediaView;
    TemplateView admob_native_ad;
    LinearLayout admob_native_background;

    //StartApp
    View startapp_native_ad;
    ImageView startapp_native_image;
    ImageView startapp_native_icon;
    TextView startapp_native_title;
    TextView startapp_native_description;
    Button startapp_native_button;
    LinearLayout startapp_native_background;

    //AppLovin
    FrameLayout applovin_native_ad;

    public NativeAdViewHolder(View v) {
        super(v);

        native_ad_view_container = v.findViewById(R.id.native_ad_view_container);

        //AdMob
        admob_native_ad = v.findViewById(R.id.admob_native_ad_container);
        mediaView = v.findViewById(R.id.media_view);
        admob_native_background = v.findViewById(R.id.background);

        //StartApp
        startapp_native_ad = v.findViewById(R.id.startapp_native_ad_container);
        startapp_native_image = v.findViewById(R.id.startapp_native_image);
        startapp_native_icon = v.findViewById(R.id.startapp_native_icon);
        startapp_native_title = v.findViewById(R.id.startapp_native_title);
        startapp_native_description = v.findViewById(R.id.startapp_native_description);
        startapp_native_button = v.findViewById(R.id.startapp_native_button);
        startapp_native_button.setOnClickListener(v1 -> itemView.performClick());
        startapp_native_background = v.findViewById(R.id.startapp_native_background);

        //AppLovin
        applovin_native_ad = v.findViewById(R.id.applovin_native_ad_container);

    }

    public void loadNativeAd(Context context, String adStatus, int placementStatus, String adNetwork, String backupAdNetwork, String adMobNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeStyles) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (adNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        native_ad_view_container.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            //admob_native_ad.setVisibility(View.GONE);
                                            //native_ad_view_container.setVisibility(View.GONE);
                                            loadBackupNativeAd(context, adStatus, placementStatus, backupAdNetwork, adMobNativeId, appLovinNativeId, darkTheme, legacyGDPR, nativeStyles);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case STARTAPP:
                    case APPLOVIN_MAX:
                    case APPLOVIN:
                       //do nothing
                        break;

                }
            }
        }
    }

    public void loadBackupNativeAd(Context context, String adStatus, int placementStatus, String backupAdNetwork, String adMobNativeId, String appLovinNativeId, boolean darkTheme, boolean legacyGDPR, String nativeStyles) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (placementStatus != 0) {
                switch (backupAdNetwork) {
                    case ADMOB:
                        if (admob_native_ad.getVisibility() != View.VISIBLE) {
                            AdLoader adLoader = new AdLoader.Builder(context, adMobNativeId)
                                    .forNativeAd(NativeAd -> {
                                        if (darkTheme) {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorBackgroundDark));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundDark);
                                        } else {
                                            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.colorBackgroundLight));
                                            NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                            admob_native_ad.setStyles(styles);
                                            admob_native_background.setBackgroundResource(R.color.colorBackgroundLight);
                                        }
                                        mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                        admob_native_ad.setNativeAd(NativeAd);
                                        admob_native_ad.setVisibility(View.VISIBLE);
                                        native_ad_view_container.setVisibility(View.VISIBLE);
                                    })
                                    .withAdListener(new AdListener() {
                                        @Override
                                        public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                            admob_native_ad.setVisibility(View.GONE);
                                            native_ad_view_container.setVisibility(View.GONE);
                                        }
                                    })
                                    .build();
                            adLoader.loadAd(Tools.getAdRequest((Activity) context, legacyGDPR));
                        } else {
                            Log.d(TAG, "AdMob native ads has been loaded");
                        }
                        break;

                    case STARTAPP:
                    case APPLOVIN_MAX:
                    case APPLOVIN:
                    case NONE:
                        native_ad_view_container.setVisibility(View.GONE);
                        break;

                }
            }
        }
    }

    public void setNativeAdPadding(int left, int top, int right, int bottom) {
        native_ad_view_container.setPadding(left, top, right, bottom);
    }

}
