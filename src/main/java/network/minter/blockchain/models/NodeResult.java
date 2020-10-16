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

package network.minter.blockchain.models;

import org.parceler.Parcel;

import java.util.Map;

import javax.annotation.Nonnull;

import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class NodeResult {
    public Error error;

    public int getCode() {
        if (error == null) return 0;

        return error.code;
    }

    public String getMessage() {
        if (error == null) {
            return null;
        }

        return error.message;
    }

    public BlockchainStatus getStatus() {
        if (error == null) {
            return BlockchainStatus.Success;
        }

        return BlockchainStatus.findByCode(getCode());
    }

    public boolean isOk() {
        return error == null;
    }

    @Nonnull
    @Override
    public String toString() {
        return String.format("NodeResult{code=%s, message=%s}",
                firstNonNull(getCode(), 0),
                firstNonNull(getMessage(), "OK")
        );
    }

    @Parcel
    public static class Error {
        public Integer code;
        public String message;
        public Map<String, String> data;
    }
}
