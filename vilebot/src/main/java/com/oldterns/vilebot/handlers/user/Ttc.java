package com.oldterns.vilebot.handlers.user;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by eunderhi on 27/10/15.
 */
// @HandlerContainer
public class Ttc
    extends ListenerAdapter
{

    private static final Pattern TTC_PATTERN = Pattern.compile( "^!ttc$" );

    public static final String TTC_URL = "https://www.ttc.ca/Service_Advisories/all_service_alerts.jsp";

    // @Handler
    @Override
    public void onGenericMessage( final GenericMessageEvent event )
    {
        String text = event.getMessage();
        Matcher match = TTC_PATTERN.matcher( text );

        if ( match.matches() )
        {
            printAlerts( event );
        }
    }

    private void printAlerts( GenericMessageEvent event )
    {
        try
        {
            for ( Element element : parseContent( getContent() ) )
            {
                event.respondWith( element.text() );
            }
        }
        catch ( Exception e )
        {
            event.respondWith( "Unable to retrieve alerts." );
        }
    }

    private Elements parseContent( String content )
        throws Exception
    {
        Elements alerts = new Elements();
        Document doc = Jsoup.parse( content );
        Elements alertDivs = doc.select( "div[class=alert-content]" );
        for ( Element element : alertDivs )
        {
            if ( !element.text().toLowerCase().contains( "elevator" ) )
            {
                alerts.addAll( element.select( "p[class=veh-replace]" ) );
            }
        }
        return alerts;
    }

    private String getContent()
        throws Exception
    {
        String content;
        URLConnection connection;
        connection = new URL( TTC_URL ).openConnection();
        connection.addRequestProperty( "User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)" );
        Scanner scanner = new Scanner( connection.getInputStream() );
        scanner.useDelimiter( "\\Z" );
        content = scanner.next();
        return content;
    }
}
