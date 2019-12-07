package com.example.unmadesai.kardiacare4;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by User on 12/21/2016.
 */

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";

    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;


   // private FirebaseDatabase mFirebaseDatabase;
   // private DatabaseReference mMessagesDatabaseReference;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {

        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            //talk about this is in the 3rd
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }

    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID_INSECURE );
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }

            //will talk about this in the 3rd video
            connected(mmSocket,mmDevice);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**
     AcceptThread starts and sits waiting for a connection.
     Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device,UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){

            //mFirebaseDatabase = FirebaseDatabase.getInstance();
            // mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("messages");

            byte[] buffer = new byte[1024];  // buffer store for the stream

            int bytes; // bytes returned from read()
            String str=" ";

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {

                    bytes = mmInStream.read(buffer);
                    String incomingMessage = new String(buffer,0,bytes);
                   // if(incomingMessage.equalsIgnoreCase(str)==true) {
                        //int value=Integer.parseInt(incomingMessage);
                        //mMessagesDatabaseReference.push().setValue(incomingMessage);
                        //  int heartrate=Integer.parseInt(incomingMessage);

                        bytes = mmInStream.read(buffer);
                        String incomingMessage2 = new String(buffer, 0, bytes);
                        incomingMessage2= incomingMessage2.replaceAll("^\\s+|\\s+|\\s+$", "");
                        // incomingMessage2=incomingMessage2.trim();
                        if(incomingMessage2==""){
                            Log.d(TAG, "InputStream heart rate: " + incomingMessage2);
                        }else {
                            if (Integer.parseInt(incomingMessage2) < 1000) {
                                Log.d(TAG, "InputStream heart rate: " + incomingMessage2);


                            }
                        }
                        //int value=Integer.parseInt(incomingMessage);
                        //mMessagesDatabaseReference.push().setValue(incomingMessage);
                        // int spo2=Integer.parseInt(incomingMessage2);

                        bytes = mmInStream.read(buffer);
                        String incomingMessage3 = new String(buffer, 0, bytes);
                        incomingMessage3= incomingMessage3.replaceAll("^\\s+|\\s+|\\s+$", "");
                        if(incomingMessage3==""){
                            Log.d(TAG, "InputStream spo2: " + incomingMessage3);
                        }else {
                            if (Integer.parseInt(incomingMessage3) < 100) {
                                Log.d(TAG, "InputStream spo2: " + incomingMessage3);
                            }
                        }
                        //int value=Integer.parseInt(incomingMessage);
                        //mMessagesDatabaseReference.push().setValue(incomingMessage);
                        String Heartrate = incomingMessage2;
                        String Spo2 = incomingMessage3;
                        DataBaseOperations1 db = new DataBaseOperations1(mContext

                        db.putInformation(db, Heartrate, Spo2, "1", "1");
                        Log.d("Insert function", "insert sqlite bluetooth");


                    //}
                    Log.d("Toast function", "Toast timer");
                    DataBaseOperations1 db1=new DataBaseOperations1(mContext);
                    Cursor c = db1.getInformation(db1);
                           if (c.moveToFirst()) {
                                do {
                                    Log.d("heartrate",c.getString(0)+"\n" + "spo2 "+c.getString(1)+"\n");
                                } while (c.moveToNext());
                            }
                    // Log.d("in retrieve","retrieve successful");
                    Cursor crs=db1.select();
                    crs.moveToFirst();
                    String q=crs.getString(0);
                    Log.d("avg",""+q);
                    //break;
                    //Intent i=new Intent(mContext,MapsActivity.class);
                    //mContext.startActivity(i);


                    db1.close();
                   // break;
                    //Log.d(TAG, "InputStream skip: " + incomingMessage);
                /*    Timer timer=new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        private Handler updateUI = new Handler(){
                            @Override
                            public void publish(LogRecord record) {

                            }

                            @Override
                            public void flush() {

                            }

                            @Override
                            public void close() throws SecurityException {

                            }

                        };
                        public void run() {
                            try {
                                Log.d("Toast function", "Toast timer");
                                DataBaseOperations1 db=new DataBaseOperations1(mContext);
                                Cursor c = db.getInformation(db);
                            if (c.moveToFirst()) {
                                do {
                                    Log.d("heartrate",c.getString(0)+"\n" + "spo2 "+c.getString(1)+"\n");
                                } while (c.moveToNext());
                            }
                                // Log.d("in retrieve","retrieve successful");
                                Cursor crs=db.select();
                                crs.moveToFirst();
                                String q=crs.getString(0);
                                Log.d("avg",""+q);
                                //break;

                                Intent i=new Intent(mContext,MapsActivity.class);
                                mContext.startActivity(i);

                           // boolean s=db.delete();

                                db.close();
                            } catch (Exception e) {e.printStackTrace(); }
                        }
                    }, 20000,90000);
                    timer.cancel();
                    //break;
*/
                } catch (IOException e) {
                                                          Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage());
                                                          break;
                                                      }
                                                  }
                                              }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }

}
