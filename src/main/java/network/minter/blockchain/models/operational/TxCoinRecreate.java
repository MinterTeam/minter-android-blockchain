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

import static network.minter.blockchain.models.operational.Transaction.normalizeValue;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */

public class TxCoinRecreate extends TxCoinCreate {
    public TxCoinRecreate() {
    }

    public TxCoinRecreate(@Nonnull Transaction rawTx) {
        super(rawTx);
    }

    @Override
    public OperationType getType() {
        return OperationType.RecreateCoin;
    }

    public TxCoinRecreate setName(String name) {
        super.setName(name);
        return this;
    }

    public TxCoinRecreate setSymbol(String coinName) {
        super.setSymbol(coinName);
        return this;
    }

    public TxCoinRecreate setInitialAmount(String amountDecimal) {
        super.setInitialAmount(new BigDecimal(amountDecimal));
        return this;
    }

    public TxCoinRecreate setInitialAmount(BigDecimal amount) {
        super.setInitialAmount(normalizeValue(amount));
        return this;
    }

    public TxCoinRecreate setInitialAmount(BigInteger amount) {
        super.setInitialAmount(amount);
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     *
     * @param maxSupply
     * @return self
     */
    public TxCoinRecreate setMaxSupply(BigInteger maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    /**
     * Coin purchase will not be possible if the limit is exceeded
     *
     * @param maxSupply Coin HardCap
     * @return self
     */
    public TxCoinRecreate setMaxSupply(BigDecimal maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    public TxCoinRecreate setMaxSupply(String maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    public TxCoinRecreate setInitialReserve(BigDecimal amount) {
        return setInitialReserve(normalizeValue(amount));
    }

    public TxCoinRecreate setInitialReserve(BigInteger amount) {
        super.setInitialReserve(amount);
        return this;
    }

    public TxCoinRecreate setInitialReserve(String amountDecimal) {
        return setInitialReserve(new BigDecimal(amountDecimal));
    }
}
