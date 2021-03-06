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

import java.math.BigInteger;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */

public class NetworkStatus extends NodeResult {

    @SerializedName("version")
    public String version;
    @SerializedName("latest_block_hash")
    public BytesData latestBlockHash;
    @SerializedName("latest_app_hash")
    public BytesData latestAppHash;
    @SerializedName("latest_block_height")
    public long latestBlockHeight;
    @SerializedName("latest_block_time")
    public String latestBlockTime;
    @SerializedName("keep_last_states")
    public long keepLastStates;
    @SerializedName("total_slashed")
    public BigInteger totalSlashed;
    @SerializedName("catching_up")
    public boolean catchingUp;
    @SerializedName("public_key")
    public MinterPublicKey publicKey;
    @SerializedName("node_id")
    public String nodeId;


}
