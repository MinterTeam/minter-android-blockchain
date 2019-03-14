/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.blockchain.models;

import com.google.common.base.Objects;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Map;

import network.minter.blockchain.models.operational.Transaction;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@Parcel
public class Balance {
    public Map<String, CoinBalance> coins;
    @SerializedName("transaction_count")
    public BigInteger txCount;

    public CoinBalance get(String coin) {
        return coins.get(coin.toUpperCase());
    }

    public BigDecimal getFor(String coin) {
        if (!coins.containsKey(coin.toUpperCase())) {
            return new BigDecimal("0");
        }

        return coins.get(coin.toUpperCase()).getBalance();
    }

    @Parcel
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

        @Override
        public int hashCode() {
            return Objects.hashCode(coin, balance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoinBalance balance1 = (CoinBalance) o;
            return Objects.equal(coin, balance1.coin) &&
                    Objects.equal(balance, balance1.balance);
        }
    }


}
