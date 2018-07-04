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

import network.minter.mintercore.MinterSDK;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.helpers.BytesHelper;
import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

import static network.minter.mintercore.internal.common.Preconditions.checkNotNull;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public final class TxSendCoin extends Operation {
    String coin = MinterSDK.DEFAULT_COIN;
    MinterAddress to;
    BigInteger value;

    TxSendCoin() {
    }

    public long getValue() {
        return value.divide(new BigInteger(String.valueOf(Transaction.VALUE_MUL))).longValue();
    }

    public BigInteger getValueBigInteger() {
        return value;
    }

    public MinterAddress getTo() {
        return new MinterAddress(to);
    }

    public String getCoin() {
        return coin.replace("\0", "");
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        byte[] to = this.to.getData();
        to = BytesHelper.lpad(20, to);

        return RLP.encode(new Object[]{coin, to, value});
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        coin = StringHelper.bytesToString(fromRawRlp(0, decoded));
        to = new MinterAddress(fromRawRlp(1, decoded));
        value = new BigInteger(fromRawRlp(2, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(
            Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxSendCoin>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxSendCoin> {
        Builder(Transaction<TxSendCoin> op) {
            super(op);
        }

        public Builder setCoin(final String coin) {
            TxSendCoin.this.coin = StringHelper.strrpad(10, coin.toUpperCase());
            return this;
        }

        public Builder setTo(MinterAddress address) {
            checkNotNull(address);
            to = address;
            return this;
        }

        public Builder setTo(String address) {
            checkNotNull(address);
            return setTo(new MinterAddress(address));
        }

        public Builder setTo(CharSequence address) {
            checkNotNull(address);
            return setTo(address.toString());
        }

        /**
         * You MUST multiply this rawValue on {@code Transaction#VALUE_MUL} by yourself
         * @param rawValue
         * @param radix
         * @return
         */
        public Builder setRawValue(String rawValue, int radix) {
            return setValue(new BigInteger(rawValue, radix));
        }

        /**
         *
         * @param decimalValue Floating point string value. Precision up to 18 digits: 0.10203040506078090
         * @return
         */
        public Builder setValue(@NonNull final CharSequence decimalValue) {
            checkNotNull(decimalValue);
            return setValue(new BigDecimal(decimalValue.toString()));
        }

        /**
         * Precision up to 18 digits
         * @see Transaction#VALUE_MUL
         * @param value
         * @return
         */
        public Builder setValue(BigDecimal value) {
            TxSendCoin.this.value = value.multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
            return this;
        }

        public Transaction<TxSendCoin> build() {
            getTx().setData(TxSendCoin.this);
            return getTx();
        }

        private Builder setValue(BigInteger value) {
            TxSendCoin.this.value = value.multiply(Transaction.VALUE_MUL);
            return this;
        }

    }


}
