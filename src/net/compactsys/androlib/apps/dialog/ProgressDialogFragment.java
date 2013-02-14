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

public class ProgressDialogFragment extends InternalDialogFragment {

    public final static String ARG_TITLE = "title";
    public final static String ARG_MESSAGE = "message";
    public final static String ARG_CANCELABE = "cancelable";
    public final static String ARG_STYLE = "style";

    private final static String ARG_PROGRESS = "progress";

    private int mMax;
    private int mProgress;
    private String mMessage;

    /**
     * Create a spinner progress dialog.
     *
     * @param title      Title displayed in the {@link Dialog}.
     * @param message    Set the message to display.
     * @param cancelable Sets whether the dialog is cancelable or not
     * @return ProgressDialogFragment
     */
    public static ProgressDialogFragment newInstance(String title, String message, boolean cancelable) {
        return newInstance(title, message, ProgressDialog.STYLE_SPINNER, cancelable);
    }

    /**
     * Create a spinner progress dialog.
     *
     * @param title      Title displayed in the {@link Dialog}.
     * @param message    Set the message to display.
     * @param cancelable Sets whether the dialog is cancelable or not
     * @return ProgressDialogFragment
     * @pram style STYLE_SPINNER or STYLE_HORIZONTAL
     */
    public static ProgressDialogFragment newInstance(String title, String message, int style, boolean cancelable) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_MESSAGE, message);
        args.putBoolean(ARG_CANCELABE, cancelable);
        args.putInt(ARG_STYLE, style);

        ProgressDialogFragment pdf = new ProgressDialogFragment();
        pdf.setArguments(args);
        pdf.setCancelable(cancelable);
        return pdf;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(ARG_TITLE);
        boolean cancelable = getArguments().getBoolean(ARG_CANCELABE);
        int style = getArguments().getInt(ARG_STYLE);
        mMessage = getArguments().getString(ARG_MESSAGE);
        mProgress = getArguments().getInt(ARG_PROGRESS);

        // savedInstanceState not work!!! savedInstanceState value is NULL

        // Restore instance from HackishSavedState
        Bundle savedBundle = getInternalState();
        if (savedBundle != null) {
            mMessage = savedBundle.getString(ARG_MESSAGE);
            mProgress = savedBundle.getInt(ARG_PROGRESS);
        }

        ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setProgressStyle(style);
        pd.setCancelable(cancelable);
        pd.setCanceledOnTouchOutside(false);

        if (null != title) {
            pd.setTitle(title);
        }

        if (null != mMessage) {
            pd.setMessage(mMessage);
        }

        if (mMax > 0) {
            pd.setMax(mMax);
        }

        if (mProgress > 0) {
            setProgress(mProgress);
        }

        return pd;
    }

    public void updateMessage(String message) {
        mMessage = message;
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
        mProgress = progress;
        ProgressDialog pd = (ProgressDialog) getDialog();
        if (pd != null) {
            pd.setProgress(progress);
        }
    }

    public void incrementProgressBy(int increment) {
        mProgress += increment;
        ProgressDialog pd = (ProgressDialog) getDialog();
        if (pd != null) {
            pd.incrementProgressBy(increment);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ProgressDialog dlg = (ProgressDialog) getDialog();
        if (dlg != null) {
            // Don't work -> savedInstanceState value is NULL in OnCreate()

            // Save in HackishSavedState
            Bundle internalState = new Bundle();
            internalState.putString(ARG_MESSAGE, mMessage);
            internalState.putInt(ARG_PROGRESS, dlg.getProgress());
            saveInternalState(internalState);
        } else {
            resetInternalState();
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
