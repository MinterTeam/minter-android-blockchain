/*
 * Copyright (C) by MinterTeam. 2018
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

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.mintercore.crypto.PublicKey;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class TxDelegate extends Operation {
    PublicKey pubKey;
    String coin;
    BigInteger stake;

    public PublicKey getPublicKey() {
        return pubKey;
    }

    public String getCoin() {
        return coin;
    }

    public BigInteger getStake() {
        return stake;
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                pubKey.getData(),
                coin,
                stake
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        pubKey = new PublicKey(fromRawRlp(0, decoded));
        coin = StringHelper.bytesToString(fromRawRlp(1, decoded));
        stake = new BigInteger(fromRawRlp(2, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxDelegate>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxDelegate> {

        Builder(Transaction<TxDelegate> op) {
            super(op);
        }

        public TxDelegate.Builder setPublicKey(PublicKey publicKey) {
            pubKey = publicKey;
            return this;
        }

        public TxDelegate.Builder setPublicKey(String hexPubKey) {
            pubKey = new PublicKey(hexPubKey);
            return this;
        }

        public TxDelegate.Builder setPublicKey(byte[] publicKey) {
            pubKey = new PublicKey(publicKey);
            return this;
        }

        public TxDelegate.Builder setCoin(String coinName) {
            coin = StringHelper.strrpad(10, coinName.toUpperCase());
            return this;
        }

        public TxDelegate.Builder setStake(BigInteger stakeBigInteger) {
            stake = stakeBigInteger;
            return this;
        }

        public TxDelegate.Builder setStake(String stakeBigInteger) {
            stake = new BigInteger(stakeBigInteger);
            return this;
        }

        public TxDelegate.Builder setStake(BigDecimal stakeDecimal) {
            stake = stakeDecimal.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
            return this;
        }

        public Transaction<TxDelegate> build() {
            getTx().setData(TxDelegate.this);
            return getTx();
        }


    }


}

