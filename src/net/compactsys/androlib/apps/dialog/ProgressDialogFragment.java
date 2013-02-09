/*
 * Copyright 2013 Ivan Masmitjà
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

package net.compactsys.androlib.apps.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Bundle;


public class ProgressDialogFragment extends DialogFragment {

    public final static String ARG_TITLE = "title";
    public final static String ARG_MESSAGE = "message";
    public final static String ARG_CANCELABE = "cancelable";

    private int mMax;
    private int mIncrementBy;
    private int mProgress;

    public static ProgressDialogFragment newInstance(String title, String message, boolean cancelable) {
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putBoolean(ARG_CANCELABE, cancelable);

        ProgressDialogFragment pdf = new ProgressDialogFragment();
        pdf.setArguments(args);
        pdf.setCancelable(cancelable);
        return pdf;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog pd = new ProgressDialog(getActivity());

        final String title = getArguments().getString(ARG_TITLE);
        final String message = getArguments().getString(ARG_MESSAGE);
        final boolean cancelable = getArguments().getBoolean(ARG_CANCELABE);

        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        if (null != title) {
            pd.setTitle(title);
        }

        if (null != message) {
            pd.setMessage(message);
        }

        pd.setCancelable(cancelable);
        pd.setCanceledOnTouchOutside(false);

        if (mMax > 0) {
            pd.setMax(mMax);
        }

        if (mProgress > 0) {
            pd.setProgress(mProgress);
        }

        if (mIncrementBy > 0) {
            pd.incrementProgressBy(mIncrementBy);
        }

        setCancelable(cancelable);

        return pd;
    }

    public void updateMessage(String message) {
        if (getDialog() == null) {
            return;
        }
        ((ProgressDialog) getDialog()).setMessage(message);
    }

    public void setMax(int max) {
        ProgressDialog pd = (ProgressDialog) getDialog();
        if (pd != null) {
            pd.setMax(max);
        } else {
            mMax = max;
        }
    }

    public void setProgress(int progress) {
        ProgressDialog pd = (ProgressDialog) getDialog();
        if (pd != null) {
            pd.setProgress(progress);
        } else {
            mProgress = progress;
        }
    }

    public void incrementProgressBy(int increment) {
        ProgressDialog pd = (ProgressDialog) getDialog();
        if (pd != null) {
            pd.incrementProgressBy(increment);
        } else {
            mIncrementBy += increment;
        }
    }

    @Override
    public void onDestroyView() {
        // Used because of a bug in the support library
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
