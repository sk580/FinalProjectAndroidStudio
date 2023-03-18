package com.example.nasaiotd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DateSelectedListener callback;

    public DatePickerFragment(DateSelectedListener callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String date = getArguments().getString("date");
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(5, 7));
        int day = Integer.valueOf(date.substring(8, 10));

        return new DatePickerDialog(getActivity(), this, year, month - 1, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String dateString = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
        callback.onDateSelected(dateString);
    }

    public interface DateSelectedListener {
        void onDateSelected(String date);
    }
}
