/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Before;

import network.minter.mintercore.MinterSDK;
import timber.log.Timber;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BaseApiTest {

    static {
        MinterSDK.initialize();
        //noinspection ConstantConditions
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected void log(int priority, String tag, String message, Throwable t) {
                if (message.length() < 4000) {
                    if (priority == Log.ASSERT) {
                        System.err.println(String.format("[%d]%s: %s", priority, tag, message));
                    } else {
                        System.out.println(String.format("[%d]%s: %s", priority, tag, message));
                    }
                    return;
                }

                // Split by line, then ensure each line can fit into Log's maximum length.
                for (int i = 0, length = message.length(); i < length; i++) {
                    int newline = message.indexOf('\n', i);
                    newline = newline != -1 ? newline : length;
                    do {
                        int end = Math.min(newline, i + 4000);
                        String part = message.substring(i, end);
                        if (priority == Log.ASSERT) {
                            System.err.println(String.format("[%d]%s: %s", priority, tag, part));
                        } else {
                            System.out.println(String.format("[%d]%s: %s", priority, tag, part));
                        }
                        i = end;
                    } while (i < newline);
                }
            }
        });
    }

    private Gson mGson;

    @Before
    public void setUp() {
        mGson = new GsonBuilder().setPrettyPrinting().create();
    }


    public String toJson(Object o) {
        return mGson.toJson(o);
    }
}
