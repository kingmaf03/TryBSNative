// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.ads.nativetemplates;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.nativetemplates.R;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAd.Image;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static android.net.sip.SipSession.State.NOT_DEFINED;

/** Base class for a template view. * */
public class BSNative extends RelativeLayout {

  private int templateType;
  private UnifiedNativeAd nativeAd;
  private UnifiedNativeAdView nativeAdView;

  private TextView primaryView;
  private TextView secondaryView;
  private MaterialRatingBar ratingBar;
  private TextView tertiaryView;
  private ImageView iconView;
  private MediaView mediaView;
  private TextView callToActionView;
  private ConstraintLayout root_view;
  private CardView card_button;
  private int bs_button_background_color = NOT_DEFINED;
  private int bs_button_text_color = NOT_DEFINED;
  private int bs_primary_text_color = NOT_DEFINED;
  private int bs_secondary_text_color = NOT_DEFINED;
  private int bs_rating_color = NOT_DEFINED;
  private int bs_background_color = NOT_DEFINED;

  private static final String MEDIUM_TEMPLATE = "medium_template";
  private static final String SMALL_TEMPLATE = "small_template";

  public BSNative(Context context) {
    super(context);
  }

  public BSNative(Context context, AttributeSet attrs) {
    super(context, attrs);
    initView(context, attrs);
  }

  public BSNative(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initView(context, attrs);
  }


  private boolean adHasOnlyStore(UnifiedNativeAd nativeAd) {
    String store = nativeAd.getStore();
    String advertiser = nativeAd.getAdvertiser();
    return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser);
  }

  public void setNativeAd(UnifiedNativeAd nativeAd) {
    this.nativeAd = nativeAd;
    String secondaryText;
    nativeAdView.setCallToActionView(callToActionView);
    nativeAdView.setHeadlineView(primaryView);
    nativeAdView.setMediaView(mediaView);
    secondaryView.setVisibility(VISIBLE);
    if (adHasOnlyStore(nativeAd)) {
      nativeAdView.setStoreView(secondaryView);
      secondaryText = nativeAd.getStore();
    } else if (!TextUtils.isEmpty(nativeAd.getAdvertiser())) {
      nativeAdView.setAdvertiserView(secondaryView);
      secondaryText = nativeAd.getAdvertiser();
    } else {
      secondaryText = "";
    }

    primaryView.setText(nativeAd.getHeadline());
    callToActionView.setText(nativeAd.getCallToAction());

    if (nativeAd.getStarRating() != null && nativeAd.getStarRating() > 0) {
      secondaryView.setVisibility(GONE);
      ratingBar.setVisibility(VISIBLE);
      ratingBar.setMax(5);
      ratingBar.setRating(3);
      nativeAdView.setStarRatingView(ratingBar);
    } else {
      secondaryView.setText(secondaryText);
      secondaryView.setVisibility(VISIBLE);
    }

    if (nativeAd.getIcon() != null) {
      iconView.setVisibility(VISIBLE);
      iconView.setImageDrawable(nativeAd.getIcon().getDrawable());
    }

    if (tertiaryView != null) {
      tertiaryView.setText(nativeAd.getBody());
      nativeAdView.setBodyView(tertiaryView);
    }

    nativeAdView.setNativeAd(nativeAd);
  }

  public void destroyNativeAd() {
    nativeAd.destroy();
  }

  private void initView(Context context, AttributeSet attributeSet) {
    if (attributeSet != null) {
      Resources resources = getResources();
      TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.BSNative);
      templateType = typedArray.getResourceId(R.styleable.BSNative_bs_template_type, R.layout.medium_size);
      bs_button_background_color = typedArray.getColor(R.styleable.BSNative_bs_button_background_color, resources.getColor(R.color.bs_button_background_color));
      bs_button_text_color = typedArray.getColor(R.styleable.BSNative_bs_button_text_color, resources.getColor(R.color.bs_button_text_color));
      bs_primary_text_color = typedArray.getColor(R.styleable.BSNative_bs_primary_text_color, resources.getColor(R.color.bs_primary_text_color));
      bs_secondary_text_color = typedArray.getColor(R.styleable.BSNative_bs_secondary_text_color, resources.getColor(R.color.bs_secondary_text_color));
      bs_rating_color = typedArray.getColor(R.styleable.BSNative_bs_rating_color, resources.getColor(R.color.bs_rating_color));
      bs_background_color = typedArray.getColor(R.styleable.BSNative_bs_background_color, resources.getColor(R.color.bs_background_color));
      typedArray.recycle();
    }

    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    assert inflater != null;
    inflater.inflate(templateType, this);
  }

  @Override
  public void onFinishInflate() {
    super.onFinishInflate();
    root_view = findViewById(R.id.root_view);
    root_view.setBackgroundColor(bs_background_color);
    nativeAdView = findViewById(R.id.native_ad_view);
    primaryView = findViewById(R.id.primary);
    primaryView.setTextColor(bs_primary_text_color);
    secondaryView = findViewById(R.id.secondary);
    secondaryView.setTextColor(bs_secondary_text_color);
    tertiaryView = findViewById(R.id.body);
    tertiaryView.setTextColor(bs_secondary_text_color);
    ratingBar = findViewById(R.id.rating_bar);
    callToActionView = findViewById(R.id.cta);
    callToActionView.setTextColor(bs_button_text_color);
    iconView = findViewById(R.id.icon);
    mediaView = findViewById(R.id.media_view);
    card_button = findViewById(R.id.card_button);
    card_button.setCardBackgroundColor(bs_button_background_color);
  }
}
