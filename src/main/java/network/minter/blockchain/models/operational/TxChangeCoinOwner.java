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
import android.os.Parcelable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.helpers.StringHelper.charsToString;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */

public class TxChangeCoinOwner extends Operation {


    public static final Parcelable.Creator<TxChangeCoinOwner> CREATOR = new Parcelable.Creator<TxChangeCoinOwner>() {
        @Override
        public TxChangeCoinOwner createFromParcel(Parcel in) {
            return new TxChangeCoinOwner(in);
        }

        @Override
        public TxChangeCoinOwner[] newArray(int size) {
            return new TxChangeCoinOwner[size];
        }
    };

    private String mSymbol;
    private MinterAddress mNewOwner;

    public TxChangeCoinOwner(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public TxChangeCoinOwner(Parcel in) {
        mSymbol = in.readString();
        mNewOwner = (MinterAddress) in.readValue(MinterAddress.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(mSymbol);
        dest.writeValue(mNewOwner);
    }

    @Override
    public OperationType getType() {
        return OperationType.ChangeCoinOwner;
    }

    public String getSymbol() {
        return mSymbol.replace("\0", "");
    }

    public TxChangeCoinOwner setSymbol(String symbol) {
        checkArgument(
                symbol != null && symbol.length() >= 3 && symbol.length() <= 10,
                String.format("Coin %s length must be from 3 to 10 symbols", symbol)
        );
        mSymbol = StringHelper.strrpad(10, symbol.toUpperCase());
        return this;
    }

    public MinterAddress getNewOwner() {
        return mNewOwner;
    }

    public TxChangeCoinOwner setNewOwner(MinterAddress newOwner) {
        mNewOwner = newOwner;
        return this;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mSymbol", mSymbol != null, "Coin symbol must be set")
                .addResult("mNewOwner", mNewOwner != null, "New owner address must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mSymbol = charsToString(fromRawRlp(0, decoded));
        mNewOwner = new MinterAddress(fromRawRlp(1, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mSymbol,
                mNewOwner
        });
    }
}
