package com.angrydu.plumbingstore.message;

import java.util.Locale;
import java.util.ResourceBundle;

public class InternalizationMessageManagerConfig {

    public static String getExceptionMessage(String keyMessage) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("exceptionsMessages", Locale.ENGLISH);
        return resourceBundle.getString(keyMessage);
    }

    public static String getMessage(String keyMessage) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("textMessages", Locale.ENGLISH);
        return resourceBundle.getString(keyMessage);
    }
}
