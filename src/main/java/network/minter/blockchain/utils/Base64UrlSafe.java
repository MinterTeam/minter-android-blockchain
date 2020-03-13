/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.blockchain.utils;

import org.spongycastle.util.encoders.Base64;

import network.minter.core.crypto.BytesData;

public class Base64UrlSafe {
    private final static char PADDING = '=';
    private final static char[] TO_ESCAPE = new char[]{'+', '/'};
    private final static char[] TO_REPLACE = new char[]{'-', '_'};

    public static BytesData encode(BytesData data) {
        return new BytesData(encodeString(data).getBytes());
    }

    public static BytesData encode(byte[] data) {
        return new BytesData(encodeString(data).getBytes());
    }

    public static BytesData encode(char[] data) {
        return new BytesData(encodeString(data).getBytes());
    }

    public static String encodeString(String data) {
        return encodeString(data.getBytes());
    }

    public static String encodeString(BytesData data) {
        return encodeString(data.getBytes());
    }

    public static String encodeString(byte[] data) {
        String enc = new String(Base64.encode(data));
        for (int i = 0; i < TO_ESCAPE.length; i++) {
            enc = enc.replace(TO_ESCAPE[i], TO_REPLACE[i]);
        }
        enc = substrRemove(enc, PADDING);
        return enc;
    }

    public static String encodeString(char[] data) {
        return encodeString(new BytesData(data).getBytes());
    }

    public static BytesData decode(BytesData data) {
        return decode(data.stringValue());
    }

    public static BytesData decode(byte[] data) {
        return decode(new String(data));
    }

    public static BytesData decode(char[] data) {
        return decode(new String(data));
    }

    public static BytesData decode(String data) {
        String enc = data;
        for (int i = 0; i < TO_REPLACE.length; i++) {
            enc = enc.replace(TO_REPLACE[i], TO_ESCAPE[i]);
        }
        if (enc.length() % 4 != 0) {
            enc += strRepeat(PADDING, 4 - enc.length() % 4);
        }

        byte[] dec = Base64.decode(enc);
        return new BytesData(dec);
    }

    public static String decodeString(String data) {
        return decode(data.getBytes()).stringValue();
    }

    private static String strRepeat(char s, int cnt) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cnt; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    private static String substrRemove(String src, char toFind) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) != toFind) {
                sb.append(src.charAt(i));
            }
        }
        return sb.toString();
    }
}
