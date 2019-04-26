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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class NetworkStatus {
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("latest_block_hash")
    @Expose
    public BytesData latestBlockHash;
    @SerializedName("latest_app_hash")
    @Expose
    public BytesData latestAppHash;
    @SerializedName("latest_block_height")
    @Expose
    public long latestBlockHeight;
    @SerializedName("latest_block_time")
    @Expose
    public String latestBlockTime;
    @SerializedName("state_history")
    @Expose
    public String stateHistory;
    @SerializedName("tm_status")
    @Expose
    public TmStatus tmStatus;

    @Parcel
    public static class NodeInfo {
        @SerializedName("protocol_version")
        @Expose
        public ProtocolVersion protocolVersion;
        @SerializedName("id")
        @Expose
        public BytesData id;
        @SerializedName("listen_addr")
        @Expose
        public String listenAddr;
        @SerializedName("network")
        @Expose
        public String network;
        @SerializedName("version")
        @Expose
        public String version;
        @SerializedName("channels")
        @Expose
        public String channels;
        @SerializedName("moniker")
        @Expose
        public String moniker;
        @SerializedName("other")
        @Expose
        public Other other;
    }

    @Parcel
    public static class Other {
        @SerializedName("tx_index")
        @Expose
        public String txIndex;
        @SerializedName("rpc_address")
        @Expose
        public String rpcAddress;
    }

    @Parcel
    public static class ProtocolVersion {
        @SerializedName("p2p")
        @Expose
        public long p2p;
        @SerializedName("block")
        @Expose
        public long block;
        @SerializedName("app")
        @Expose
        public long app;
    }

    @Parcel
    public static class PubKey {
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("value")
        @Expose
        public String value;
    }

    @Parcel
    public static class SyncInfo {
        @SerializedName("latest_block_hash")
        @Expose
        public BytesData latestBlockHash;
        @SerializedName("latest_app_hash")
        @Expose
        public BytesData latestAppHash;
        @SerializedName("latest_block_height")
        @Expose
        public long latestBlockHeight;
        @SerializedName("latest_block_time")
        @Expose
        public String latestBlockTime;
        @SerializedName("catching_up")
        @Expose
        public boolean catchingUp;
    }

    @Parcel
    public static class TmStatus {
        @SerializedName("node_info")
        @Expose
        public NodeInfo nodeInfo;
        @SerializedName("sync_info")
        @Expose
        public SyncInfo syncInfo;
        @SerializedName("validator_info")
        @Expose
        public ValidatorStatus validatorStatus;
    }

    @Parcel
    public static class Validator {
        @SerializedName("pub_key")
        @Expose
        public MinterPublicKey pubKey;
        @SerializedName("voting_power")
        @Expose
        public String votingPower;
    }

    @Parcel
    public static class ValidatorStatus {
        @SerializedName("address")
        @Expose
        public BytesData address;
        @SerializedName("pub_key")
        @Expose
        public PubKey pubKey;
        @SerializedName("voting_power")
        @Expose
        public String votingPower;

    }

}
