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

import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */

public class TxSetHaltBlock extends Operation {
    public static final Creator<TxSetHaltBlock> CREATOR = new Creator<TxSetHaltBlock>() {
        @Override
        public TxSetHaltBlock createFromParcel(Parcel in) {
            return new TxSetHaltBlock(in);
        }

        @Override
        public TxSetHaltBlock[] newArray(int size) {
            return new TxSetHaltBlock[size];
        }
    };

    private MinterPublicKey mPublicKey;
    private BigInteger mHeight;

    public TxSetHaltBlock(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public TxSetHaltBlock(Parcel in) {
        mPublicKey = (MinterPublicKey) in.readValue(MinterPublicKey.class.getClassLoader());
        mHeight = (BigInteger) in.readValue(BigInteger.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeValue(mPublicKey);
        dest.writeValue(mHeight);
    }

    @Override
    public OperationType getType() {
        return OperationType.SetHaltBlock;
    }

    public MinterPublicKey getPublicKey() {
        return mPublicKey;
    }

    public TxSetHaltBlock setPublicKey(MinterPublicKey publicKey) {
        mPublicKey = publicKey;
        return this;
    }

    public TxSetHaltBlock setPublicKey(CharSequence publicKey) {
        mPublicKey = new MinterPublicKey(publicKey);
        return this;
    }

    public BigInteger getHeight() {
        return mHeight;
    }

    public TxSetHaltBlock setHeight(long height) {
        if (height < 1) {
            throw new IllegalArgumentException("Height must be positive number");
        }
        mHeight = BigInteger.valueOf(height);
        return this;
    }

    public TxSetHaltBlock setHeight(BigInteger height) {
        mHeight = height;
        return this;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPublicKey", mPublicKey != null, "Public key must be set")
                .addResult("mHeight", mHeight != null, "Block number must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPublicKey,
                mHeight
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mPublicKey = new MinterPublicKey(fromRawRlp(0, decoded));
        mHeight = fixBigintSignedByte(fromRawRlp(1, decoded));
    }
}
