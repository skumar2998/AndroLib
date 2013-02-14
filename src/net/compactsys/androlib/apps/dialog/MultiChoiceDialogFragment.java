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

import android.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

import net.compactsys.androlib.util.ArrayUtils;

public class MultiChoiceDialogFragment extends InternalDialogFragment {

    public final static String ARG_TITLE = "title";
    public final static String ARG_CANCELABE = "cancelable";

    private final static String EXTRA_SELECTED_ITEMS = "selectedItems";

    private ListAdapter mAdapter;
    public CharSequence mPositiveButtonText;
    public DialogInterface.OnClickListener mPositiveButtonListener;
    public CharSequence mNegativeButtonText;
    public DialogInterface.OnClickListener mNegativeButtonListener;

    public static MultiChoiceDialogFragment newInstance(String title, boolean cancelable) {

        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putBoolean(ARG_CANCELABE, cancelable);

        MultiChoiceDialogFragment ldf = new MultiChoiceDialogFragment();
        ldf.setArguments(args);
        ldf.setCancelable(cancelable);
        return ldf;
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

        // Create ListView
        ListView mListView = new ListView(getActivity());
        mListView.setId(R.id.list);
        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //Create dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setCancelable(cancelable)
                .setView(mListView);

        if (mPositiveButtonText != null) {
            builder.setPositiveButton(mPositiveButtonText, mPositiveButtonListener);
        } else {
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MultiChoiceDialogFragment.this.dismiss();
                }
            });
        }

        if (mNegativeButtonText != null) {
            builder.setNegativeButton(mNegativeButtonText, mNegativeButtonListener);
        }

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }

        // check previous items
        int[] selectedItems;
        Bundle internalState = getInternalState();
        if (internalState != null) {
            selectedItems = internalState.getIntArray(EXTRA_SELECTED_ITEMS);
            if (selectedItems != null) {
                for (int position : selectedItems) {
                    mListView.setItemChecked(position, true);
                }
            }
        }

        return builder.create();
    }

    public ListView getListView() {
        AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {
            return (ListView) dialog.findViewById(R.id.list);
        }
        return null;
    }

    public void setAdapter(ListAdapter adapter) {
        mAdapter = adapter;

        ListView lv = getListView();
        if (lv != null)
            lv.setAdapter(adapter);
    }

    public void setPositiveButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        mPositiveButtonText = text;
        mPositiveButtonListener = listener;
    }

    public void setNegativeeButton(CharSequence text, final DialogInterface.OnClickListener listener) {
        mNegativeButtonText = text;
        mNegativeButtonListener = listener;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ListView lv = this.getListView();
        if (lv != null) {
            // Don't work -> savedInstanceState value is NULL in OnCreate()
            // Save in HackishSavedState
            Bundle internalState = new Bundle();
            internalState.putIntArray(EXTRA_SELECTED_ITEMS,
                    ArrayUtils.toArray(lv.getCheckedItemPositions()));
            saveInternalState(internalState);
        } else {
            resetInternalState();
        }
    }

    //private void setPositiveButton
    @Override
    public void onDestroyView() {
        // Used because of a bug in the support library
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }
}
