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

import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class TxConvertCoin extends Operation {

    String fromCoin;
    String toCoin;
    BigInteger value;

    public String getFromCoin() {
        return fromCoin;
    }

    public String getToCoin() {
        return toCoin;
    }

    public BigInteger getValue() {
        return value;
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                fromCoin,
                toCoin,
                value
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        fromCoin = StringHelper.bytesToString(fromRawRlp(0, decoded));
        toCoin = StringHelper.bytesToString(fromRawRlp(1, decoded));
        value = new BigInteger(fromRawRlp(2, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(
            Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxConvertCoin>) rawTx);
    }


    public final class Builder extends Operation.Builder<TxConvertCoin> {

        Builder(Transaction<TxConvertCoin> op) {
            super(op);
        }

        public Builder setFromCoin(String coin) {
            fromCoin = StringHelper.strrpad(10, coin.toUpperCase());
            return this;
        }

        public Builder setToCoin(String coin) {
            toCoin = StringHelper.strrpad(10, coin.toUpperCase());
            return this;
        }

        public Builder setAmount(BigInteger amount) {
            value = amount;
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            return setAmount(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
        }

        public Transaction<TxConvertCoin> build() {
            getTx().setData(TxConvertCoin.this);
            return getTx();
        }
    }
}
