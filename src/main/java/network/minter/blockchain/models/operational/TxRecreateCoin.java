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

import android.os.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */

public class TxRecreateCoin extends TxCreateCoin {

    public static final Creator<TxRecreateCoin> CREATOR = new Creator<TxRecreateCoin>() {
        @Override
        public TxRecreateCoin createFromParcel(Parcel in) {
            return new TxRecreateCoin(in);
        }

        @Override
        public TxRecreateCoin[] newArray(int size) {
            return new TxRecreateCoin[size];
        }
    };

    public TxRecreateCoin() {
    }

    public TxRecreateCoin(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    public TxRecreateCoin(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    @Override
    public OperationType getType() {
        return OperationType.RecreateCoin;
    }

    public TxRecreateCoin setName(String name) {
        super.setName(name);
        return this;
    }

    public TxRecreateCoin setSymbol(String coinName) {
        super.setSymbol(coinName);
        return this;
    }

    public TxRecreateCoin setInitialAmount(String amountDecimal) {
        super.setInitialAmount(new BigDecimal(amountDecimal));
        return this;
    }

    public TxRecreateCoin setInitialAmount(BigDecimal amount) {
        super.setInitialAmount(normalizeValue(amount));
        return this;
    }

    public TxRecreateCoin setInitialAmount(BigInteger amount) {
        super.setInitialAmount(amount);
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     * @param maxSupply
     * @return self
     */
    public TxRecreateCoin setMaxSupply(BigInteger maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     * @param maxSupply Coin HardCap
     * @return self
     */
    public TxRecreateCoin setMaxSupply(BigDecimal maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    public TxRecreateCoin setMaxSupply(String maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    public TxRecreateCoin setInitialReserve(BigDecimal amount) {
        return setInitialReserve(normalizeValue(amount));
    }

    public TxRecreateCoin setInitialReserve(BigInteger amount) {
        super.setInitialReserve(amount);
        return this;
    }

    public TxRecreateCoin setInitialReserve(String amountDecimal) {
        return setInitialReserve(new BigDecimal(amountDecimal));
    }
}
