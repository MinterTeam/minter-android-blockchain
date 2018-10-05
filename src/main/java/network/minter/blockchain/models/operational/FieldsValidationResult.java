/*
 * Copyright (C) by MinterTeam. 2018
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

package network.minter.blockchain.models.operational;

import java.util.ArrayList;
import java.util.List;

import network.minter.core.internal.common.Pair;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class FieldsValidationResult {

    private final List<Pair<String, String>> mFieldMessageMap = new ArrayList<>();
    private String mTitle;

    public FieldsValidationResult() {
    }

    public FieldsValidationResult(String title) {
        mTitle = title;
    }

    public FieldsValidationResult addResult(String field, String errorMessage) {
        mFieldMessageMap.add(Pair.create(field, errorMessage));
        return this;
    }

    public FieldsValidationResult addResult(String field, boolean valid, String message) {
        if (!valid) {
            mFieldMessageMap.add(Pair.create(field, message));
        }

        return this;
    }

    public FieldsValidationResult addResult(FieldsValidationResult result) {
        if (result == null || result.isValid()) {
            return this;
        }

        mFieldMessageMap.addAll(result.getFieldMessageMap());
        return this;
    }

    public List<Pair<String, String>> getFieldMessageMap() {
        return mFieldMessageMap;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getInvalidFieldsMessages() {
        if (isValid()) {
            return "";
        }

        final StringBuilder sb = new StringBuilder();
        for (Pair<String, String> kv : mFieldMessageMap) {
            sb.append(kv.first).append(": ").append(kv.second).append("\n");
        }
        return sb.toString();
    }

    public final boolean isValid() {
        return mFieldMessageMap.isEmpty();
    }
}
