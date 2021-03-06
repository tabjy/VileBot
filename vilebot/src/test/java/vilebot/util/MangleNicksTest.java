package vilebot.util;

import com.google.common.collect.ImmutableSortedSet;
import com.oldterns.vilebot.util.MangleNicks;
import org.junit.Before;
import org.junit.Test;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.JoinEvent;
import org.pircbotx.hooks.events.MessageEvent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MangleNicksTest
{

    private MessageEvent rcvPrivMsg;

    private JoinEvent rcvJoin;

    private ImmutableSortedSet<String> nickList = ImmutableSortedSet.of( "salman", "sasiddiq" );

    @Before
    public void setup()
    {
        Channel chan = mock( Channel.class );
        rcvPrivMsg = mock( MessageEvent.class );
        rcvJoin = mock( JoinEvent.class );
        when( rcvPrivMsg.getChannel() ).thenReturn( chan );
        when( rcvJoin.getChannel() ).thenReturn( chan );
        when( chan.getUsersNicks() ).thenReturn( nickList );
    }

    @Test
    public void noNicks()
    {
        String messageText = "i am the karma police";
        String returnText1 = MangleNicks.mangleNicks( rcvPrivMsg, messageText );
        String returnText2 = MangleNicks.mangleNicks( rcvJoin, messageText );

        assertEquals( returnText1, messageText );
        assertEquals( returnText2, messageText );
    }

    @Test
    public void oneNick()
    {
        String messageText = "salman is a man of many bots";
        String returnText1 = MangleNicks.mangleNicks( rcvPrivMsg, messageText );
        String returnText2 = MangleNicks.mangleNicks( rcvJoin, messageText );
        String expectedReturn = "namlas is a man of many bots";

        assertEquals( returnText1, expectedReturn );
        assertEquals( returnText2, expectedReturn );
    }

    @Test
    public void multipleNicks()
    {
        String messageText = "salman is actually sasiddiq";
        String returnText1 = MangleNicks.mangleNicks( rcvPrivMsg, messageText );
        String returnText2 = MangleNicks.mangleNicks( rcvJoin, messageText );
        String expectedReturn = "namlas is actually qiddisas";

        assertEquals( returnText1, expectedReturn );
        assertEquals( returnText2, expectedReturn );
    }
}
