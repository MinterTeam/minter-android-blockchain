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

package network.minter.blockchainapi.models.operational;

import android.support.annotation.NonNull;

import network.minter.mintercore.util.RLP;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public abstract class Operation {
    /**
     * Encodes all operation fields via RLP
     *
     * @return encoded byte[]
     * @see RLP
     */
    @NonNull
    protected abstract byte[] encodeRLP();
    protected abstract void decodeRLP(@NonNull byte[] rlpEncodedData);


    protected byte[] fromRawRlp(int idx, Object[] raw) {
        return (byte[]) raw[idx];
    }

    protected abstract <T extends Operation, B extends Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx);

    public abstract static class Builder<Op extends Operation> {
        private Transaction<Op> mTx;

        Builder(Transaction<Op> op) {
            mTx = op;
        }

        public Transaction<Op> save() {
            return mTx;
        }

        protected Transaction<Op> getTx() {
            return mTx;
        }
    }
}
