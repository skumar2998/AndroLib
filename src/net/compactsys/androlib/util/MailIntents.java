/*
 * Copyright 2012 Ivan Masmitjà
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.compactsys.androlib.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

public class MailIntents {

    /**
     * Launch an Intent to send an email.
     * @param context Context that launches Intent.
     * @param mailTo Specifies the receiver / receivers of the email.
     */
    public static void sendEmail(Context context, String[] mailTo) {
        sendEmail(context, mailTo, "", "", null);
    }

    /**
     * Launch an Intent to send an email.
     * @param context Context that launches Intent.
     * @param mailTo Specifies the receiver / receivers of the email.
     * @param subject Specifies the subject of the email.
     * @param body Defines the message to be sent.
     * @param attachments Files to attach to the email message.
     */
    public static void sendEmail(Context context, String[] mailTo,
                                 String subject, String body, File[] attachments) {
        ArrayList<Uri> uris = new ArrayList<Uri>();

        if (attachments != null) {
            for (File f : attachments) {
                Uri u = Uri.fromFile(f);
                uris.add(u);
            }
        }

        String action = android.content.Intent.ACTION_SEND;
        String mimeType = "text/plain";

        if (uris.size() > 1)
            action = android.content.Intent.ACTION_SEND_MULTIPLE;

        if (uris.size() > 0)
            mimeType = "multipart/mixed";

        Intent emailIntent = new Intent(action);
        emailIntent.setType(mimeType);
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, mailTo);

        if ((subject != null) && (subject.trim().length() > 0))
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        if ((body != null) && (body.trim().length() > 0))
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);

        if (uris.size() > 0)
            emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);

        context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }
}
