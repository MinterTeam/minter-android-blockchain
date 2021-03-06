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

package network.minter.blockchain.repo;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import io.reactivex.Observable;
import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.api.NodeTransactionEndpoint;
import network.minter.blockchain.models.HistoryTransaction;
import network.minter.blockchain.models.HistoryTransactionList;
import network.minter.blockchain.models.TransactionCommissionValue;
import network.minter.blockchain.models.TransactionSendResult;
import network.minter.blockchain.models.UnconfirmedTransactions;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;

import static network.minter.core.internal.common.Preconditions.checkArgument;
import static network.minter.core.internal.common.Preconditions.checkNotNull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class NodeTransactionRepository extends DataRepository<NodeTransactionEndpoint> implements DataRepository.Configurator {
    public NodeTransactionRepository(@Nonnull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    /**
     * Get transactions by query
     * @param query
     * @return
     * @see TQuery
     */
    public Observable<HistoryTransactionList> getTransactions(@Nonnull TQuery query) {
        return getInstantService(this).getTransactions(checkNotNull(query, "Query required").build());
    }

    /**
     * Get full transaction information
     * @param hash Valid transaction hash
     * @return
     * @see #getTransaction(String)
     */
    public Observable<HistoryTransaction> getTransaction(MinterHash hash) {
        return getTransaction(hash.toString());
    }

    /**
     * SendCoin transaction
     * @param transactionSign Raw signed TX
     * @return Prepared request
     * @see TransactionSendResult
     */
    public Observable<TransactionSendResult> sendTransaction(@Nonnull TransactionSign transactionSign) {
        //TODO: move it to sign
        String sig = transactionSign.getTxSign();
        if (!sig.startsWith("0x")) {
            sig = "0x" + sig;
        }
        transactionSign.clear();
        return getInstantService().sendTransaction(sig);
    }

    /**
     * Get full transaction information
     * @param txHash Valid transaction hash with prefix "0x"
     * @return
     */
    public Observable<HistoryTransaction> getTransaction(String txHash) {
        return getInstantService(this).getTransaction(txHash);
    }

    @Override
    public void configure(ApiService.Builder api) {
        api.registerTypeAdapter(HistoryTransaction.class, new HistoryTransactionDeserializer());
        api.registerTypeAdapter(TransactionSign.class, new TransactionSignDeserializer());
    }

    /**
     * Resolve transaction commission before sending it
     * @param sign Transaction sign is NOT A TRANSACTION HASH, it's a valid transaction and it's ready to send
     * @return
     * @see network.minter.blockchain.models.operational.Transaction#signSingle(PrivateKey)
     */
    public Observable<TransactionCommissionValue> getTransactionCommission(TransactionSign sign) {
        return getTransactionCommission(sign.getTxSign());
    }

    /**
     * Resolve transaction commission before sending it
     * @param sign
     * @return
     */
    public Observable<TransactionCommissionValue> getTransactionCommission(String sign) {
        checkArgument(sign != null && sign.length() > 2, "Invalid signature");
        if (!sign.substring(0, 2).toLowerCase().equals("0x")) {
            return getInstantService().getTxCommission("0x" + sign);
        }

        return getInstantService().getTxCommission(sign);
    }

    /**
     * Get unconfirmed transactions signatures
     * Use result as collection
     * @return
     */
    public Observable<UnconfirmedTransactions> getUnconfirmedList() {
        return getInstantService().getUnconfirmed(null);
    }

    /**
     * Get unconfirmed transactions signatures
     * Use result as collection
     * @return
     */
    public Observable<UnconfirmedTransactions> getUnconfirmedList(int limit) {
        return getInstantService().getUnconfirmed(limit);
    }

    @Nonnull
    @Override
    protected Class<NodeTransactionEndpoint> getServiceClass() {
        return NodeTransactionEndpoint.class;
    }

    public static final class TransactionSignDeserializer implements JsonDeserializer<TransactionSign> {

        @Override
        public TransactionSign deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonNull() || !json.isJsonPrimitive()) {
                return null;
            }

            String sig = json.getAsString();
            return new TransactionSign(sig);
        }
    }

    public static final class HistoryTransactionDeserializer implements JsonDeserializer<HistoryTransaction> {

        @Override
        public HistoryTransaction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull() || !json.isJsonObject()) {
                return null;
            }

            final Gson gson = MinterBlockChainSDK.getInstance().getGsonBuilder().create();

            final HistoryTransaction out = gson.fromJson(json, HistoryTransaction.class);

            if (json.getAsJsonObject().has("data")) {
                JsonObject data = json.getAsJsonObject().get("data").getAsJsonObject();
                if (out.type == null) {
                    throw new IllegalStateException(String.format("Unknown transaction type %s", json.getAsJsonObject().get("type").getAsString()));
                }
                out.data = gson.fromJson(data, out.type.getOpClass());
            }

            return out;
        }
    }

    /**
     * Transaction getting query (@TODO documentation)
     * @link https://github.com/MinterTeam/minter-wiki/wiki/Minter-Node-JSON-API
     */
    public static class TQuery {
        private final Map<String, String> mData = new HashMap<>();

        public TQuery setFrom(MinterAddress from) {
            return setFrom(from.toString());
        }

        public TQuery setTo(MinterAddress to) {
            return setTo(to.toString());
        }

        public TQuery setTo(String to) {
            mData.put("tags.tx.to", normalizeAddress(to));
            return this;
        }

        public TQuery setFrom(String from) {
            mData.put("tags.tx.from", normalizeAddress(from));
            return this;
        }

        public TQuery setCustomTag(String key, String value) {
            mData.put(key, value);
            return this;
        }

        public String build() {
            StringBuilder out = new StringBuilder();

            int i = 0;
            for (Map.Entry<String, String> v : mData.entrySet()) {
                out.append(v.getKey()).append("=").append("'").append(v.getValue()).append("'");
                if (i + 1 < mData.size()) {
                    out.append('&');
                }
            }

            return out.toString();
        }

        private String normalizeAddress(String in) {
            final String prefix = in.substring(0, 2);
            if (prefix.equals(MinterSDK.PREFIX_ADDRESS) ||
                    prefix.equals(MinterSDK.PREFIX_ADDRESS.toLowerCase()) || prefix.equals("0x")) {
                return in.substring(2);
            }

            return in;
        }


    }
}
