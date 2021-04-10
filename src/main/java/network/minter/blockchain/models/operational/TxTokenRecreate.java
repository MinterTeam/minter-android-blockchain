/*
 * Copyright (C) by MinterTeam. 2021
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
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxTokenRecreate extends TxTokenCreate {

    public TxTokenRecreate() {
        super();
    }

    public TxTokenRecreate(Transaction rawTx) {
        super(rawTx);
    }

    @Override
    public TxTokenCreate setName(String name) {
        return super.setName(name);
    }

    @Override
    public TxTokenCreate setSymbol(String coinName) {
        return super.setSymbol(coinName);
    }

    @Override
    public TxTokenCreate setInitialAmount(String amountDecimal) {
        return super.setInitialAmount(amountDecimal);
    }

    @Override
    public TxTokenCreate setInitialAmount(BigDecimal amount) {
        return super.setInitialAmount(amount);
    }

    @Override
    public TxTokenCreate setInitialAmount(BigInteger amount) {
        return super.setInitialAmount(amount);
    }

    @Override
    public TxTokenCreate setMaxSupply(BigInteger maxSupply) {
        return super.setMaxSupply(maxSupply);
    }

    @Override
    public TxTokenCreate setMaxSupply(BigDecimal maxSupply) {
        return super.setMaxSupply(maxSupply);
    }

    @Override
    public TxTokenCreate setMaxSupply(String maxSupply) {
        return super.setMaxSupply(maxSupply);
    }

    @Override
    public TxTokenCreate setIsMintable(boolean mintable) {
        return super.setIsMintable(mintable);
    }

    @Override
    public TxTokenCreate setIsBurnable(boolean burnable) {
        return super.setIsBurnable(burnable);
    }

    @Override
    public OperationType getType() {
        return OperationType.RecreateToken;
    }
}
