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

package network.minter.blockchain.models.operational;

import android.support.annotation.NonNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.core.internal.helpers.StringHelper;
import network.minter.core.util.DecodeResult;
import network.minter.core.util.RLP;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCoinBuy extends Operation {

    String coinToBuy;
    BigInteger valueToBuy;
    String coinToSell;

    public String getCoinToBuy() {
        return coinToBuy.replace("\0", "");
    }

    public String getCoinToSell() {
        return coinToSell.replace("\0", "");
    }

    public BigInteger getValueBigInteger() {
        return valueToBuy;
    }

    public BigDecimal getValueToBuy() {
        return Transaction.VALUE_MUL_DEC.divide(new BigDecimal(valueToBuy));
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                coinToBuy,
                valueToBuy,
                coinToSell
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();

        coinToBuy = StringHelper.bytesToString(fromRawRlp(0, decoded));
        valueToBuy = new BigInteger(fromRawRlp(2, decoded));
        coinToSell = StringHelper.bytesToString(fromRawRlp(1, decoded));
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(
            Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxCoinBuy>) rawTx);
    }


    public final class Builder extends Operation.Builder<TxCoinBuy> {

        Builder(Transaction<TxCoinBuy> op) {
            super(op);
        }

        public Builder setCoinToBuy(String coin) {
            coinToBuy = StringHelper.strrpad(10, coin.toUpperCase());
            return this;
        }

        public Builder setCoinToSell(String coin) {
            coinToSell = StringHelper.strrpad(10, coin.toUpperCase());
            return this;
        }

        public Builder setValueToBuy(BigInteger amount) {
            valueToBuy = amount;
            return this;
        }

        public Builder setValueToBuy(BigDecimal amount) {
            return setValueToBuy(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
        }

        public Transaction<TxCoinBuy> build() {
            getTx().setData(TxCoinBuy.this);
            return getTx();
        }
    }
}
