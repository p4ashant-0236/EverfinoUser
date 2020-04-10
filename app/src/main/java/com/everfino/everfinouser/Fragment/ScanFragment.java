package com.everfino.everfinouser.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.everfino.everfinouser.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.Collections;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment {

    ZXingScannerView qrCodeScanner;
    Boolean isCameraOn = false;

    public ScanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment4
        View view = inflater.inflate(R.layout.fragment_scan, container, false);
        qrCodeScanner = view.findViewById(R.id.qrCodeScanner);

        Log.e("####", "OnCreate");
        setScannerProperties();
        qrCodeScanner.startCamera();
        isCameraOn = true;
        qrCodeScanner.setResultHandler(new ZXingScannerView.ResultHandler() {
            @Override
            public void handleResult(Result result) {
                String restid="",tableid="";
                int flag=0;
                for (char i:result.toString().toCharArray()) {
                        if(flag==0)
                        {
                            if(i!='_')
                            {
                                restid=restid+i;
                            }
                            else {
                                flag=1;
                            }
                        }
                        else
                        {
                            if(flag==1)
                            {   if(i!='_') {
                                tableid = tableid + i;
                            }else
                            {
                                break;
                            }
                            }
                        }
                }
                 Fragment fragment=new RestMenuFragment();
                Bundle b=new Bundle();
                b.putInt("restid",Integer.parseInt(restid));
                b.putInt("tableid",Integer.parseInt(tableid));
                fragment.setArguments(b);
                loadFragment(fragment);
            }
        });
        return view;
    }


    private void setScannerProperties() {
        qrCodeScanner.setFormats(Collections.singletonList((BarcodeFormat.QR_CODE)));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);

    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
