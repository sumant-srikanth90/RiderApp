package in.droom.riderapp.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import in.droom.riderapp.R;
import in.droom.riderapp.util.AppConstants;
import in.droom.riderapp.util.GlobalMethods;

public class ChatActivity extends AppCompatActivity {

    View ll_connect, ll_msg;
    NestedScrollView sv_output;
    TextView tv_status, tv_output;
    EditText et_msg, et_ip, et_port;
    Button btn_connect, btn_send;

    boolean isReceiving, timerFinished;
    int awaitingAck = 0;

    CountDownTimer timer;

    SimpleDateFormat timeFormat = new SimpleDateFormat("H:mm:ss");

    final int ACK_NONE = 0, ACK_CONNECT = 1, ACK_MESSAGE = 2, ACK_DISCONNECT = 3;
    final int SOCKET_TIMEOUT = 5000;

    private Socket socket;

    Thread receiverThread = new Thread() {

        @Override
        public void run() {

            while (true) {
                if (isReceiving || awaitingAck == ACK_CONNECT) {
                    final String output = readStream();

                    System.out.println("output == " + output);

                    if (output != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (awaitingAck != ACK_NONE) {

                                    switch (awaitingAck) {
                                        case ACK_CONNECT:
                                            updateUIForConnect();
                                            tv_status.setText("Successfully connected!");
                                            break;

                                        case ACK_MESSAGE:
                                            updateUIForMessage();
                                            et_msg.setText("");
                                            tv_status.setText("Last nessage sent " + timeFormat.format(Calendar.getInstance().getTime()));
                                            break;

                                        case ACK_DISCONNECT:
                                            updateUIForDisconnect();
                                            tv_status.setText("Not connected");
                                            break;
                                    }
                                    awaitingAck = ACK_NONE;
                                }
                                updateOutput(output);
                            }
                        });
                    }

                    if (!socket.isConnected() && awaitingAck != ACK_DISCONNECT) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new SocketDisconnect().execute();
                            }
                        });
                    }
                }

                try {
                    sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ll_connect = findViewById(R.id.ll_connect);
        ll_msg = findViewById(R.id.ll_msg);

        sv_output = (NestedScrollView) findViewById(R.id.sv_output);

        et_ip = (EditText) findViewById(R.id.et_ip);
        et_port = (EditText) findViewById(R.id.et_port);

        btn_connect = (Button) findViewById(R.id.btn_connect);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setEnabled(false);

        et_msg = (EditText) findViewById(R.id.et_msg);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_output = (TextView) findViewById(R.id.et_output);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_msg.getText().toString().equalsIgnoreCase("")) {
                    try {
                        new sendMessage().execute();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        updateOutput(ex.getMessage());
                    }
                } else
                    Toast.makeText(ChatActivity.this, "Message cannot be empty!", Toast.LENGTH_LONG).show();
            }
        });

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Connect
                if (btn_connect.getText().toString().equalsIgnoreCase(getString(R.string.connect))) {

                    if (et_ip.getText().length() > 0 && et_port.getText().length() > 0) {
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_SERVER_IP, et_ip.getText().toString(), GlobalMethods.STRING);
                        GlobalMethods.saveToPrefs(AppConstants.PREFS_SERVER_PORT, et_ip.getText().toString(), GlobalMethods.STRING);

                        new SocketConnect().execute();
                    } else
                        Toast.makeText(ChatActivity.this, "Invalid IP/Port entered!", Toast.LENGTH_LONG);

                } else {
                    // Disconnect
                    new SocketDisconnect().execute();
                }
            }
        });

        // Init inputs
        String saved_ip = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_SERVER_IP, GlobalMethods.STRING);
        if (saved_ip == null)
            et_ip.setText(AppConstants.SERVER_IP);
        else
            et_ip.setText(saved_ip);

        String saved_port = (String) GlobalMethods.getFromPrefs(AppConstants.PREFS_SERVER_PORT, GlobalMethods.STRING);
        if (saved_port == null)
            et_port.setText(AppConstants.SERVER_PORT);
        else
            et_port.setText(saved_port);
        et_port.setText("443");

        et_msg.setEnabled(false);
        btn_send.setEnabled(false);

        ll_connect.setVisibility(View.VISIBLE);
        ll_msg.setVisibility(View.GONE);
        tv_status.setText("Not connected");

        receiverThread.start();
    }

    class SocketConnect extends AsyncTask<Void, Void, Void> {

        String ip;
        int port;

        boolean isValid;

        @Override
        protected void onPreExecute() {
            GlobalMethods.showLoadingDialog(ChatActivity.this);
            btn_connect.setEnabled(false);

            try {
                ip = et_ip.getText().toString();
                port = Integer.parseInt(et_port.getText().toString());
                isValid = true;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (isValid) {
                try {
                    InetAddress serverAddr = InetAddress.getByName(ip);
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(serverAddr, port), SOCKET_TIMEOUT);

                    awaitingAck = ACK_CONNECT;

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {

            if (awaitingAck == ACK_CONNECT) {
                startTimer();
            } else {
                awaitingAck = ACK_NONE;

                updateUIForDisconnect();
                tv_status.setText("Failed to Connect!");
            }
        }
    }

    class SocketDisconnect extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            GlobalMethods.showLoadingDialog(ChatActivity.this);
            btn_connect.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF("*shutdown*");
                out.flush();

                awaitingAck = ACK_DISCONNECT;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {

            if (awaitingAck == ACK_DISCONNECT) {
                startTimer();
            } else {
                awaitingAck = ACK_NONE;

                updateUIForDisconnect();
                tv_status.setText("Not connected");
            }
        }
    }

    class sendMessage extends AsyncTask<Void, Void, Void> {

        String input;
        DataOutputStream out;

        @Override
        protected void onPreExecute() {
            input = et_msg.getText().toString();
            et_msg.setEnabled(false);
            btn_send.setEnabled(false);

            tv_status.setText("Sending message...");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Send message
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(input);
                out.flush();

                awaitingAck = ACK_MESSAGE;

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void o) {

            if (awaitingAck != ACK_MESSAGE) {
                updateUIForMessage();
                tv_status.setText("Message Failed!");
            }
        }
    }

    void updateUIForDisconnect() {
        btn_connect.setText(getString(R.string.connect));
        btn_send.setEnabled(false);
        et_msg.setEnabled(false);

        ll_msg.setVisibility(View.GONE);
        ll_connect.setVisibility(View.VISIBLE);

        btn_connect.setEnabled(true);

        isReceiving = false;

        if (socket != null) {
            try {
                socket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        GlobalMethods.hideLoadingDialog(ChatActivity.this);
        stopTimer();
    }

    void updateUIForConnect() {
        btn_connect.setText(getString(R.string.disconnect));
        btn_send.setEnabled(true);
        et_msg.setEnabled(true);

        ll_msg.setVisibility(View.VISIBLE);
        ll_connect.setVisibility(View.GONE);

        btn_connect.setEnabled(true);

        isReceiving = true;

        GlobalMethods.hideLoadingDialog(ChatActivity.this);
        stopTimer();
    }

    void updateUIForMessage() {
        et_msg.setEnabled(true);
        btn_send.setEnabled(true);
    }

    void startTimer() {
        timer = new CountDownTimer(SOCKET_TIMEOUT, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (awaitingAck == ACK_CONNECT) {
                    updateUIForDisconnect();
                    tv_status.setText("Failed to connect!");
                } else if (awaitingAck == ACK_DISCONNECT) {
                    updateUIForDisconnect();
                    tv_status.setText("Not connected");
                }
            }
        };
        timer.start();
    }

    void stopTimer() {
        if (timer != null)
            timer.cancel();
    }

    String readStream() {
        try {
            InputStream in = socket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(in), 1024);
            StringBuilder sb = new StringBuilder();
            char[] buffer = new char[1024];
            int read = 0;

            while ((read = br.read(buffer, 0, buffer.length)) > 0) {
                sb.append(buffer, 0, read);
                if (sb.toString().endsWith("\0"))
                    break;
            }

            return sb.deleteCharAt(0).toString().trim();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    void updateOutput(String txt) {
        tv_output.setText(txt + "\n" + tv_output.getText().toString());
        sv_output.fullScroll(ScrollView.FOCUS_UP);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {

        if (btn_connect.getText().toString().equalsIgnoreCase(getString(R.string.connect)))
            super.onBackPressed();
        else {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            ChatActivity.super.onBackPressed();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            dialog.dismiss();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
            builder.setTitle("Confirm")
                    .setMessage("You are currently connected. Are you sure you want to go back?")
                    .setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener)
                    .show();
        }
    }

}
