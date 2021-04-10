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
import network.minter.core.util.RLPBoxed;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxMoveStake extends Operation {
    private MinterPublicKey mFrom, mTo;
    private BigInteger mCoinId;
    private BigInteger mStake;

    public TxMoveStake() {
    }

    public TxMoveStake(Transaction rawTx) {
        super(rawTx);
    }

    public MinterPublicKey getFrom() {
        return mFrom;
    }

    public TxMoveStake setFrom(MinterPublicKey publicKey) {
        mFrom = publicKey;
        return this;
    }

    public TxMoveStake setFrom(String hexPubKey) {
        mFrom = new MinterPublicKey(hexPubKey);
        return this;
    }

    public TxMoveStake setFrom(byte[] publicKey) {
        mFrom = new MinterPublicKey(publicKey);
        return this;
    }
    public MinterPublicKey getTo() {
        return mTo;
    }
    public TxMoveStake setTo(MinterPublicKey publicKey) {
        mTo = publicKey;
        return this;
    }
    public TxMoveStake setTo(String hexPubKey) {
        mTo = new MinterPublicKey(hexPubKey);
        return this;
    }
    public TxMoveStake setTo(byte[] publicKey) {
        mTo = new MinterPublicKey(publicKey);
        return this;
    }
    public BigInteger getCoinId() {
        return mCoinId;
    }

    public TxMoveStake setCoinId(BigInteger coinId) {
        mCoinId = coinId;
        return this;
    }

    public TxMoveStake setCoinId(long coinId) {
        return setCoinId(BigInteger.valueOf(coinId));
    }

    public BigInteger getStakeBigInteger() {
        return mStake;
    }

    public BigDecimal getStake() {
        return Transaction.humanizeValue(mStake);
    }

    public TxMoveStake setStake(BigInteger amountNormalized) {
        mStake = amountNormalized;
        return this;
    }

    public TxMoveStake setStake(@Nonnull final CharSequence decimalValue) {
        return setStake(new BigDecimal(decimalValue.toString()));
    }

    public TxMoveStake setStake(BigDecimal amount) {
        mStake = normalizeValue(amount);
        return this;
    }

    @Override
    public OperationType getType() {
        return OperationType.MoveStake;
    }

    @Nullable
    @Override
    protected FieldsValidationResult validate() {
        return new FieldsValidationResult()
                .addResult("mFrom", mFrom != null, "Node public key must be set")
                .addResult("mTo", mTo != null, "Node public key must be set")
                .addResult("mFrom", !mFrom.equals(mTo), "From and To cannot be equal")
                .addResult("mCoin", mCoinId != null, "Coin ID must be set");
    }

    @Nonnull
    @Override
    protected char[] encodeRLP() {
        return RLPBoxed.encode(new Object[]{
                mFrom,
                mTo,
                mCoinId,
                mStake
        });
    }

    @Override
    protected void decodeRLP(@Nonnull char[] rlpEncodedData) {
        RLPValues rlp = decodeValues(rlpEncodedData);
        mFrom = rlp.asPublicKey(0);
        mTo = rlp.asPublicKey(1);
        mCoinId = rlp.asBigInt(2);
        mStake = rlp.asBigInt(3);
    }


}

