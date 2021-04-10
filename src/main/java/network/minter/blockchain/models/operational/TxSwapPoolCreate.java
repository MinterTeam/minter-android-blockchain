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

import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxSwapPoolCreate extends Operation {
    private BigInteger mCoin0, mCoin1;
    private BigInteger mVolume0, mVolume1;

    public TxSwapPoolCreate() {
    }

    public TxSwapPoolCreate(Transaction rawTx) {
        super(rawTx);
    }
    public BigInteger getCoin0() {
        return mCoin0;
    }
    public TxSwapPoolCreate setCoin0(BigInteger coinId) {
        mCoin0 = coinId;
        return this;
    }
    public TxSwapPoolCreate setCoin0(long coinId) {
        mCoin0 = BigInteger.valueOf(coinId);
        return this;
    }
    public BigInteger getCoin1() {
        return mCoin1;
    }
    public TxSwapPoolCreate setCoin1(BigInteger coinId) {
        mCoin1 = coinId;
        return this;
    }
    public TxSwapPoolCreate setCoin1(long coinId) {
        mCoin1 = BigInteger.valueOf(coinId);
        return this;
    }
    public BigDecimal getVolume0() {
        return Transaction.humanizeValue(mVolume0);
    }
    public TxSwapPoolCreate setVolume0(BigInteger volume0) {
        mVolume0 = volume0;
        return this;
    }
    public TxSwapPoolCreate setVolume0(BigDecimal valueHuman) {
        mVolume0 = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public TxSwapPoolCreate setVolume0(CharSequence valueHuman) {
        mVolume0 = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public BigInteger getVolume0BigInteger() {
        return mVolume0;
    }
    public BigDecimal getVolume1() {
        return Transaction.humanizeValue(mVolume1);
    }
    public TxSwapPoolCreate setVolume1(BigInteger volume1) {
        mVolume1 = volume1;
        return this;
    }
    public TxSwapPoolCreate setVolume1(BigDecimal valueHuman) {
        mVolume1 = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public TxSwapPoolCreate setVolume1(CharSequence valueHuman) {
        mVolume1 = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public BigInteger getVolume1BigInteger() {
        return mVolume1;
    }
    @Override
    public OperationType getType() {
        return OperationType.CreateSwapPool;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("coin", !mCoin0.equals(mCoin1), "Cannot create pool with same coins");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoin0 = fixBigintSignedByte(fromRawRlp(0, decoded));
        mCoin1 = fixBigintSignedByte(fromRawRlp(1, decoded));
        mVolume0 = fixBigintSignedByte(fromRawRlp(2, decoded));
        mVolume1 = fixBigintSignedByte(fromRawRlp(3, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mCoin0,
                mCoin1,
                mVolume0,
                mVolume1
        });
    }
}
