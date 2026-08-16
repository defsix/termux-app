package com.termux.app.settings;

import androidx.annotation.NonNull;

import com.termux.shared.logger.Logger;
import com.termux.shared.settings.properties.SharedProperties;
import com.termux.shared.termux.TermuxConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Writes a single {@code key=value} pair into termux.properties, preserving all other lines
 * (comments, other properties, ordering). termux.properties is otherwise only ever read by the
 * app (see {@link SharedProperties}) and is meant to be user-edited directly; this lets the
 * Settings UI update the same file a user would otherwise edit by hand, so there is a single
 * source of truth.
 */
public final class TermuxPropertiesWriter {

    private static final String LOG_TAG = "TermuxPropertiesWriter";

    private TermuxPropertiesWriter() {}

    public static synchronized boolean setProperty(@NonNull String key, @NonNull String value) {
        File propertiesFile = SharedProperties.getPropertiesFileFromList(TermuxConstants.TERMUX_PROPERTIES_FILE_PATHS_LIST, LOG_TAG);
        if (propertiesFile == null)
            propertiesFile = TermuxConstants.TERMUX_PROPERTIES_PRIMARY_FILE;

        List<String> lines = new ArrayList<>();
        if (propertiesFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(propertiesFile))) {
                String line;
                while ((line = reader.readLine()) != null) lines.add(line);
            } catch (IOException e) {
                Logger.logError(LOG_TAG, "Failed to read " + propertiesFile.getAbsolutePath() + ": " + e.getMessage());
                return false;
            }
        }

        Pattern keyPattern = Pattern.compile("^\\s*" + Pattern.quote(key) + "\\s*=");
        String newLine = key + "=" + value;

        boolean replaced = false;
        for (int i = 0; i < lines.size(); i++) {
            Matcher matcher = keyPattern.matcher(lines.get(i));
            if (matcher.find()) {
                lines.set(i, newLine);
                replaced = true;
                break;
            }
        }
        if (!replaced) lines.add(newLine);

        File parentDir = propertiesFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            Logger.logError(LOG_TAG, "Failed to create parent directory " + parentDir.getAbsolutePath());
            return false;
        }

        try (FileWriter writer = new FileWriter(propertiesFile)) {
            for (String line : lines) {
                writer.write(line);
                writer.write("\n");
            }
        } catch (IOException e) {
            Logger.logError(LOG_TAG, "Failed to write " + propertiesFile.getAbsolutePath() + ": " + e.getMessage());
            return false;
        }

        return true;
    }

}
