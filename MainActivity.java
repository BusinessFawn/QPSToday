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



public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private String hostName;
    private int portNumber;
    LinearLayout layoutOfPopup;
    PopupWindow popupMessage;
    Button submitTcp, insidePopupButton;
    EditText editTextIP, editTextPort;

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
        final TextView textOne = (TextView) findViewById(R.id.title_one);
        textOne.setText(changerOne.getTextStatus());
        buttonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerOne.changeStatus();
                textOne.setText(changerOne.getTextStatus());
                buttonOne.setImageResource(changerOne.getImage());


            }
        });
        final ImageView buttonTwo = (ImageView) findViewById(R.id.button_two);
        final TextView textTwo = (TextView) findViewById(R.id.title_two);
        textTwo.setText(changerTwo.getTextStatus());
        buttonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerTwo.changeStatus();
                textTwo.setText(changerTwo.getTextStatus());
                buttonTwo.setImageResource(changerTwo.getImage());
            }
        });


        final ImageView buttonThree = (ImageView) findViewById(R.id.button_three);
        final TextView textThree = (TextView) findViewById(R.id.title_three);
        textThree.setText(changerThree.getTextStatus());
        buttonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerThree.changeStatus();
                textThree.setText(changerThree.getTextStatus());
                buttonThree.setImageResource(changerThree.getImage());
            }
        });
        final ImageView buttonFour = (ImageView) findViewById(R.id.button_four);
        final TextView textFour = (TextView) findViewById(R.id.title_four);
        textFour.setText(changerFour.getTextStatus());
        buttonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changerFour.changeStatus();
                textFour.setText(changerFour.getTextStatus());
                buttonFour.setImageResource(changerFour.getImage());
            }
        });
        Button sendTCP = (Button) findViewById(R.id.send_tcp);
        sendTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String submitStat = changerOne.getStatus() + ", " + changerTwo.getStatus() + ", "
                        + changerThree.getStatus() + ", " + changerFour.getStatus();

                if (mTcpClient != null) {
                    mTcpClient.sendMessage(submitStat);
                    //Do we want to stop communication after we send the first single?

                    //mTcpClient.stopClient();
                }

            }
        });

        Button submitTCP = (Button) findViewById(R.id.connect_tcp);
        submitTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        init();
        popupInit();
    }



    public void init() {
        submitTcp = (Button) findViewById(R.id.connect_tcp);
        editTextIP = new EditText(this);
        editTextPort = new EditText(this);
        insidePopupButton = new Button(this);
        layoutOfPopup = new LinearLayout(this);
        editTextIP.setPadding(0, 25, 0, 0);
        insidePopupButton.setText("Send it!");
        editTextIP.setHint("127.0.0.1");
        editTextIP.setInputType(InputType.TYPE_CLASS_PHONE);
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
                    hostName = "192.168.1.73";
                    portNumber = 8000;
                } else {
                    hostName = editTextIP.getText().toString();
                    portNumber = Integer.parseInt(editTextPort.getText().toString());
                }
                new connectTask().execute("");

                Log.e("TCP Client", "Submitting to server at " + hostName + ":" + portNumber);
                popupMessage.dismiss();

            }
        });
        popupMessage = new PopupWindow(layoutOfPopup,
                ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        popupMessage.setContentView(layoutOfPopup);
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
            mTcpClient.run();

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            System.out.println("Server sent something back!" + values);
        }
    }
}
