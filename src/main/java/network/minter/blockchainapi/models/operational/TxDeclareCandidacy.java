/*
 * Copyright (C) 2018 by MinterTeam
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

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PublicKey;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxDeclareCandidacy extends Operation {

    MinterAddress address;
    PublicKey pubKey;
    Integer commission;
    String coin;
    BigInteger stake;

    public MinterAddress getAddress() {
        return address;
    }

    public PublicKey getPublicKey() {
        return pubKey;
    }

    public Integer getCommission() {
        return commission;
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
                address.getData(),
                pubKey.getData(),
                commission,
                coin,
                stake
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        address = new MinterAddress(fromRawRlp(0, decoded));
        pubKey = new PublicKey(fromRawRlp(1, decoded));
        commission = new BigInteger(fromRawRlp(2, decoded)).intValue();
        coin = StringHelper.bytesToString(fromRawRlp(3, decoded));
        stake = new BigInteger(fromRawRlp(4, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx) {
        return (B) new TxDeclareCandidacy.Builder((Transaction<TxDeclareCandidacy>) rawTx);
    }

    public class Builder extends Operation.Builder<TxDeclareCandidacy> {

        Builder(Transaction<TxDeclareCandidacy> op) {
            super(op);
        }

        public Builder setAddress(String address) {
            TxDeclareCandidacy.this.address = new MinterAddress(address);
            return this;
        }

        public Builder setAddress(MinterAddress address) {
            TxDeclareCandidacy.this.address = address;
            return this;
        }

        public Builder setPublicKey(PublicKey publicKey) {
            pubKey = publicKey;
            return this;
        }

        public Builder setPublicKey(String hexPubKey) {
            pubKey = new PublicKey(hexPubKey);
            return this;
        }

        public Builder setPublicKey(byte[] publicKey) {
            pubKey = new PublicKey(publicKey);
            return this;
        }

        public Builder setCommission(Integer commission) {
            checkArgument(commission >= 0, "Commission must be unsigned integer");
            TxDeclareCandidacy.this.commission = commission;
            return this;
        }

        public Builder setCoin(String coinName) {
            coin = StringHelper.strrpad(10, coinName.toUpperCase());
            return this;
        }

        public Builder setStake(BigInteger stakeBigInteger) {
            stake = stakeBigInteger;
            return this;
        }

        public Builder setStake(String stakeBigInteger) {
            stake = new BigInteger(stakeBigInteger);
            return this;
        }

        public Builder setStake(BigDecimal stakeDecimal) {
            stake = stakeDecimal.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
            return this;
        }

        public Transaction<TxDeclareCandidacy> build() {
            getTx().setData(TxDeclareCandidacy.this);
            return getTx();
        }
    }
}
