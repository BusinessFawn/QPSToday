package com.littlehouse_design.qpstoday;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.app.FragmentTransaction;
import android.app.ActionBar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity  implements ActionBar.TabListener{

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String hostName;
    private int portNumber;
    private static String staticHost;
    private static int staticPort;
    //static EditText editTextIP, editTextPort;
    private boolean connectedToServer = false;
    //static String[] connectionStatusWords = {"Status: Not Connected", "Status: Connected"};
    private DBHandler dbHandler = new DBHandler(this);

    static ButtonChanger changerOne;
    static ButtonChanger changerTwo;
    static ButtonChanger changerThree;
    static ButtonChanger changerFour;
    private String submitStat;
    private static String[] tabs = {"Sending", "Settings"};

    private TCPClient mTcpClient;

    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

        final ActionBar actionBar = getActionBar();

        actionBar.setHomeButtonEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        changerOne = new ButtonChanger(0);
        changerTwo = new ButtonChanger(1);
        changerThree = new ButtonChanger(2);
        changerFour = new ButtonChanger(3);

        if(!"Not Set".equals(dbHandler.getIpAddress())) {
            hostName = dbHandler.getIpAddress();
            staticHost = hostName;
        }
        else {
            hostName = "this";
            //staticHost = hostName;
        }

        if(dbHandler.getPort() > 0) {
            portNumber = dbHandler.getPort();
            staticPort = portNumber;
        }
        else {
            portNumber = 123;
            //staticPort = portNumber;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    static public void connectionStatus(int status) {
        //connectionStatus.setText(connectionStatusWords[status]);
    }



    public class connectTask extends AsyncTask<String,String,TCPClient> {

        @Override
        protected TCPClient doInBackground(String... message) {

            //we create a TCPClient object and
            mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
                @Override
                //here the messageReceived method is implemented
                public void messageReceived(String message) {
                    //this method calls the onProgressUpdate
                    publishProgress(message);
                }
            }, hostName, portNumber);
            mTcpClient.run(submitStat);

            return null;
        }
        //This is my listener for the server to say something back.
        // Currently, I don't do anything with it but print it to the log.
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Log.e("Server says","Server sent something back!" + values);
        }
    }

    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    // Here is where the meat of the app lives. Create and Send to the server.
                    return new SendingSection();


                default:
                    // Set IP and Port here
                    return new SettingsSection();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }

    /**
     * A fragment that launches other parts of the demo application.
     */
    public static class SendingSection extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.sending_action, container, false);

            final ImageView buttonOne = (ImageView) rootView.findViewById(R.id.button_one);
            buttonOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changerOne.changeStatus();
                    buttonOne.setImageResource(changerOne.getImage());


                }
            });
            final ImageView buttonTwo = (ImageView) rootView.findViewById(R.id.button_two);
            buttonTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changerTwo.changeStatus();
                    buttonTwo.setImageResource(changerTwo.getImage());
                }
            });


            final ImageView buttonThree = (ImageView) rootView.findViewById(R.id.button_three);
            buttonThree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changerThree.changeStatus();
                    buttonThree.setImageResource(changerThree.getImage());
                }
            });
            final ImageView buttonFour = (ImageView) rootView.findViewById(R.id.button_four);
            buttonFour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changerFour.changeStatus();
                    buttonFour.setImageResource(changerFour.getImage());
                }
            });
            /*ImageView sendTCP = (ImageView) rootView.findViewById(R.id.send_tcp);
            sendTCP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    submitStat = changerOne.getStatus() + ", " + changerTwo.getStatus() + ", "
                            + changerThree.getStatus() + ", " + changerFour.getStatus();
                    //If the client connection has been initiated already, don't try again. just send it.
                    if(mTcpClient == null) {
                        new connectTask().execute("");
                        connectedToServer = true;
                    } else if(connectedToServer){
                        mTcpClient.sendMessage(submitStat);
                    }

                }
            });*/


            return rootView;
        }
    }
    public void sendMessage(View view) {
        submitStat = changerOne.getStatus() + ", " + changerTwo.getStatus() + ", "
                + changerThree.getStatus() + ", " + changerFour.getStatus();
        //If the client connection has been initiated already, don't try again. just send it.
        if(mTcpClient == null) {
            new connectTask().execute("");
            connectedToServer = true;
        } else if(connectedToServer){
            mTcpClient.sendMessage(submitStat);
        }
    }

    public static class SettingsSection extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.setting_action, container, false);
            final EditText editTextIP = (EditText) rootView.findViewById(R.id.host_name);
            if(staticHost != null) {
                editTextIP.setText(staticHost);
            }
            System.out.println(staticHost);
            final EditText editTextPort = (EditText) rootView.findViewById(R.id.port_number);
            if(staticPort > 0) {
                editTextPort.setText(String.valueOf(staticPort));
            }
            System.out.println(staticPort);


            return rootView;
        }
    }
    public void settingsUpdate(View view) {
        EditText editTextIP = (EditText) findViewById(R.id.host_name);
        EditText editTextPort = (EditText) findViewById(R.id.port_number);
        if (editTextIP.getText().toString().length() < 1
                || editTextPort.getText().toString().length() < 1) {

            Toast.makeText(this, "Input for IP and Port are required", Toast.LENGTH_LONG).show();
            //dbHandler.dropDB();
        } else {
            hostName = editTextIP.getText().toString();
            portNumber = Integer.parseInt(editTextPort.getText().toString());
            dbHandler.addIP(hostName, portNumber);
            System.out.println(hostName + ":" + portNumber);
            Toast.makeText(this, "Updated IP/Port settings" + hostName + ":" + portNumber,Toast.LENGTH_LONG).show();

            System.out.println(dbHandler.getIpAddress() + " : " + dbHandler.getPort());

        }
    }
}
