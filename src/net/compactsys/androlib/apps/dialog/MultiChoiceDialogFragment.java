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
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class MultiChoiceDialogFragment extends DialogFragment {

    public final static String ARG_TITLE = "title";
    public final static String ARG_CANCELABE = "cancelable";

    private ListView mListView = null;
    private ListAdapter mAdapter;

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

        mListView = new ListView(getActivity());
        mListView.setId(R.id.list);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setCancelable(cancelable)
                .setView(mListView);


        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultiChoiceDialogFragment.this.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultiChoiceDialogFragment.this.dismiss();
            }
        });

        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
        }

        return builder.create();
    }

    private ListView getListView() {
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
