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

import network.minter.mintercore.internal.helpers.StringHelper;
import network.minter.mintercore.util.DecodeResult;
import network.minter.mintercore.util.RLP;

import static network.minter.mintercore.internal.common.Preconditions.checkArgument;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxCreateCoin extends Operation {
    String name;
    String symbol;
    BigInteger initialAmount;
    BigInteger initialReserve;
    // unsigned!!!
    Integer constantReserveRatio;

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigInteger getInitialAmount() {
        return initialAmount;
    }

    public BigInteger getInitialReserve() {
        return initialReserve;
    }

    public Integer getConstantReserveRatio() {
        return constantReserveRatio;
    }

    @NonNull
    @Override
    protected byte[] encodeRLP() {
        return RLP.encode(new Object[]{
                name,
                symbol,
                initialAmount,
                initialReserve,
                constantReserveRatio
        });
    }

    @Override
    protected void decodeRLP(@NonNull byte[] rlpEncodedData) {
        final DecodeResult rlp = RLP.decode(rlpEncodedData, 0);/**/
        final Object[] decoded = (Object[]) rlp.getDecoded();
        name = StringHelper.bytesToString(fromRawRlp(0, decoded));
        symbol = StringHelper.bytesToString(fromRawRlp(1, decoded));
        initialAmount = new BigInteger(fromRawRlp(2, decoded));
        initialReserve = new BigInteger(fromRawRlp(3, decoded));
        constantReserveRatio = new BigInteger(fromRawRlp(4, decoded)).intValue();
    }

    @Override
    protected <T extends Operation, B extends Operation.Builder<T>> B getBuilder(Transaction<? extends Operation> rawTx) {
        return (B) new Builder((Transaction<TxCreateCoin>) rawTx);
    }

    public final class Builder extends Operation.Builder<TxCreateCoin> {

        Builder(Transaction<TxCreateCoin> op) {
            super(op);
        }

        public Builder setName(String n) {
            name = n;
            return this;
        }

        public Builder setSymbol(String coinName) {
            symbol = StringHelper.strrpad(10, coinName.toUpperCase());
            return this;
        }

        public Builder setInitialAmount(BigInteger amount) {
            initialAmount = amount;
            return this;
        }

        public Builder setInitialAmount(double amount) {
            return setInitialAmount(new BigDecimal(amount));
        }

        public Builder setInitialAmount(BigDecimal amount) {
            return setInitialAmount(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
        }

        public Builder setInitialReserve(BigInteger amount) {
            initialReserve = amount;
            return this;
        }

        public Builder setInitialReserve(double amount) {
            return setInitialReserve(new BigDecimal(amount));
        }

        public Builder setInitialReserve(BigDecimal amount) {
            return setInitialReserve(amount.multiply(Transaction.VALUE_MUL_DEC).toBigInteger());
        }

        public Builder setConstantReverveRatio(Integer ratio) {
            checkArgument(ratio >= 0, "Ratio must be unsigned integer");
            constantReserveRatio = ratio;
            return this;
        }

        public Transaction<TxCreateCoin> build() {
            getTx().setData(TxCreateCoin.this);
            return getTx();
        }
    }
}
