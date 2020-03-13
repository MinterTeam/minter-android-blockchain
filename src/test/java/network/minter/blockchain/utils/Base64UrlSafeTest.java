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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Base64UrlSafeTest {

    @Test
    public void testEncodeDecode1() {
        String test = "hello";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG8", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }

    @Test
    public void testEncodeDecode2() {
        String test = "hellow";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG93", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }

    @Test
    public void testEncodeDecode3() {
        String test = "hellowo";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG93bw", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }

    @Test
    public void testEncodeDecode4() {
        String test = "hellowor";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG93b3I", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }

    @Test
    public void testEncodeDecode5() {
        String test = "helloworl";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG93b3Js", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }

    @Test
    public void testEncodeDecode6() {
        String test = "helloworld";
        String encoded = Base64UrlSafe.encodeString(test);
        assertEquals("aGVsbG93b3JsZA", encoded);

        String decoded = Base64UrlSafe.decodeString(encoded);
        assertEquals(test, decoded);
    }
}
