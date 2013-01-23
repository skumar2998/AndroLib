
/*
* Copyright 2012 Ivan Masmitjà
* Copyright 2011 Uwe Trottmann
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*
*/

package net.compactsys.androlib.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import net.compactsys.androlib.util.ALog;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStore.SecretKeyEntry;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Symmetrically encrypts and decrypts strings using a seeded key stored in a
 * key store on internal storage. Be aware that anyone gaining access to this
 * app's storage may be able to access the key store. This is just supposed to
 * add furhter layers of work before being able to access encrypted data.
 */
public class SimpleCrypto {

    public static final String KEY_ALIAS = "coreentry";
    public static final String PREFERENCES_KEY = "net.compactsys.androlib.simplecryptokey";
    private static final String DATACORE = "datacore";

    /**
     * Returns the given string in encrypted form, or {@code null} if encryption
     * was unsuccessful.
     */
    public static String encrypt(String cleartext, Context context) {
        try {
            SecretKey key = getKey(context, PREFERENCES_KEY);
            byte[] result = encrypt(key, cleartext.getBytes());
            return toHex(result);
        } catch (GeneralSecurityException e) {
            ALog.w("Encrypt exception", e);
        } catch (IOException e) {
            ALog.w("Encrypt exception", e);
        }
        return null;
    }

    /**
     * Decrypts the given string and returns the clear text, or {@code null} if
     * decryption was unsuccessful.
     */
    public static String decrypt(String encrypted, Context context) {
        try {
            SecretKey key = getKey(context, PREFERENCES_KEY);
            byte[] enc = toByte(encrypted);
            byte[] result = decrypt(key, enc);
            return new String(result);
        } catch (GeneralSecurityException e) {
            ALog.w("Decrypt exception", e);
        } catch (IOException e) {
            ALog.w("Decrypt exception", e);
        }
        return null;
    }

    private static SecretKey getKey(Context context, String preferenceSecureKey) throws IOException,
            GeneralSecurityException {
        // ensure seed/password
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context
                .getApplicationContext());
        String seed = prefs.getString(preferenceSecureKey, null);
        if (seed == null) {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] seedBytes = sr.generateSeed(16);
            seed = toHex(seedBytes);
            prefs.edit().putString(preferenceSecureKey, seed).commit();
        }

        final char[] keystorePassword = seed.toCharArray();

        // ensure key store
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fis = null;
        try {
            // load existing key store
            fis = context.openFileInput(DATACORE);
            keystore.load(fis, keystorePassword);
        } catch (FileNotFoundException e) {
            // create new key store
            keystore.load(null, keystorePassword);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }

        // ensure key
        if (keystore.containsAlias(KEY_ALIAS)) {
            // retrieve existing key
            KeyStore.SecretKeyEntry entry = (SecretKeyEntry) keystore.getEntry(KEY_ALIAS,
                    new KeyStore.PasswordProtection(null));
            SecretKey key = entry.getSecretKey();
            return key;
        } else {
            // create new key
            SecretKey key = generateKey(toByte(seed));

            // store key
            KeyStore.SecretKeyEntry entry = new KeyStore.SecretKeyEntry(key);
            keystore.setEntry(KEY_ALIAS, entry, new KeyStore.PasswordProtection(null));

            // write out key store
            FileOutputStream fos = null;
            try {
                fos = context.openFileOutput(DATACORE, Context.MODE_PRIVATE);
                keystore.store(fos, keystorePassword);
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }

            return key;
        }
    }

    private static SecretKey generateKey(byte[] seed) throws NoSuchAlgorithmException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        sr.setSeed(seed);
        keyGen.init(128, sr); // 192 and 256 bits may not be available
        return keyGen.generateKey();
    }

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException
    {
        SecretKey skey = generateKey(seed);
        byte[] raw = skey.getEncoded();
        return raw;
    }

    private static byte[] encrypt(SecretKey key, byte[] clear) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    private static byte[] decrypt(SecretKey key, byte[] encrypted) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        return result;
    }

    public static String toHex(byte[] b) {
        String result = "";
        for (int i = 0; i < b.length; i++) {
            result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
        }
        return result;
    }

    //toHex Alternative
    /*
    private final static String HEX = "0123456789ABCDEF";
    public static String toHex(byte[] buf)
    {
        if (buf == null)
            return "";
        StringBuffer result = new StringBuffer(2 * buf.length);
        for (int i = 0; i < buf.length; i++)
        {
            sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        }
        return result.toString();
    }
    */
}