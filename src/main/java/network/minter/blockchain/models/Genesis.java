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

import org.parceler.Parcel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2020
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
@Parcel
public class Genesis extends NodeResult {

    @SerializedName("genesis_time")
    public String genesisTime;
    @SerializedName("chain_id")
    public String chainId;
    @SerializedName("consensus_params")
    public ConsensusParams consensusParams;
    @SerializedName("app_hash")
    public String appHash;

    @Parcel
    public static class AppState {
        public List<AppStateAccount> accounts;
        public List<AppStateCandidate> candidates;
        public List<AppStateCoin> coins;
        @SerializedName("coins_count")
        public BigInteger coinCount;


    }

    @Parcel
    public static class AppStateCoin extends Coin {
        @SerializedName("reserve")
        public BigInteger reserveBalance;
        public long version;
    }

    @Parcel
    public static class AppStateAccount {
        public MinterAddress address;
        public AppStateAccountBalance balance;
        public BigInteger nonce;
    }

    @Parcel
    public static class AppStateAccountBalance {
        @SerializedName("coin")
        public BigInteger coinId;
        public BigInteger value;

        public BigDecimal getValue() {
            return Transaction.humanizeValue(value);
        }
    }

    @Parcel
    public static class AppStateCandidate {
        public long id;
        @SerializedName("control_address")
        public MinterAddress controlAddress;
        @SerializedName("owner_address")
        public MinterAddress ownerAddress;
        @SerializedName("reward_address")
        public MinterAddress rewardAddress;
        public int commission;
        @SerializedName("pub_key")
        public MinterPublicKey publicKey;
        public List<AppStateCandidateStake> stakes;
    }

    @Parcel
    public static class AppStateCandidateStake {
        @SerializedName("coin")
        public BigInteger coinId;
        public BigInteger value;
        @SerializedName("bip_value")
        public BigInteger bipValue;
        public MinterAddress owner;
    }

    @Parcel
    public static class ConsensusParams {
        public ConsensusParamsBlock block;
        public ConsensusParamsEvidence evidence;
        public ConsensusParamsValidator validator;
    }

    @Parcel
    public static class ConsensusParamsBlock {
        @SerializedName("max_bytes")
        public BigInteger maxBytes;
        @SerializedName("max_gas")
        public BigInteger maxGas;
        @SerializedName("time_iota_ms")
        public BigInteger timeIotaMs;
    }

    @Parcel
    public static class ConsensusParamsEvidence {
        @SerializedName("max_age_num_blocks")
        public BigInteger maxAgeNumBlocks;
        @SerializedName("max_age_duration")
        public BigInteger maxAgeDuration;
    }

    @Parcel
    public static class ConsensusParamsValidator {
        @SerializedName("public_key_types")
        public List<String> publicKeyTypes;
    }
}
