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

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxDelegate extends Operation {
    private MinterPublicKey mPubKey;
    private BigInteger mCoinId;
    private BigInteger mStake;

    public TxDelegate() {
    }

    public TxDelegate(Transaction rawTx) {
        super(rawTx);
    }

    public MinterPublicKey getPublicKey() {
        return mPubKey;
    }

    public TxDelegate setPublicKey(MinterPublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    public TxDelegate setPublicKey(String hexPubKey) {
        mPubKey = new MinterPublicKey(hexPubKey);
        return this;
    }

    public TxDelegate setPublicKey(byte[] publicKey) {
        mPubKey = new MinterPublicKey(publicKey);
        return this;
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxDelegate setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxDelegate setCoinId(long coinId) {
        return setCoinId(BigInteger.valueOf(coinId));
    }

    public BigInteger getStakeBigInteger() {
        return mStake;
    }

    public BigDecimal getStake() {
        return Transaction.humanizeValue(mStake);
    }

    public TxDelegate setStake(BigInteger amountNormalized) {
        mStake = amountNormalized;
        return this;
    }

    public TxDelegate setStake(@Nonnull final CharSequence decimalValue) {
        return setStake(new BigDecimal(decimalValue.toString()));
    }

    public TxDelegate setStake(BigDecimal amount) {
        mStake = normalizeValue(amount);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.Delegate;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mPubKey", mPubKey != null, "Node public key must be set")
                .addResult("mCoin", mCoinId != null, "Coin ID must be set")
                .addResult("mStake", mStake != null && mStake.compareTo(new BigInteger("0")) > 0, "Stake must be set (more than 0)");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mPubKey,
                mCoinId,
                mStake
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new MinterPublicKey(fromRawRlp(0, decoded));
        mCoinId = fixBigintSignedByte(fromRawRlp(1, decoded));
        mStake = fixBigintSignedByte(fromRawRlp(2, decoded));
    }


}

