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

package net.compactsys.androlib.apps.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DateTimePickerDialogFragment extends DialogFragment {
    public interface DateDialogFragmentListener {
        public void dateDialogFragmentDateSet(int dialogId, Calendar date);
    }

    private DateDialogFragmentListener mListener;
    private int mDialogId;

    public static DateTimePickerDialogFragment newInstance(int dialogId,
                                                           int titleResource, Calendar date) {
        DateTimePickerDialogFragment dialog = new DateTimePickerDialogFragment();

        Bundle args = new Bundle();
        args.putInt("id", dialogId);
        args.putInt("title", titleResource);
        args.putInt("year", date.get(Calendar.YEAR));
        args.putInt("month", date.get(Calendar.MONTH));
        args.putInt("day", date.get(Calendar.DAY_OF_MONTH));
        dialog.setArguments(args);

        return dialog;
    }

    public DateTimePickerDialogFragment() {
        super();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (DateDialogFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DateDialogFragmentListener");
        }
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialogId = getArguments().getInt("id");
        int title = getArguments().getInt("title");
        int year = getArguments().getInt("year");
        int month = getArguments().getInt("month");
        int day = getArguments().getInt("day");

        DatePickerDialog dlg = new DatePickerDialog(getActivity(),
                dateSetListener, year, month, day);

        DatePicker datePicker = dlg.getDatePicker();
        datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        dlg.setTitle(title);
        return dlg;
    }

    public void setDateDialogFragmentListener(
            DateDialogFragmentListener listener) {
        mListener = listener;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            if (mListener != null) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                mListener.dateDialogFragmentDateSet(mDialogId, newDate);
            }
        }
    };
}
