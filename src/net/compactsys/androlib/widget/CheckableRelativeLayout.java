/*
 * Copyright 2012 Ivan Masmitjà
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.compactsys.androlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Extension of a relative layout to provide a checkable behaviour.
 * <p/>
 * Based on marvinlabs work
 * @see <a href="http://blog.marvinlabs.com/2010/10/29/custom-listview-ability-check-items/">marvinlabs blog entry</a>
 */
public class CheckableRelativeLayout extends RelativeLayout implements
        Checkable {

    public static final int CAPACITY = 5;
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private boolean isChecked;
    private List<Checkable> checkableViews;

    public CheckableRelativeLayout(Context context, AttributeSet attrs,
                                   int defStyle) {
        super(context, attrs, defStyle);
        initialise();
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    /*
     * @see android.widget.Checkable#isChecked()
     */
    public boolean isChecked() {
        return isChecked;
    }

    /*
     * @see android.widget.Checkable#setChecked(boolean)
     */
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        for (Checkable c : checkableViews) {
            c.setChecked(isChecked);
        }

    }

    /*
     * @see android.widget.Checkable#toggle()
     */
    public void toggle() {
        this.isChecked = !this.isChecked;
        for (Checkable c : checkableViews) {
            c.toggle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        final int childCount = this.getChildCount();
        for (int i = 0; i < childCount; ++i) {
            findCheckableChildren(this.getChildAt(i));
        }
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    /**
     * Initialize widget.
     */
    private void initialise() {
        this.isChecked = false;
        this.checkableViews = new ArrayList<Checkable>(CAPACITY);
    }

    /**
     * Add to our checkable list all the children of the view that implement the
     * interface Checkable.
     */
    private void findCheckableChildren(View v) {
        if (v instanceof Checkable) {
            this.checkableViews.add((Checkable) v);
        }

        if (v instanceof ViewGroup) {
            final ViewGroup vg = (ViewGroup) v;
            final int childCount = vg.getChildCount();
            for (int i = 0; i < childCount; ++i) {
                findCheckableChildren(vg.getChildAt(i));
            }
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
