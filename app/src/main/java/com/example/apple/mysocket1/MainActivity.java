package com.example.apple.mysocket1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class MainActivity extends AppCompatActivity implements Button.OnClickListener {

    Button btn,btn2;
    TextView tv;
    EditText ed;

    Thread thread;
    Socket clientSocket;
    BufferedWriter bw;
    BufferedReader br;
    String str;
    
    static WebSocketClient client;
    URI uri;
    String address;
    StringBuilder sb;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            sb.append("server: \n");
            sb.append(message.obj.toString());
            sb.append("\n");
            tv.setText(sb.toString());

            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button)findViewById(R.id.bt_connent);
        btn2 = (Button)findViewById(R.id.bt_send);
        tv = (TextView)findViewById(R.id.textView);
        ed = (EditText)findViewById(R.id.editText);

        tv.setMovementMethod(ScrollingMovementMethod.getInstance());

        sb = new StringBuilder();

        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.bt_connent:
                connect_websocket();
                Log.d("-----------", String.valueOf(client.isConnecting()));
                break;
            case R.id.bt_send:
                if (client.isClosed() || client.isClosing()) {
                    Toast.makeText(MainActivity.this,"Client正在關閉",Toast.LENGTH_SHORT).show();
                    client.connect();
                    break;
                }
                //webSocket套件提供的send Msg功能
                client.send(ed.getText().toString().trim());
                sb.append("client :");
                sb.append("\n");
                sb.append(ed.getText().toString().trim());
                sb.append("\n");
                tv.setText(sb.toString());
                ed.setText("");
                break;
            default:
                break;
        }

    }

    public void connect_websocket() {

        //address = "ws://121.40.165.18:8088";
        address = "ws://119.29.3.36:5354";
        //address = "ws://35.194.216.170:8080";
        try {
            uri  = new URI(address);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (null == client){

            client = new WebSocketClient(uri) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    Log.i("TAG","onOpen" +  handshakedata);
                    sb.append("server status: ");
                    sb.append(handshakedata.getHttpStatusMessage());
                    sb.append("\n");
                    tv.setText(sb.toString());
                }

                @Override
                public void onMessage(String message) {
                    Log.i("TAG","onMessage" + message);
                    Log.i("TAG","Fix Message :" + removeTag.remove(message));

                    Message handlerMessage = Message.obtain();
                    handlerMessage.obj = removeTag.remove(message);
                    handler.sendMessage(handlerMessage);
                    /*
                    sb.append(removeTag.remove(message) + "\n");
                    tv.append(sb);
                    */

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    Log.i("TAG","onClose");
                }

                @Override
                public void onError(Exception ex) {
                    Log.i("TAG","onError");
                }
            };
            client.connect();
            Log.d("-----------", String.valueOf(client.isConnecting()));

        }
    }

    //private void connect_socket() {
    //
    //    new Thread(new Runnable() {
    //        @Override
    //        public void run() {
    //            try {
    //                InetAddress serverIP = InetAddress.getByName("121.40.165.18");
    //                int serverPort = 8080;
    //
    //                clientSocket = new Socket(serverIP,serverPort);
    //                bw = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
    //                br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    //
    //                Log.d("isConnected test", clientSocket.isConnected()?"True":"False");
    //                while (clientSocket.isConnected()){
    //                    str = br.readLine();
    //                    if(str!=null){
    //                        tv.setText(str);
    //                    }
    //                }
    //
    //
    //            } catch (UnknownHostException e) {
    //                e.printStackTrace();
    //                Log.d("Error",e.toString());
    //            } catch (IOException e) {
    //                e.printStackTrace();
    //                Log.d("Error",e.toString());
    //            }
    //        }
    //    }).start();
    //}
}
