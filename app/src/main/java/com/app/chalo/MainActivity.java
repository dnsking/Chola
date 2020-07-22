package com.app.chalo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.chalo.networking.actions.AddContentNetworkAction;
import com.app.chalo.networking.actions.GetContentNetworkAction;
import com.app.chalo.networking.actions.InviteNetworkAction;
import com.app.chalo.networking.authentication.GivderContentHelper;
import com.app.chalo.utils.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static android.Manifest.permission.CALL_PHONE;
import static android.Manifest.permission.READ_CALL_LOG;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private View addUsersBtn,newPubBtn;
    private static final int CONTACT_PICKER_REQUEST = 11;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        newPubBtn = findViewById(R.id.newPubBtn);
        addUsersBtn = findViewById(R.id.addUsersBtn);


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_outline_radio_24).setText("Publications"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_outline_people_24).setText("Subscribers"));


        viewPager.setAdapter(new Adapter());
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==0){

                    newPubBtn.setVisibility(View.VISIBLE);
                    addUsersBtn.setVisibility(View.GONE);
                }
                else{
                    newPubBtn.setVisibility(View.GONE);
                    addUsersBtn.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        addUsersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContacts();
            }
        });
        newPubBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,NewPubActivity.class));
            }
        });
        loadData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CONTACT_PICKER_REQUEST){
            if(resultCode == RESULT_OK) {
                List<ContactResult> results = MultiContactPicker.obtainResult(data);
                final ArrayList<String> users = new ArrayList<>();
                for(ContactResult number:results){
                    users.add(number.getPhoneNumbers().get(0).getNumber());
                }

                Snackbar.make(viewPager,"Users Invited",Snackbar.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try{

                            GivderContentHelper.AddContent(MainActivity.this,
                                    new InviteNetworkAction(users.toArray(new String[]{}),
                                            Utils.GetUserName(MainActivity.this),
                                            Utils.GetString(App.ChannelName,MainActivity.this)));
                        }
                        catch (Exception ex){

                        }
                    }
                }).start();
               // results.get()
              //  Log.d("MyTag", results.get(0).getDisplayName());
            } else if(resultCode == RESULT_CANCELED){
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }
    private void startContactPicking(){

        new MultiContactPicker.Builder(MainActivity.this)
                .showTrack(true) //Optional - default: true
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) //Optional - default: White
                .setTitleText("Select Contacts") //Optional - default: Select Contacts
                .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                        android.R.anim.fade_in,
                        android.R.anim.fade_out) //Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST);
    }
    private void pickContacts(){
        Nammu.askForPermission(MainActivity.this, new String[]{
                READ_CONTACTS}, new PermissionCallback() {
            @Override
            public void permissionGranted() {
                startContactPicking();
            }

            @Override
            public void permissionRefused() {

            }
        });
    }
    private void loadData(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    AddContentNetworkAction[] addContentNetworkAction=
                            new Gson().fromJson(
                                    GivderContentHelper.AddContent(MainActivity.this,new GetContentNetworkAction(Utils.GetString(App.ChannelName,MainActivity.this))),
                                    AddContentNetworkAction[].class );
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }


    private class Adapter extends PagerAdapter {


        public Adapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position) {
            RecyclerView recyclerView = new RecyclerView(MainActivity.this);
            collection.addView(recyclerView);

            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "Image "+position;
        }
    }
}