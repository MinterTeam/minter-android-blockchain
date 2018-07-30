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

import android.annotation.TargetApi;
import android.util.Pair;

import java.util.List;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class OperationInvalidDataException extends Exception {

    /**
     * Contains list of invalid operation fields
     */
    private final List<Pair<String, String>> mInvalidFields;

    public OperationInvalidDataException(String message, List<Pair<String, String>> invalidFields) {
        super(message);
        mInvalidFields = invalidFields;
    }

    public OperationInvalidDataException(String message, List<Pair<String, String>> invalidFields, Throwable cause) {
        super(message, cause);
        mInvalidFields = invalidFields;
    }

    public OperationInvalidDataException(List<Pair<String, String>> invalidFields, Throwable cause) {
        super(cause);
        mInvalidFields = invalidFields;
    }

    @TargetApi(24)
    public OperationInvalidDataException(String message, List<Pair<String, String>> invalidFields, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        mInvalidFields = invalidFields;
    }

    public List<Pair<String, String>> getInvalidFields() {
        return mInvalidFields;
    }
}
