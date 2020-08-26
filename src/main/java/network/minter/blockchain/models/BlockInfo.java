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
import java.util.ArrayList;
import java.util.List;

import network.minter.blockchain.models.operational.Transaction;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class BlockInfo extends NodeResult {
    @SerializedName("hash")
    public BytesData hash;

    @SerializedName("height")
    public long height;

    @SerializedName("time")
    public String time;

    @SerializedName("transaction_count")
    public BigInteger transactionCount;

    @SerializedName("transactions")
    public List<HistoryTransaction> transactions = new ArrayList<>(0);

    @SerializedName("block_reward")
    public BigInteger blockReward;

    @SerializedName("size")
    public BigInteger size;

    @SerializedName("proposer")
    public MinterPublicKey proposer;

    @SerializedName("validators")
    public List<BlockInfoValidator> validators = new ArrayList<>(0);
    public BlockInfoEvidenceList evidence;
    public List<String> missed;

    public BigDecimal getBlockRewardDecimal() {
        return Transaction.humanizeValue(blockReward);
    }

    @Parcel
    public static class BlockInfoValidator {
        @SerializedName("public_key")
        public MinterPublicKey publicKey;
        public boolean signed;
    }

    @Parcel
    public static class BlockInfoEvidence {
        public BigInteger height;
        public String time;
        public MinterAddress address;
        public String hash;
    }

    @Parcel
    public static class BlockInfoEvidenceList {
        @SerializedName("evidence")
        public List<BlockInfoEvidence> items;
    }
}
