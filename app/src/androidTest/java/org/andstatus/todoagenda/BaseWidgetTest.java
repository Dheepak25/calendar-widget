package org.andstatus.todoagenda;

import org.andstatus.todoagenda.prefs.InstanceSettings;
import org.andstatus.todoagenda.provider.MockCalendarContentProvider;
import org.andstatus.todoagenda.widget.LastEntry;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.assertTrue;

/**
 * @author yvolk@yurivolkov.com
 */
public class BaseWidgetTest {
    final static String TAG = BaseWidgetTest.class.getSimpleName();

    protected MockCalendarContentProvider provider = null;
    protected RemoteViewsFactory factory = null;

    @Before
    public void setUp() throws Exception {
        provider = MockCalendarContentProvider.getContentProvider();
        factory = new RemoteViewsFactory(provider.getContext(), provider.getWidgetId());
        assertTrue(factory.getWidgetEntries().get(0) instanceof LastEntry);
    }

    @After
    public void tearDown() throws Exception {
        MockCalendarContentProvider.tearDown();
    }

    DateTime dateTime(
            int year,
            int monthOfYear,
            int dayOfMonth) {
        return dateTime(year, monthOfYear, dayOfMonth, 0, 0);
    }

    DateTime dateTime(
            int year,
            int monthOfYear,
            int dayOfMonth,
            int hourOfDay,
            int minuteOfHour) {
        return new DateTime(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, 0, 0,
                provider.getSettings().clock().getZone());
    }

    protected void playResults(String tag) {
        provider.updateAppSettings(tag);

        EnvironmentChangedReceiver.updateWidget(provider.getContext(), provider.getWidgetId());

        factory.setWaitingForRedraw(true);
        factory.onDataSetChanged();
        factory.logWidgetEntries(tag);

        if (provider.usesActualWidget) {
            waitTillWidgetIsRedrawn();
        }
    }

    private void waitTillWidgetIsRedrawn() {
        long start = System.currentTimeMillis();
        while (factory.isWaitingForRedraw() && Math.abs(System.currentTimeMillis() - start) < 3000){
            EnvironmentChangedReceiver.sleep(20);
        }
        EnvironmentChangedReceiver.sleep(250);
    }

    protected InstanceSettings getSettings() {
        return provider.getSettings();
    }
}
