package com.malmstein.floatinghints;

/*
 * Copyright (C) 2014 David Gonzalez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Layout composed by an {@link android.widget.EditText} and a {@link android.widget.TextView} to show a
 * floating label when the hint is hidden due to the user inputting text.
 *
 * @see <a href="https://plus.google.com/+ChrisBanes/posts/5Ejaq51UWGo">Chris Banes implementation</a>
 * @see <a href="https://plus.google.com/118417777153109946393/posts/ewdTd7bNw29">Cyril Mottier's on App polishing</a>
 * @see <a href="https://dribbble.com/shots/1254439--GIF-Mobile-Form-Interaction">Matt D. Smith on Dribble</a>
 * @see <a href="http://bradfrostweb.com/blog/post/float-label-pattern/">Brad Frost's blog post</a>
 */
public final class FloatEditTextLayout extends FrameLayout {

    private static final float DEFAULT_PADDING_LEFT_RIGHT_DP = 12f;

    private EditText mEditText;
    private TextView mLabel;

    private final Animation inAnimation;
    private final Animation outAnimation;

    public FloatEditTextLayout(Context context) {
        this(context, null);
    }

    public FloatEditTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatEditTextLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final TypedArray a = context
                .obtainStyledAttributes(attrs, R.styleable.FloatEditTextLayout);

        inAnimation = AnimationUtils.loadAnimation(getContext(),
                a.getResourceId(R.styleable.FloatEditTextLayout_floatLayoutInAnimation, R.anim.slide_in_top));
        inAnimation.setAnimationListener(showLabelAnimationListener);

        outAnimation = AnimationUtils.loadAnimation(getContext(),
                a.getResourceId(R.styleable.FloatEditTextLayout_floatLayoutOutAnimation, R.anim.slide_out_bottom));
        outAnimation.setAnimationListener(hideLabelAnimationListener);

        final int sidePadding = a.getDimensionPixelSize(
                R.styleable.FloatEditTextLayout_floatLayoutSidePadding,
                dipsToPix(DEFAULT_PADDING_LEFT_RIGHT_DP));

        mLabel = new TextView(context);
        mLabel.setPadding(sidePadding, 0, sidePadding, 0);
        mLabel.setVisibility(INVISIBLE);

        mLabel.setTextAppearance(context,
                a.getResourceId(R.styleable.FloatEditTextLayout_floatLayoutTextAppearance,
                        android.R.style.TextAppearance_Small)
        );

        addView(mLabel, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        a.recycle();
    }

    @Override
    public final void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof EditText) {
            // If we already have an EditText, throw an exception
            if (mEditText != null) {
                throw new IllegalArgumentException("We already have an EditText, can only have one");
            }

            // Update the layout params so that the EditText is at the bottom, with enough top
            // margin to show the label
            final LayoutParams lp = new LayoutParams(params);
            lp.gravity = Gravity.BOTTOM;
            lp.topMargin = (int) mLabel.getTextSize();
            params = lp;

            setEditText((EditText) child);
        }

        // Carry on adding the View...
        super.addView(child, index, params);
    }

    private void setEditText(EditText editText) {
        mEditText = editText;

        // Add a TextWatcher so that we know when the text input has changed
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) {
                    // The text is empty, so hide the label if it is visible
                    if (mLabel.getVisibility() == View.VISIBLE) {
                        hideLabel();
                    }
                } else {
                    // The text is not empty, so show the label if it is not visible
                    if (mLabel.getVisibility() != View.VISIBLE) {
                        showLabel();
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        // Add focus listener to the EditText so that we can notify the label that it is activated.
        // Allows the use of a ColorStateList for the text color on the label
        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                mLabel.setActivated(focused);
            }
        });

        mLabel.setText(mEditText.getHint());
    }

    /**
     * @return the {@link android.widget.EditText} text input
     */
    public EditText getEditText() {
        return mEditText;
    }

    /**
     * @return the {@link android.widget.TextView} label
     */
    public TextView getLabel() {
        return mLabel;
    }

    /**
     * Show the label using an animation
     */
    private void showLabel() {
        mLabel.startAnimation(inAnimation);
    }

    /**
     * Hide the label using an animation
     */
    private void hideLabel() {
        mLabel.startAnimation(outAnimation);
    }

    /**
     * Helper method to convert dips to pixels.
     */
    private int dipsToPix(float dps) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps,
                getResources().getDisplayMetrics());
    }

    /**
     * Animation listener triggered when showing the label
     */
    private Animation.AnimationListener showLabelAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            mLabel.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };

    /**
     * Animation listener triggered when hiding the label
     */
    private Animation.AnimationListener hideLabelAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            mLabel.setVisibility(View.GONE);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };
}