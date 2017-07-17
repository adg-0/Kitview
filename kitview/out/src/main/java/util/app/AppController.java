package util.app;

/**
 * Created by orthalis on 06/06/2017.
 */
import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import util.helper.XmlParser;

public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    public static XmlParser.Address practiceAddress;
    public static String practiceName;
    public static ArrayList<XmlParser.Doctor> practiceDoctors;
    public static XmlParser.OpeningHours practiceOpeningHours;
    public static XmlParser.KitviewServer practiceKitviewServer;
    public static String practiceText;
    public static XmlParser.Contact practiceContact;

    public void parseConfigFile(){
        String path = getApplicationContext().getFilesDir().getAbsolutePath() + File.separator + "Config" + File.separator + "kitpatient.xml";
        File fileConf = new File(path);
        if (fileConf.exists()) {
            XmlParser xml = null;
            try {
                xml = new XmlParser(path);
                practiceAddress = xml.getAddress();
                practiceName = xml.getName();
                practiceDoctors = xml.getDoctors();
                practiceOpeningHours = xml.getOpeningHours();
                practiceKitviewServer = xml.getKitviewServer();
                practiceText = xml.getText();
                practiceContact = xml.getContact();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException");
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                System.out.println("XmlPullParserException");
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        parseConfigFile();
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}