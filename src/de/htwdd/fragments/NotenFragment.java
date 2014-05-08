package de.htwdd.fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SimpleExpandableListAdapter;
import android.widget.TextView;

import de.htwdd.DatabaseHandlerNoten;
import de.htwdd.R;
import de.htwdd.Wizard1;
import de.htwdd.types.TNote;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class NotenFragment extends Fragment
{
    public SharedPreferences app_preferences;
    public String stg, stg2;
    public String abschl, abschl2;
    public DatabaseHandlerNoten db;
    public int count;
    public int count2;
    public int mode;

    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier()
    {
        public boolean verify(String hostname, SSLSession session)
        {
            return true;
        }
    };

    public NotenFragment(int i)
    {
        // TODO Auto-generated constructor stub
        mode = i;
    }

    public NotenFragment()
    {

    }

    public static String getStackTrace(Throwable aThrowable)
    {
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

    private static void trustAllHosts()
    {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
        {
            public java.security.cert.X509Certificate[] getAcceptedIssuers()
            {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException
            {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException
            {
            }
        }};

        // Install the all-trusting trust manager
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.noten, null);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        db = new DatabaseHandlerNoten(getActivity());
        app_preferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        getView().findViewById(R.id.error_text).setVisibility(View.GONE);
        getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
        getView().findViewById(R.id.wait).setVisibility(View.GONE);

        count = db.getAllNoten().size();
        //	Toast.makeText(getActivity(), count+"", Toast.LENGTH_LONG).show();


        if (mode == 0)
        {

            LinearLayout l = (LinearLayout) getView().findViewById(R.id.linearLayout2);
            l.setVisibility(View.GONE);
            ExpandableListView v = (ExpandableListView) getView().findViewById(R.id.noten);
            v.setVisibility(View.VISIBLE);

            display(null);


            if (app_preferences.getString("matnr", "").length() < 3 || app_preferences.getString("pw", "").length() < 3)
            {

                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity()).create();
                alertDialog2.setTitle("Fehlende Daten");
                alertDialog2.setMessage("Für die Notenanzeige müssen Matrikelnummer und Passwort eingetragen werden.\n\nSoll der Konfigurations-Assistent gestartet werden?");
                alertDialog2.setIcon(R.drawable.ic_launcher);
                alertDialog2.setButton2("nein", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        try
                        {
                            TextView error_text = (TextView) getView().findViewById(R.id.error_text);
                            getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
                            error_text.setText("Noten konnten nicht aktualisiert werden.");
                        } catch (Exception e)
                        {
                        }
                        ;
                    }
                });
                alertDialog2.setButton("Assistenten starten", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        Intent nextScreen = new Intent(getActivity(), Wizard1.class);
                        startActivity(nextScreen);
                        getActivity().finish();
                    }
                });
                alertDialog2.show();


            }
            else
            {

			 /*
	    			worker w = new worker();
     		w.execute(app_preferences.getString("matnr", "0"), app_preferences.getString("pw", "0"),app_preferences.getString("encoding", "UTF-8"),"0");
	    	*/
            }
        }
        if (mode == 1)
        {
            getView().findViewById(R.id.wait).setVisibility(View.VISIBLE);
            LinearLayout l = (LinearLayout) getView().findViewById(R.id.linearLayout2);
            l.setVisibility(View.GONE);
            getView().findViewById(R.id.error_text).setVisibility(View.VISIBLE);
            getView().findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
            ExpandableListView v = (ExpandableListView) getView().findViewById(R.id.noten);
            v.setVisibility(View.VISIBLE);

            worker w = new worker();
            w.execute(app_preferences.getString("matnr", "0"), app_preferences.getString("pw", "0"), app_preferences.getString("encoding", "UTF-8"), "0");


        }

    }

    private class worker extends AsyncTask<String, Void, String[]>
    {
        @Override
        protected String[] doInBackground(String... params)
        {
            TNote[] noten = null;
            TNote[] notenb = null;

            String line;
            String line2 = "";
            String line3 = "";
            String line4 = "";

            SharedPreferences.Editor editor = app_preferences.edit();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm ");
            Date currentTime = new Date();
            editor.putString("noten_date", formatter.format(currentTime));
            editor.commit(); // Very important

            try
            {

                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
                data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");
                data += "&" + URLEncoder.encode("submit", "UTF-8") + "=" + URLEncoder.encode(" OK ", "UTF-8");


                System.setProperty("http.keepAlive", "false");

                URL url = new URL("https://wwwqis.htw-dresden.de/qisserver/rds?state=user&type=1&category=auth.login&startpage=portal.vm");
                trustAllHosts();
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setUseCaches(false);
                conn.setHostnameVerifier(DO_NOT_VERIFY);
                conn.setInstanceFollowRedirects(false);
                conn.setDoOutput(true);


                //Connect to login-page and send login data
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
                wr.write(data);
                wr.flush();
                wr.close();


                //			Map<String, List<String>> headers=conn.getHeaderFields();
                //get cookies
                List<String> cookies = null;
                try
                {
                    cookies = conn.getHeaderFields().get("Set-Cookie");
                } catch (Exception e)
                {
                }
                ;
                if (cookies == null) cookies = conn.getHeaderFields().get("set-cookie");

                conn.disconnect();

                //connect to overview page
                url = new URL("https://wwwqis.htw-dresden.de/qisserver/rds?state=user&type=0&category=menu.browse&breadCrumbSource=&startpage=portal.vm");
                //trustAllHosts();
                conn = (HttpsURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setUseCaches(false);
                conn.setHostnameVerifier(DO_NOT_VERIFY);
                conn.setInstanceFollowRedirects(false);

                //Send cookies for identification
                for (String cookie : cookies)
                {
                    conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
                    conn.addRequestProperty("cookie", cookie.split(";", 2)[0]);
                }

                //			Map<String, List<String>> tf= conn.getRequestProperties();

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                while (!(line2.contains("</html>")))
                {
                    line = rd.readLine();
                    line2 += line;
                }
                // wr.close();
                rd.close();


                //If Login has failed, there is no asi link
                if (!line2.contains("asi="))
                {
                    //noten = new TNote[1];
                    //noten[0]=new TNote("\n\nFehler: Einloggen fehlgeschlagen - Passwort falsch?","einloggen geht nicht","","","","","","","", "");
                    String returnvalue[] = new String[3];
                    returnvalue[0] = "Fehler: Einloggen fehlgeschlagen - Matrikelnummer oder Passwort falsch";
                    returnvalue[1] = "empty";
                    returnvalue[2] = params[3];

                    return returnvalue;
                }

                //save asi value
                line2 = line2.substring(line2.indexOf("asi="));
                line2 = line2.substring(4, line2.indexOf('"')); //<------------------------------------------
                String asi = line2;

                //	https://wwwqis.htw-dresden.de/qisserver/rds?state=htmlbesch&moduleParameter=Student&menuid=notenspiegel&asi=3tlRm7ZLnOVZUdhs0bz$

                //Connect to get stuga (for diploma changing people)
                conn.disconnect();
                line2 = "";

                url = new URL("https://wwwqis.htw-dresden.de/qisserver/rds?state=htmlbesch&moduleParameter=Student&menuid=notenspiegel&asi=" + asi);
                //trustAllHosts();
                conn = (HttpsURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setUseCaches(false);
                conn.setHostnameVerifier(DO_NOT_VERIFY);
                conn.setInstanceFollowRedirects(false);

                //Send cookies for identification
                for (String cookie : cookies)
                {
                    conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
                }

                // Get the response
                rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                while (!(line2.contains("</html>")))
                {
                    line = rd.readLine();
                    line2 += line;
                }
                // wr.close();
                rd.close();

                stg = "";
                stg2 = "";
                abschl = "";
                abschl2 = "";
                try
                {
                    stg = line2.substring(line2.indexOf("&amp;stg=") + 9, line2.indexOf("&amp;abschl"));
                    abschl = line2.substring(line2.indexOf("&amp;abschl=") + 12, line2.indexOf("&amp;next=list.vm"));

                    line2 = line2.substring(line2.indexOf("&amp;next=list.vm") + 17);

                    stg2 = line2.substring(line2.indexOf("&amp;stg=") + 9, line2.indexOf("&amp;abschl"));
                    abschl2 = line2.substring(line2.indexOf("&amp;abschl=") + 12, line2.indexOf("&amp;next=list.vm"));

                } catch (Exception e)
                {
                }
                ;


                //Connect to real page 1
                url = new URL("https://wwwqis.htw-dresden.de/qisserver/rds?state=htmlbesch&stg=" + stg + "&abschl=" + abschl + "&next=list.vm&asi=" + asi); //this adds the asi as well
                //trustAllHosts();
                conn = (HttpsURLConnection) url.openConnection();
                conn.setConnectTimeout(30000);
                conn.setUseCaches(false);
                conn.setHostnameVerifier(DO_NOT_VERIFY);
                conn.setInstanceFollowRedirects(false);
                //send cookies
                for (String cookie : cookies)
                {
                    conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
                }
                //establish connection, read stream
                rd = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));

                while (!(line3.contains("</html>")))
                {
                    line = rd.readLine();
                    line3 += line;
                }
                // wr.close();
                rd.close();


                //Connect to real page 2
                if (stg2.length() > 1)
                {
                    url = new URL("https://wwwqis.htw-dresden.de/qisserver/rds?state=htmlbesch&stg=" + stg2 + "&abschl=" + abschl2 + "&next=list.vm&asi=" + asi); //this adds the asi as well
                    //trustAllHosts();
                    conn = (HttpsURLConnection) url.openConnection();
                    conn.setConnectTimeout(30000);
                    conn.setUseCaches(false);
                    conn.setHostnameVerifier(DO_NOT_VERIFY);
                    conn.setInstanceFollowRedirects(false);
                    //send cookies
                    for (String cookie : cookies)
                    {
                        conn.addRequestProperty("Cookie", cookie.split(";", 2)[0]);
                    }
                    //establish connection, read stream
                    rd = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));

                    while (!(line4.contains("</html>")))
                    {
                        line = rd.readLine();
                        line4 += line;
                    }
                    // wr.close();
                    rd.close();

                }


                line3 = line3.substring(line3.indexOf("ffentlichungsdatum"));
                line3 = line3.substring(0, line3.indexOf("Hinweis"));

                String tokens[] = line3.split("</tr>");

                noten = new TNote[tokens.length - 2];

                for (int i = 1; i < tokens.length - 1; i++)
                {

                    String tokens2[] = tokens[i].split("class=\"posrecords\"");

                    //Replace bad characters
                    for (int b = 1; b < tokens2.length; b++)
                    {
                        tokens2[b] = tokens2[b].replaceAll("</td>", "");
                        tokens2[b] = tokens2[b].replaceAll("<td>", "");
                        tokens2[b] = tokens2[b].replaceAll("<td align=\"center\"", "");
                        tokens2[b] = tokens2[b].replaceAll("&nbsp;", "");
                        tokens2[b] = tokens2[b].replaceAll("\t", "");
                        tokens2[b] = tokens2[b].replaceAll(">", "");
                        tokens2[b] = tokens2[b].replaceAll("<!--", "");
                        tokens2[b] = tokens2[b].replaceAll("--", "");
                        //	tokens2[b]=tokens2[b].replaceAll(" ", "")	;
                        tokens2[b] = tokens2[b].replaceAll("<td", "");

                    }

                    //Save into array
                    noten[i - 1] = new TNote(tokens2[1].trim(), tokens2[2].trim(), tokens2[3].trim(), tokens2[4].trim(), tokens2[5].trim(), tokens2[6].trim(), tokens2[7].trim(), tokens2[8].trim(), tokens2[9].trim(), tokens2[10].trim());
                }


                for (int i = 0; i < noten.length; i++)
                {

                    db.overwriteNote(noten[i]);
                }


                if (line4.contains("ffentlichungs- datum"))
                {


                    line4 = line4.substring(line4.indexOf("ffentlichungs- datum"));
                    line4 = line4.substring(0, line4.indexOf("Hinweis"));

                    String tokensb[] = line4.split("</tr>");

                    notenb = new TNote[tokensb.length - 2];

                    for (int i = 1; i < tokensb.length - 1; i++)
                    {

                        String tokens2[] = tokensb[i].split("class=\"posrecords\"");

                        //Replace bad characters
                        for (int b = 1; b < tokens2.length; b++)
                        {
                            tokens2[b] = tokens2[b].replaceAll("</td>", "");
                            tokens2[b] = tokens2[b].replaceAll("<td>", "");
                            tokens2[b] = tokens2[b].replaceAll("<td align=\"center\"", "");
                            tokens2[b] = tokens2[b].replaceAll("&nbsp;", "");
                            tokens2[b] = tokens2[b].replaceAll("\t", "");
                            tokens2[b] = tokens2[b].replaceAll(">", "");
                            tokens2[b] = tokens2[b].replaceAll("<!--", "");
                            tokens2[b] = tokens2[b].replaceAll("--", "");
                            //	tokens2[b]=tokens2[b].replaceAll(" ", "")	;
                            tokens2[b] = tokens2[b].replaceAll("<td", "");

                        }

                        //Save into array
                        notenb[i - 1] = new TNote(tokens2[1].trim(), tokens2[2].trim(), tokens2[3].trim(), tokens2[4].trim(), tokens2[5].trim(), tokens2[6].trim(), tokens2[7].trim(), tokens2[8].trim(), tokens2[9].trim(), tokens2[10].trim());
                    }

                    for (int i = 0; i < notenb.length; i++)
                    {

                        db.overwriteNote(notenb[i]);
                    }


                }


            } catch (Exception e)
            {


                String returnvalue[] = new String[3];
                returnvalue[0] = "interner Fehler: " + e.toString();
                returnvalue[1] = "Stacktrace:" + getStackTrace(e);
                returnvalue[2] = params[3];
                return returnvalue;
            }

            String returnvalue[] = new String[3];
            returnvalue[0] = "success";
            returnvalue[1] = "success";
            returnvalue[2] = params[3];
            return returnvalue;
        }


        @Override
        protected void onPostExecute(String[] result)
        {

            try
            {

                TextView error_text = (TextView) getView().findViewById(R.id.error_text);
                getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);

                if (!result[0].contains("success"))
                {
                    //	 Toast.makeText(getActivity(),"Konnte Noten nicht aktualisieren.\r\n "+result[0]  ,Toast.LENGTH_LONG).show();

                    getView().findViewById(R.id.progressBar1).setVisibility(View.GONE);
                    if (result[0].contains("Host"))
                        error_text.setText("Konnte Keine Verbindung zum Server herstellen. Keine Internetverbindung?");
                    else
                        //error_text.setText(result[0]);
                        error_text.setText("Noten konnten nicht aktualisiert werden - Probiere es später erneut.");
                }
                else
                {
                    //getView().findViewById(R.id.wait).setVisibility(View.GONE);
                    count2 = db.getAllNoten().size();
                    if (count2 - count == 0) error_text.setText("Keine neue Noten");
                    else
                        error_text.setText(count2 - count + " neue Note(n) heruntergeladen");

                }

//maybe restart noten?

                display(result);
            } catch (Exception e)
            {
            }
            ;
        }
    }

    private List createGroupList(String[] allsem)
    {
        ArrayList result = new ArrayList();
        for (int i = 0; i < allsem.length; i++)
        { // 15 groups........
            HashMap m = new HashMap();
            m.put("Group Item", allsem[i]); // the key and it's value.
            result.add(m);
        }
        return (List) result;
    }

    private List createChildList(String[] allsem)
    {

        ArrayList result = new ArrayList();
//    	for( int i = 0 ; i < 15 ; ++i ) { // this -15 is the number of groups(Here it's fifteen)
//    	  /* each group need each HashMap-Here for each group we have 3 subgroups */
//    	  ArrayList secList = new ArrayList();
//    	  for( int n = 0 ; n < 3 ; n++ ) {
//    	    HashMap child = new HashMap();
//    		child.put( "Sub Item", "Sub Item " + n );
//    		child.put( "Sub Item2", "Sub Item2 " + n );
//    		secList.add( child );
//    	  }
//    	 result.add( secList );
//    	}
//    	
        for (int i = 0; i < allsem.length; i++)
        {


            List<TNote> semnoten = db.getSemNoten(allsem[i]);


            ArrayList secList = new ArrayList();
            secList = new ArrayList();
            for (int n = 0; n < semnoten.size(); n++)
            {
                if (semnoten.get(n)._Credits.equals("0.0"))
                {

                    HashMap child = new HashMap();
                    child.put("Sub Item", semnoten.get(n)._Titel);
                    child.put("Sub Item2", "Note: " + semnoten.get(n)._Note);
                    child.put("Sub Item3", "Credits: " + semnoten.get(n)._Credits);
                    //child.put( "Sub Item4", "" );
                    //child.put( "Sub Item5", "" );
                    //child.put( "Sub Item6", "");
                    secList.add(child);
                }
                else
                {

                    HashMap child = new HashMap();
                    //	child.put( "Sub Item", "" );
                    //	child.put( "Sub Item2", "" );
                    //	child.put( "Sub Item3", "");
                    child.put("Sub Item4", semnoten.get(n)._Titel);
                    child.put("Sub Item2", "Note: " + semnoten.get(n)._Note);
                    child.put("Sub Item3", "Credits: " + semnoten.get(n)._Credits);
                    //	child.put( "Sub Item5", "Note: "+semnoten.get(n)._Note );
                    //	child.put( "Sub Item6", "Credits: "+ semnoten.get(n)._Credits);
                    secList.add(child);

                }

            }
            result.add(secList);
        }


        return result;
    }

    void display(String[] result)
    {

        try
        {

            //	LinearLayout l=(LinearLayout) getView().findViewById(R.id.linearLayout2);
            //	l.setVisibility(View.GONE);
            ExpandableListView v = (ExpandableListView) getView().findViewById(R.id.noten);
            v.setVisibility(View.VISIBLE);

            String[] allsem = db.getAllSem();


            ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> map = new HashMap<String, String>();

            SimpleExpandableListAdapter expListAdapter =
                    new SimpleExpandableListAdapter(
                            getActivity(),
                            createGroupList(allsem),                // Creating group List.
                            R.layout.group_row,                // Group item layout XML.
                            new String[]{"Group Item"},    // the key of group item.
                            new int[]{R.id.row_name},    // ID of each group item.-Data under the key goes into this TextView.
                            createChildList(allsem),                // childData describes second-level entries.
                            R.layout.child_row,                // Layout for sub-level entries(second level).
                            new String[]{"Sub Item", "Sub Item2", "Sub Item3", "Sub Item4"},        // Keys in childData maps to display.
                            new int[]{R.id.grp_child, R.id.grp_child2, R.id.grp_child3, R.id.grp_child_green}        // Data under the keys above go into these TextViews.
                    );
            //	v.setListAdapter( expListAdapter );
            v.setAdapter(expListAdapter);
            v.setDividerHeight(0);

        } catch (Exception e)
        {
        }
        ;


    }

}
