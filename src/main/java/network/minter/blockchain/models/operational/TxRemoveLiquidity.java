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
public class TxRemoveLiquidity extends Operation {
    private BigInteger mCoin0;
    private BigInteger mCoin1;
    private BigInteger mLiquidity;
    private BigInteger mMinimumVolume0 = BigInteger.ZERO;
    private BigInteger mMinimumVolume1 = BigInteger.ZERO;

    public TxRemoveLiquidity() {
    }

    public TxRemoveLiquidity(Transaction rawTx) {
        super(rawTx);
    }

    public BigInteger getCoin0() {
        return mCoin0;
    }

    public TxRemoveLiquidity setCoin0(BigInteger coinId) {
        mCoin0 = coinId;
        return this;
    }

    public TxRemoveLiquidity setCoin0(long coinId) {
        mCoin0 = BigInteger.valueOf(coinId);
        return this;
    }


    public BigInteger getCoin1() {
        return mCoin1;
    }

    public TxRemoveLiquidity setCoin1(BigInteger coinId) {
        mCoin1 = coinId;
        return this;
    }

    public TxRemoveLiquidity setCoin1(long coinId) {
        mCoin1 = BigInteger.valueOf(coinId);
        return this;
    }

    public BigDecimal getLiquidity() {
        return Transaction.humanizeValue(mLiquidity);
    }
    public TxRemoveLiquidity setLiquidity(BigDecimal valueHuman) {
        mLiquidity = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public TxRemoveLiquidity setLiquidity(CharSequence valueHuman) {
        mLiquidity = Transaction.normalizeValue(valueHuman);
        return this;
    }
    public TxRemoveLiquidity setLiquidity(BigInteger value) {
        mLiquidity = value;
        return this;
    }
    public BigInteger getLiquidityBigInteger() {
        return mLiquidity;
    }
    public BigDecimal getMinVolume0() {
        return Transaction.humanizeValue(mMinimumVolume0);
    }

    public TxRemoveLiquidity setMinVolume0(BigInteger value) {
        mMinimumVolume0 = value;
        return this;
    }

    public TxRemoveLiquidity setMinVolume0(BigDecimal valueHuman) {
        mMinimumVolume0 = Transaction.normalizeValue(valueHuman);
        return this;
    }

    public TxRemoveLiquidity setMinVolume0(CharSequence valueHuman) {
        mMinimumVolume0 = Transaction.normalizeValue(valueHuman);
        return this;
    }

    public BigInteger getMinVolume1BigInteger() {
        return mMinimumVolume1;
    }

    public BigDecimal getMinVolume1() {
        return Transaction.humanizeValue(mMinimumVolume1);
    }

    public TxRemoveLiquidity setMinVolume1(BigInteger value) {
        mMinimumVolume1 = value;
        return this;
    }

    public TxRemoveLiquidity setMinVolume1(BigDecimal valueHuman) {
        mMinimumVolume1 = Transaction.normalizeValue(valueHuman);
        return this;
    }

    public TxRemoveLiquidity setMinVolume1(CharSequence valueHuman) {
        mMinimumVolume1 = Transaction.normalizeValue(valueHuman);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.RemoveLiquidity;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("coin", !mCoin0.equals(mCoin1), "Coin0 and Coin1 are equals")
                .addResult("coin0", mCoin0 != null, "Coin 0 must be set")
                .addResult("coin1", mCoin1 != null, "Coin 1 must be set")
                .addResult("liquidity", mLiquidity != null, "liquidity must be set")
                .addResult("mMinimumVolume0", mMinimumVolume0 != null, "Min volume 0 must be set")
                .addResult("mMinimumVolume1", mMinimumVolume1 != null, "Min volume 1 must be set");
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        mCoin0 = fixBigintSignedByte(fromRawRlp(0, decoded));
        mCoin1 = fixBigintSignedByte(fromRawRlp(1, decoded));
        mLiquidity = fixBigintSignedByte(fromRawRlp(2, decoded));
        mMinimumVolume0 = fixBigintSignedByte(fromRawRlp(3, decoded));
        mMinimumVolume1 = fixBigintSignedByte(fromRawRlp(4, decoded));
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mCoin0,
                mCoin1,
                mLiquidity,
                mMinimumVolume0,
                mMinimumVolume1
        });
    }
}
