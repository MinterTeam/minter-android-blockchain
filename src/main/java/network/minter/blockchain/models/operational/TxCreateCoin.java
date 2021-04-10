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

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 * @deprecated Use TxCoinCreate instead for unified class names
 */
@Deprecated
public class TxCreateCoin extends TxCoinCreate {
    public TxCreateCoin() {
        super();
    }

    public TxCreateCoin(Transaction rawTx) {
        super(rawTx);
    }


    @Override
    public TxCreateCoin setName(String name) {
        super.setName(name);
        return this;
    }

    @Override
    public TxCreateCoin setSymbol(String coinName) {
        super.setSymbol(coinName);
        return this;
    }

    @Override
    public TxCreateCoin setInitialAmount(String amountDecimal) {
        super.setInitialAmount(amountDecimal);
        return this;
    }

    @Override
    public TxCreateCoin setInitialAmount(BigDecimal amount) {
        super.setInitialAmount(amount);
        return this;
    }

    @Override
    public TxCreateCoin setInitialAmount(BigInteger amount) {
        super.setInitialAmount(amount);
        return this;
    }

    @Override
    public TxCreateCoin setMaxSupply(BigInteger maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    @Override
    public TxCreateCoin setMaxSupply(BigDecimal maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    @Override
    public TxCreateCoin setMaxSupply(String maxSupply) {
        super.setMaxSupply(maxSupply);
        return this;
    }

    @Override
    public TxCreateCoin setInitialReserve(BigDecimal amount) {
        super.setInitialReserve(amount);
        return this;
    }

    @Override
    public TxCreateCoin setInitialReserve(BigInteger amount) {
        super.setInitialReserve(amount);
        return this;
    }

    @Override
    public TxCreateCoin setInitialReserve(String amountDecimal) {
        super.setInitialReserve(amountDecimal);
        return this;
    }

    @Override
    public TxCreateCoin setConstantReserveRatio(Integer ratio) {
        super.setConstantReserveRatio(ratio);
        return this;
    }
}
