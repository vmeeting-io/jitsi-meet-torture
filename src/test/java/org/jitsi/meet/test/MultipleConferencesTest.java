package org.jitsi.meet.test;

import org.jitsi.meet.test.base.*;
import org.jitsi.meet.test.util.*;
import org.jitsi.meet.test.web.*;
import org.openqa.selenium.*;
import org.testng.*;
import org.testng.annotations.*;

import java.util.*;

public class MultipleConferencesTest
    extends WebTestBase
{
    /**
     * The video file to use as input for the first participant (the sender).
     */
    private static final String INPUT_VIDEO_FILE
        = "resources/FourPeople_1280x720_60.y4m";


    @DataProvider(name = "dp", parallel = true)
    public Object[][] createData()
    {
        return new Object[][]
            {
                new Object[] { getJitsiMeetUrl(), 3, 100000 },
                new Object[] { getJitsiMeetUrl(), 3, 100000 }
            };
    }

    @Test(dataProvider = "dp")
    public void testMain(
        JitsiMeetUrl url, int numberOfParticipants, long waitTime)
    {
        Participant[] participants = new Participant[numberOfParticipants];
        try
        {
            WebParticipantOptions ops
                = new WebParticipantOptions().setFakeStreamVideoFile(
                    INPUT_VIDEO_FILE);

            for(int i = 0; i < participants.length; i++)
            {
                System.err.println("ooooo");
                participants[i] =
                    this.participants
                        .createParticipant("web.participant" + (i + 1), ops);
                participants[i].joinConference(url);
            }

            TestUtils.waitMillis(waitTime);
        }
        catch(Exception e)
        {
            // There was no dialog, so we fail the test !
            e.printStackTrace();
        }
        finally
        {
            // Clean up the participants in participants array
            Arrays.stream(participants).forEach(Participant::close);
        }
    }
}
