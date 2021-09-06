package com.example.walkaloop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.walkaloop.model.Response;
import com.example.walkaloop.model.SubPlaceItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private List<SubPlaceItem> list_places = new ArrayList<>();
    private FragmentActivity myContext;
    public View map_frag;
    CardView crd_view;
    TextView tv_place, tv_desc;
    boolean isUp;
    private APIInterface apiInterface;
    private Response callresponse;
    ImageView img_pic;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_maps, container, false);
        map_frag = view.findViewById(R.id.map);
        crd_view = view.findViewById(R.id.crd_view);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_place = view.findViewById(R.id.tv_place);
        img_pic = view.findViewById(R.id.img_pic);
        crd_view.setVisibility(View.INVISIBLE);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        Call<Response> apicall = apiInterface.getallDetails("1");
        apicall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                callresponse = response.body();
                list_places = callresponse.getSubPlace();
                SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(MapsActivity.this::onMapReady);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                t.getMessage();
            }
        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (isUp) {
                    onSlideViewButtonClick();
                }
            }
        });

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(Double.parseDouble(callresponse.getLatitude()), Double.parseDouble(callresponse.getLongitude()));
        mMap.setMinZoomPreference(10);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        PolylineOptions options = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);


        for (int i = 0; i <= list_places.size(); i++) {
            if (i != list_places.size()) {
                LatLng latlong = new LatLng(Double.parseDouble(list_places.get(i).getLatitude()), Double.parseDouble(list_places.get(i).getLongitude()));

                // below line is use to add marker to each location of our array list.
                mMap.addMarker(new MarkerOptions().position(latlong).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin)).title(list_places.get(i).getNpostion() + ""));

                // below lin is use to zoom our camera on map.
                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f));

                // below line is use to move our camera to the specific location.
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latlong));
                options.add(latlong);
                mMap.addPolyline(options);
                mMap.setOnMarkerClickListener(this);
            } else {
                LatLng latlong = new LatLng(Double.parseDouble(list_places.get(0).getLatitude()), Double.parseDouble(list_places.get(0).getLongitude()));
                options.add(latlong);
                mMap.addPolyline(options);
            }
        }


//        mMap.addMarker(new MarkerOptions().position(sydney).title("Pimp Hall Nature Reserve"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    // slide the view from below itself to the current position
    public void slideUp(View view) {
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                view.getHeight(),  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideDown(View view) {
        TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                0,                 // fromYDelta
                view.getHeight()); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick() {
        if (isUp) {
            slideDown(crd_view);
        } else {
            slideUp(crd_view);
        }
        isUp = !isUp;
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        if (isUp) {
            isUp = !isUp;
        }
        tv_place.setText(list_places.get(Integer.parseInt(marker.getTitle())).getStr_name());
        tv_desc.setText(list_places.get(Integer.parseInt(marker.getTitle())).getPlace_desc());
        byte[] decodedString = Base64.decode(list_places.get(Integer.parseInt(marker.getTitle())).getImg_encode(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img_pic.setImageBitmap(decodedByte);

        onSlideViewButtonClick();
        return true;
    }
}