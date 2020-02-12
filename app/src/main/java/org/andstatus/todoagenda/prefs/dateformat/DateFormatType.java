package org.andstatus.todoagenda.prefs.dateformat;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import org.andstatus.todoagenda.R;
import org.andstatus.todoagenda.util.LazyVal;

import java.util.ArrayList;
import java.util.List;

/** See https://github.com/andstatus/todoagenda/issues/7
 *  @author yvolk@yurivolkov.com
 * */
public enum DateFormatType {
    HIDDEN("hidden", R.string.hidden, ""),
    DEVICE_DEFAULT("deviceDefault", R.string.device_default, ""),
    DEFAULT_WEEKDAY("defaultWeekday", R.string.date_format_default_weekday, ""),
    DEFAULT_DAYS("defaultDays", R.string.date_format_default_days, ""),
    ABBREVIATED("abbrev", R.string.appearance_abbreviate_dates_title, ""),
    NUMBER_OF_DAYS("days", R.string.date_format_number_of_days_to_event, ""),
    DAY_IN_MONTH("dayInMonth", R.string.date_format_day_in_month, "dd"),
    MONTH_DAY("monthDay", R.string.date_format_month_day, "MM-dd"),
    WEEK_YEAR("weekInYear", R.string.date_format_week_in_year, "ww"),
    CUSTOM("custom-01", R.string.custom_pattern, ""),
    UNKNOWN("unknown", R.string.not_found, "");

    public final String code;
    @StringRes
    public final int titleResourceId;
    public final String pattern;

    public final static DateFormatType DEFAULT = DEFAULT_WEEKDAY;

    private final LazyVal<DateFormatValue> defaultValue = LazyVal.of( () ->
            new DateFormatValue(DateFormatType.this, ""));

    DateFormatType(String code, int titleResourceId, String pattern) {
        this.code = code;
        this.titleResourceId = titleResourceId;
        this.pattern = pattern;
    }

    @NonNull
    public static DateFormatValue load(String storedValue, @NonNull DateFormatValue defaultValue) {
        DateFormatType formatType = DateFormatType.load(storedValue, UNKNOWN);
        switch (formatType) {
            case UNKNOWN:
                return defaultValue;
            case CUSTOM:
                return new DateFormatValue(formatType, storedValue.substring(CUSTOM.code.length() + 1));
            default:
                return formatType.defaultValue();
        }
    }

    @NonNull
    private static DateFormatType load(String storedValue, @NonNull DateFormatType defaultType) {
        if (storedValue == null) return defaultType;

        for (DateFormatType type: values()) {
            if (storedValue.startsWith( type.code + ":")) return type;
        }
        return defaultType;
    }

    public static DateFormatValue unknownValue() {
        return UNKNOWN.defaultValue();
    }

    public static List<CharSequence> getSpinnerEntryList(Context context) {
        List<CharSequence> list = new ArrayList<>();
        for (DateFormatType type: values()) {
            if (type == UNKNOWN) break;
            list.add(context.getText(type.titleResourceId));
        }
        return list;
    }

    public DateFormatValue defaultValue() {
        return defaultValue.get();
    }

    public int getSpinnerPosition() {
        for (int position = 0; position < values().length; position++) {
            DateFormatType type = values()[position];
            if (type == UNKNOWN) break;
            if (type == this) return position;
        }
        return 0;
    }
}