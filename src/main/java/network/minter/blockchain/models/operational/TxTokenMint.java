/*
 * Copyright (C) by MinterTeam. 2021
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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.util.RLPBoxed;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxTokenMint extends Operation {
    private BigInteger mCoinId;
    private BigInteger mValue;

    public TxTokenMint() {
    }

    public TxTokenMint(Transaction rawTx) {
        super(rawTx);
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxTokenMint setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxTokenMint setCoinId(long coinId) {
        mCoinId = BigInteger.valueOf(coinId);
        return this;
    }

    public BigDecimal getValue() {
        return Transaction.humanizeValue(mValue);
    }

    public TxTokenMint setValue(BigInteger value) {
        mValue = value;
        return this;
    }

    public TxTokenMint setValue(BigDecimal value) {
        mValue = Transaction.normalizeValue(value);
        return this;
    }

    public TxTokenMint setValue(CharSequence value) {
        mValue = Transaction.normalizeValue(value);
        return this;
    }

    public BigInteger getValueBigInteger() {
        return mValue;
    }

    @Override
    public OperationType getType() {
        return OperationType.MintToken;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoinId", mCoinId != null, "Coin must be set")
                .addResult("mValue", mValue != null, "Value must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        mCoinId = rlp.asBigInt(0);
        mValue = rlp.asBigInt(1);
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mCoinId,
                mValue
        });
    }
}
