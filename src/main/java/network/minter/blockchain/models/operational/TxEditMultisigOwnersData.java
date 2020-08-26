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

package network.minter.blockchain.models.operational;

import android.os.Parcel;

import javax.annotation.Nonnull;

import network.minter.core.crypto.MinterAddress;

/**
 * Transaction for modify multisig address data
 * <p>
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxEditMultisigOwnersData extends TxCreateMultisigAddress {
    public static final Creator<TxEditMultisigOwnersData> CREATOR = new Creator<TxEditMultisigOwnersData>() {
        @Override
        public TxEditMultisigOwnersData createFromParcel(Parcel in) {
            return new TxEditMultisigOwnersData(in);
        }

        @Override
        public TxEditMultisigOwnersData[] newArray(int size) {
            return new TxEditMultisigOwnersData[size];
        }
    };

    public TxEditMultisigOwnersData() {
    }

    public TxEditMultisigOwnersData(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    protected TxEditMultisigOwnersData(Parcel in) {
        super(in);
    }

    @Override
    public TxEditMultisigOwnersData setThreshold(long threshold) {
        super.setThreshold(threshold);
        return this;
    }

    @Override
    public TxEditMultisigOwnersData addWeight(long... weight) {
        super.addWeight(weight);
        return this;
    }

    @Override
    public TxEditMultisigOwnersData addAddress(MinterAddress address) {
        super.addAddress(address);
        return this;
    }

    @Override
    public TxEditMultisigOwnersData addAddress(CharSequence address, long weight) {
        super.addAddress(address, weight);
        return this;
    }

    @Override
    public TxEditMultisigOwnersData addAddress(MinterAddress address, long weight) {
        super.addAddress(address, weight);
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public OperationType getType() {
        return OperationType.EditMultisigOwnersData;
    }
}
