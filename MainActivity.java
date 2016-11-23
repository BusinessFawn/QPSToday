package com.littlehouse_design.qpstoday;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String hostName;
    private int portNumber;
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button insidePopupButton;
    ImageView submitTcp;
    EditText editTextIP, editTextPort;
    static TextView connectionStatus;
    static String[] connectionStatusWords = {"Status: Not Connected", "Status: Connected"};
    private DBHandler dbHandler = new DBHandler(this);

    ButtonChanger changerOne;
    ButtonChanger changerTwo;
    ButtonChanger changerThree;
    ButtonChanger changerFour;
    String submitStat;

    private TCPClient mTcpClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changerOne = new ButtonChanger(0);
        changerTwo = new ButtonChanger(1);
        changerThree = new ButtonChanger(2);
        changerFour = new ButtonChanger(3);
        final ImageView buttonOne = (ImageView) findViewById(R.id.button_one);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerOne.changeStatus();
                buttonOne.setImageResource(changerOne.getImage());


            }
        });
        final ImageView buttonTwo = (ImageView) findViewById(R.id.button_two);
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerTwo.changeStatus();
                buttonTwo.setImageResource(changerTwo.getImage());
            }
        });


        final ImageView buttonThree = (ImageView) findViewById(R.id.button_three);
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerThree.changeStatus();
                buttonThree.setImageResource(changerThree.getImage());
            }
        });
        final ImageView buttonFour = (ImageView) findViewById(R.id.button_four);
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerFour.changeStatus();
                buttonFour.setImageResource(changerFour.getImage());
            }
        });
        ImageView sendTCP = (ImageView) findViewById(R.id.send_tcp);
        sendTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new connectTask().execute("");
                submitStat = changerOne.getStatus() + ", " + changerTwo.getStatus() + ", "
                        + changerThree.getStatus() + ", " + changerFour.getStatus();

                System.out.println("This happened " + submitStat);

//                if (mTcpClient != null)
//                    mTcpClient.sendMessage(submitStat);
                    //Do we want to stop communication after we send the first single?

                    //mTcpClient.stopClient();


            }
        });

        ImageView submitTCP = (ImageView) findViewById(R.id.connect_tcp);
        submitTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();
        popupInit();

        connectionStatus = (TextView) findViewById(R.id.connection_status);
        connectionStatus(0);
    }



    public void init() {
        submitTcp = (ImageView) findViewById(R.id.connect_tcp);
        editTextIP = new EditText(this);
        editTextPort = new EditText(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        editTextIP.setPadding(0, 25, 0, 0);
        insidePopupButton.setText("Send it!");
        if(!"Not Set".equals(dbHandler.getIpAddress()))
            hostName = dbHandler.getIpAddress();
            editTextIP.setText(hostName);
        editTextIP.setHint("127.0.0.1");
        editTextIP.setInputType(InputType.TYPE_CLASS_PHONE);
        if(dbHandler.getPort() > 0)
            portNumber = dbHandler.getPort();
            editTextPort.setText(String.valueOf(portNumber));
        editTextPort.setHint("8000");
        editTextPort.setInputType(InputType.TYPE_CLASS_NUMBER);
        layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
        layoutOfPopup.addView(editTextIP);
        layoutOfPopup.addView(editTextPort);
        layoutOfPopup.addView(insidePopupButton);
        layoutOfPopup.setBackgroundColor(getResources().getColor(R.color.white));

    }
    public void popupInit() {
        submitTcp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMessage.showAtLocation(view, Gravity.CENTER, 0, 60);
                popupMessage.setFocusable(true);
                popupMessage.update();
                submitStat = changerOne.getStatus() + ", " + changerTwo.getStatus() + ", "
                        + changerThree.getStatus() + ", " + changerFour.getStatus();

            }
        });
        insidePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextIP.getText().toString().length() < 1
                        || editTextPort.getText().toString().length() < 1) {

                    Toast.makeText(getApplicationContext(),"Input for IP and Port are required", Toast.LENGTH_LONG).show();
                } else {
                    hostName = editTextIP.getText().toString();
                    portNumber = Integer.parseInt(editTextPort.getText().toString());
                    dbHandler.addIP(hostName,portNumber);
                    popupMessage.dismiss();
                }
            }
        });
        popupMessage = new PopupWindow(layoutOfPopup,
                ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
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
}
