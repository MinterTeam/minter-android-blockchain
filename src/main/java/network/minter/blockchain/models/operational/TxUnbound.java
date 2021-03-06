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

import static network.minter.blockchain.models.operational.Transaction.humanizeValue;
import static network.minter.blockchain.models.operational.Transaction.normalizeValue;
import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.BytesHelper.fixBigintSignedByte;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxUnbound extends Operation {
    private MinterPublicKey mPubKey;
    private BigInteger mCoinId;
    private BigInteger mValue;

    public TxUnbound() {
    }

    public TxUnbound(Transaction rawTx) {
        super(rawTx);
    }

    public MinterPublicKey getPublicKey() {
        return mPubKey;
    }

    public TxUnbound setPublicKey(byte[] publicKey) {
        mPubKey = new MinterPublicKey(publicKey);
        return this;
    }

    public TxUnbound setPublicKey(MinterPublicKey publicKey) {
        mPubKey = publicKey;
        return this;
    }

    public TxUnbound setPublicKey(String hexPubKey) {
        mPubKey = new MinterPublicKey(hexPubKey);
        return this;
    }

    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxUnbound setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxUnbound setCoinId(long coinId) {
        return setCoinId(BigInteger.valueOf(coinId));
    }

    public BigInteger getValueBigInteger() {
        return mValue;
    }

    public BigDecimal getValue() {
        return humanizeValue(mValue);
    }

    public TxUnbound setValue(BigDecimal stakeDecimal) {
        mValue = normalizeValue(stakeDecimal);
        return this;
    }

    public TxUnbound setValue(BigInteger stakeBigInteger) {
        mValue = stakeBigInteger;
        return this;
    }

    public TxUnbound setValue(@Nonnull final CharSequence decimalValue) {
        checkNotNull(decimalValue);
        return setValue(new BigDecimal(decimalValue.toString()));
    }

    @Override
    public OperationType getType() {
        return OperationType.Unbound;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mCoin", mCoinId != null, "Coin must be set")
                .addResult("mPubKey", mPubKey != null, "Node Public key must be set")
                .addResult("mValue", mValue != null, "Value must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{mPubKey, mCoinId, mValue});
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        final DecodeResult rlp = RLPBoxed.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        mPubKey = new MinterPublicKey(fromRawRlp(0, decoded));
        mCoinId = fixBigintSignedByte(fromRawRlp(1, decoded));
        mValue = fixBigintSignedByte(fromRawRlp(2, decoded));
    }


}

