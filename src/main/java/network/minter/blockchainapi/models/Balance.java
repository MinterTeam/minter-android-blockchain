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

package network.minter.blockchainapi.models;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

import network.minter.blockchainapi.models.operational.Transaction;

/**
 * MinterCore. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class Balance {
    public Map<String, CoinBalance> coins;

    public CoinBalance get(String coin) {
        return coins.get(coin.toUpperCase());
    }

    public BigDecimal getFor(String coin) {
        if (!coins.containsKey(coin.toUpperCase())) {
            return new BigDecimal("0");
        }

        return coins.get(coin.toUpperCase()).getBalance();
    }

    public static class CoinBalance {
        public String coin;
        public BigInteger balance;

        /**
         * @return Coin name
         */
        public String getCoin() {
            return coin;
        }

        /**
         * @return Current balance in coins for specified address
         * @throws NumberFormatException
         */
        public BigDecimal getBalance() {
            return new BigDecimal(balance).setScale(18, RoundingMode.UNNECESSARY).divide(Transaction.VALUE_MUL_DEC, BigDecimal.ROUND_UNNECESSARY);
        }

        public void setBalance(BigDecimal b) {
            balance = b.setScale(18, RoundingMode.UNNECESSARY).multiply(Transaction.VALUE_MUL_DEC).toBigInteger();
        }

        @Override
        public int hashCode() {
            return Objects.hash(coin, balance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoinBalance balance1 = (CoinBalance) o;
            return Objects.equals(coin, balance1.coin) &&
                    Objects.equals(balance, balance1.balance);
        }
    }


}
