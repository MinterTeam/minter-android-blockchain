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

package network.minter.blockchain.models;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CandidateItem extends NodeResult {
    @SerializedName("reward_address")
    public MinterAddress rewardAddress;
    @SerializedName("owner_address")
    public MinterAddress ownerAddress;
    @SerializedName("control_address")
    public MinterAddress controlAddress;
    @SerializedName("total_stake")
    public BigInteger totalStake;
    @SerializedName("public_key")
    public MinterPublicKey publicKey;
    public int commission;
    @SerializedName("used_slots")
    public BigInteger usedSlots;
    @SerializedName("uniq_users")
    public BigInteger uniqueUsers;
    @SerializedName("min_stake")
    public BigInteger minStake;
    public List<StakeInfo> stakes;
    public Status status;

    public enum Status {
        @SerializedName("0")
        Unknown(0),
        @SerializedName("1")
        CandidateOff(1),
        @SerializedName("2")
        CandidateOn(2),
        @SerializedName("3")
        ValidatorOn(3);

        private final int val;

        Status(int v) {
            val = v;
        }

        public int getValue() {
            return val;
        }
    }

    public BigDecimal getTotalStakeDecimal() {
        return Transaction.humanizeValue(totalStake);
    }

    public static class StakeCoin {
        public BigInteger id;
        public String symbol;
    }

    public static class StakeInfo {
        public MinterAddress owner;
        public StakeCoin coin;
        public BigInteger value;
        public BigInteger bipValue;

        public BigDecimal getValue() {
            return Transaction.humanizeValue(value);
        }

        public BigDecimal getBipValueDecimal() {
            return Transaction.humanizeValue(bipValue);
        }
    }
}
