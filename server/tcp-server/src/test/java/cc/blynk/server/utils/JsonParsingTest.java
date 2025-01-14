package cc.blynk.server.utils;

import cc.blynk.server.model.DashBoard;
import cc.blynk.server.model.Pin;
import cc.blynk.server.model.Profile;
import cc.blynk.server.model.enums.PinType;
import cc.blynk.server.model.widgets.Widget;
import cc.blynk.server.model.widgets.controls.Button;
import cc.blynk.server.model.widgets.controls.RGB;
import cc.blynk.server.model.widgets.others.Timer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 * User: ddumanskiy
 * Date: 21.11.13
 * Time: 13:27
 */
public class JsonParsingTest {

    //TODO Tests for all widget types!!!

    @Test
    public void testParseUserProfile() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_json.txt");

        Profile profile = JsonParser.parseProfile(is);
        assertNotNull(profile);
        assertNotNull(profile.getDashBoards());
        assertEquals(profile.getDashBoards().length, 1);

        //this property shoudn't be parsed
        assertNotNull(profile.activeDashId);

        DashBoard dashBoard = profile.getDashBoards()[0];

        assertNotNull(dashBoard);

        assertEquals(1, dashBoard.getId());
        assertEquals("My Dashboard", dashBoard.getName());
        assertNotNull(dashBoard.getWidgets());
        assertEquals(dashBoard.getWidgets().length, 8);
        assertNotNull(dashBoard.getBoardType());
        assertEquals("UNO", dashBoard.getBoardType());

        for (Widget widget : dashBoard.getWidgets()) {
            assertNotNull(widget);
            assertEquals(1, widget.x);
            assertEquals(1, widget.y);
            assertEquals(1, widget.id);
            assertEquals("Some Text", widget.label);
        }
    }

    @Test
    public void testUserProfileToJson() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_json.txt");

        Profile profile = JsonParser.parseProfile(is);
        String userProfileString = profile.toString();

        assertNotNull(userProfileString);
        assertTrue(userProfileString.contains("dashBoards"));
    }

    @Test
    public void testParseIOSProfile() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_ios_profile_json.txt");

        Profile profile = JsonParser.parseProfile(is);

        assertNotNull(profile);
        assertNotNull(profile.getDashBoards());
        assertEquals(1, profile.getDashBoards().length);
        assertNotNull(profile.getDashBoards()[0].getWidgets());
        assertNotNull(profile.getDashBoards()[0].getWidgets()[0]);
        assertNotNull(profile.getDashBoards()[0].getWidgets()[1]);
        assertTrue(((Button) profile.getDashBoards()[0].getWidgets()[0]).pushMode);
        assertFalse(((Button) profile.getDashBoards()[0].getWidgets()[1]).pushMode);
    }

    @Test
    public void testJSONToRGB() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_json_RGB.txt");

        Profile profile = JsonParser.parseProfile(is);

        assertNotNull(profile);
        assertNotNull(profile.getDashBoards());
        assertEquals(1, profile.getDashBoards().length);
        assertNotNull(profile.getDashBoards()[0]);
        assertNotNull(profile.getDashBoards()[0].getWidgets());
        assertEquals(1, profile.getDashBoards()[0].getWidgets().length);

        RGB rgb = (RGB) profile.getDashBoards()[0].getWidgets()[0];

        assertNotNull(rgb.pins);
        assertEquals(2, rgb.pins.length);
        Pin pin1 = rgb.pins[0];
        Pin pin2 = rgb.pins[1];

        assertNotNull(pin1);
        assertNotNull(pin2);

        assertEquals(1, pin1.pin.byteValue());
        assertEquals(2, pin2.pin.byteValue());

        assertEquals("1", pin1.value);
        assertEquals("2", pin2.value);

        assertEquals(PinType.DIGITAL, pin1.pinType);
        assertEquals(PinType.DIGITAL, pin2.pinType);

        assertNull(pin1.pwmMode);
        assertTrue(pin2.pwmMode);

    }

    @Test
    public void testUserProfileToJson2() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_json_2.txt");

        Profile profile = JsonParser.parseProfile(is);
        String userProfileString = profile.toString();

        assertNotNull(userProfileString);
        assertTrue(userProfileString.contains("dashBoards"));
    }

    @Test
    public void testUserProfileToJson3() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_json_3.txt");

        Profile profile = JsonParser.parseProfile(is);
        String userProfileString = profile.toString();

        assertNotNull(userProfileString);
        assertTrue(userProfileString.contains("dashBoards"));
    }

    @Test
    public void testUserProfileToJsonWithTimer() {
        InputStream is = this.getClass().getResourceAsStream("/json_test/user_profile_with_timer.txt");

        Profile profile = JsonParser.parseProfile(is);
        String userProfileString = profile.toString();
        profile.activeDashId = 1;

        assertNotNull(userProfileString);
        assertTrue(userProfileString.contains("dashBoards"));
        List<Timer> timers = profile.getActiveDashboardTimerWidgets();
        assertNotNull(timers);
        assertEquals(1, timers.size());
    }

    @Test
    public void correctSerializedObject() throws JsonProcessingException {
        Button button = new Button();
        button.id = 1;
        button.label = "MyButton";
        button.x = 2;
        button.y = 2;
        button.pushMode = false;

        String result = JsonParser.mapper.writeValueAsString(button);

        assertEquals("{\"type\":\"BUTTON\",\"id\":1,\"x\":2,\"y\":2,\"label\":\"MyButton\",\"pushMode\":false}", result);
    }
}
