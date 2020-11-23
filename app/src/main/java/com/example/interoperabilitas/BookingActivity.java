package com.example.interoperabilitas;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BookingActivity extends AppCompatActivity {
    TextView waktuMasuk;
    TextView waktuSelesai;
    TextView confirmReservation;
    public static ArrayList<Integer> idButtonMasukDisabled = new ArrayList<>();
    public static ArrayList<Integer> idButtonKeluarEnabled = new ArrayList<>();
    public static ArrayList<Integer> seluruhWaktuSibuk = new ArrayList<>();
    public static Integer waktuMasukDipilih;
    public static Integer waktuKeluarMax;
    Context context;
    Integer selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        selected=0;
        Ruangan ruanganTerpilih = MainActivity.ruangans.get(0);
        final ArrayList<InfoBooking> BookingRuanganArrayList = ruanganTerpilih.getBookings();
        waktuMasuk = (TextView) findViewById(R.id.BookingWaktuMasuk);
        waktuSelesai = (TextView) findViewById(R.id.BookingWaktuSelesai);
        confirmReservation = (TextView) findViewById(R.id.ConfirmTextView);
        context = this;

        DatePicker calendarView = findViewById(R.id.BookingCalendarView);
        calendarView.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                idButtonMasukDisabled.clear();
                seluruhWaktuSibuk.clear();
                waktuMasuk.setText("--:--");
                waktuSelesai.setText("--:--");
                selected=0;
                Log.i("Button yang disable", Integer.toString(dayOfMonth));
                for (InfoBooking variableName : BookingRuanganArrayList) {
                    if (variableName.getTahunBooking() == year) {
                        Log.i("Tahun", variableName.getTahunBooking().toString());
                        if (variableName.getBulanBooking() == monthOfYear) {
                            Log.i("Bulan", variableName.getBulanBooking().toString());
                            if (variableName.getTanggalBooking() == dayOfMonth) {
                                Log.i("Tanggal", variableName.getTanggalBooking().toString());
                                int waktuMulai = variableName.getWaktuMulai();
                                Resources res = getResources();
                                while (waktuMulai < variableName.getWaktuSelesai()) {
                                    Log.i("Waktu Selected", Integer.toString(waktuMulai));
                                    Log.i("Waktu Selesai", Integer.toString(variableName.getWaktuSelesai()));
                                    idButtonMasukDisabled.add(res.getIdentifier("button" + waktuMulai, "id", context.getPackageName()));
                                    seluruhWaktuSibuk.add(variableName.getWaktuMulai());
                                    waktuMulai++;
                                }
                            }

                        }

                    }
                }
            }
        });
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {

//        });

        waktuMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_booking);
                dialog.show();
                ArrayList<Button> buttons = new ArrayList<>();
                for (int i = 0; i <= 11; i++) {
                    Resources res = getResources();
                    int id = res.getIdentifier("button" + (i + 10), "id", dialog.getContext().getPackageName());
                    buttons.add((Button) dialog.findViewById(id));
                    final int jam = i;
                    buttons.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            idButtonKeluarEnabled.clear();
                            waktuMasuk.setText(jam + 10 + ":00");
                            BookingActivity.waktuMasukDipilih = jam+10;
                            Log.i("waktuMasukDipilih",Integer.toString(BookingActivity.waktuMasukDipilih));
                            if(BookingActivity.seluruhWaktuSibuk.size()!=0)
                            {
                                for (Integer waktusibuk: BookingActivity.seluruhWaktuSibuk)
                                {
                                    if (waktusibuk>BookingActivity.waktuMasukDipilih)
                                    {
                                        BookingActivity.waktuKeluarMax=waktusibuk;
                                        Log.i("waktuKeluarMax",Integer.toString(BookingActivity.waktuKeluarMax));
                                    }
                                    else
                                    {
                                        BookingActivity.waktuKeluarMax = 22;
                                        Log.i("waktuKeluarMax",Integer.toString(BookingActivity.waktuKeluarMax));
                                    }
                                }

                            } else
                                {
                                    BookingActivity.waktuKeluarMax = 22;
                                    Log.i("waktuKeluarMax",Integer.toString(BookingActivity.waktuKeluarMax));
                                }
                            for(int i = BookingActivity.waktuMasukDipilih; i< BookingActivity.waktuKeluarMax; i++)
                            {
                                Resources res = getResources();
                                int id = res.getIdentifier("button" + (i+1), "id", dialog.getContext().getPackageName());
                                idButtonKeluarEnabled.add(id);
                            }
                            selected=1;
                            dialog.dismiss();

                        }
                    });
                }
                for(Button buttonSelected: buttons)
                {
                    for(Integer idSelected: idButtonMasukDisabled)
                    {
                        if(buttonSelected.getId()==idSelected)
                        {
                            buttonSelected.setEnabled(false);
                            buttonSelected.setTextColor(getResources().getColor(R.color.cardview_light_background));
                        }

                    }

                }
            }
        });



        waktuSelesai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected>0)
                {
                    for(Integer integer:idButtonKeluarEnabled)
                    {
                        Log.i("ids", integer.toString());
                    }
                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.dialog_booking2);
                    dialog.show();
                    ArrayList<Button> buttons = new ArrayList<>();
                    for (int i = 0; i <= 11; i++) {
                        Resources res = getResources();
                        int id = res.getIdentifier("button" + (i + 11), "id", dialog.getContext().getPackageName());
                        buttons.add((Button) dialog.findViewById(id));
                        final int jam = i;
                        buttons.get(i).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                waktuSelesai.setText(jam + 11 + ":00");
                                selected=2;
                                dialog.dismiss();
                            }
                        });
                        buttons.get(i).setEnabled(false);
                        buttons.get(i).setTextColor(getResources().getColor(R.color.cardview_light_background));
                    }
                    for(Button buttonSelected: buttons)
                    {
                        for(Integer idSelected: idButtonKeluarEnabled)
                        {
                            if(buttonSelected.getId()==idSelected)
                            {
                                buttonSelected.setEnabled(true);
                                buttonSelected.setTextColor(getResources().getColor(R.color.colorPrimary));
                            }

                        }

                    }

                }
                else
                    {
                        Toast.makeText(BookingActivity.this, "Pilih waktu masuk terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }

            }
        });


        confirmReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected>1)
                {
                    int masuk = Integer.parseInt(waktuMasuk.getText().toString().substring(0, 2));
                    int selesai = Integer.parseInt(waktuSelesai.getText().toString().substring(0, 2));
                    if (masuk >= selesai) {
                        Toast.makeText(BookingActivity.this, "Invalid time format", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    {
                        Toast.makeText(BookingActivity.this, "Pilih waktu masuk dan keluar terlebih dahulu", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }
}
