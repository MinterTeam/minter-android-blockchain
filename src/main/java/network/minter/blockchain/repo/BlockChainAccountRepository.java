/*
 * Copyright (C) by MinterTeam. 2018
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

import android.support.annotation.NonNull;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import network.minter.blockchain.api.BlockChainAccountEndpoint;
import network.minter.blockchain.models.BCResult;
import network.minter.blockchain.models.Balance;
import network.minter.blockchain.models.TransactionSendResult;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.data.DataRepository;
import retrofit2.Call;

import static network.minter.core.internal.common.Preconditions.checkNotNull;
import static network.minter.core.internal.helpers.CollectionsHelper.asMap;

/**
 * minter-android-blockchain. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class BlockChainAccountRepository extends DataRepository<BlockChainAccountEndpoint> {
    public BlockChainAccountRepository(@NonNull ApiService.Builder apiBuilder) {
        super(apiBuilder);
    }

    public Call<BCResult<Balance>> getBalance(@NonNull MinterAddress key) {
        checkNotNull(key, "Public key required!");
        return getBalance(key.toString());
    }

    /**
     * Returns balance result data for specified address
     *
     * @param address
     * @return
     */
    public Call<BCResult<Balance>> getBalance(@NonNull String address) {
        return getInstantService(api -> {
            api.registerTypeAdapter(Balance.class, new CoinBalanceDeserializer());
        }).getBalance(checkNotNull(address, "Address required!"));
    }

    public Call<BCResult<BigInteger>> getTransactionCount(@NonNull MinterAddress key) {
        checkNotNull(key, "Public key required!");
        return getTransactionCount(key.toString());
    }

    /**
     * Returns the number of transactions sent from an address
     *
     * @param address fq address
     * @return Prepared request with transaction count result
     */
    public Call<BCResult<BigInteger>> getTransactionCount(@NonNull String address) {
        return getInstantService().getTransactionCount(checkNotNull(address, "Address required!"));
    }

    /**
     * SendCoin transaction
     *
     * @param transactionSign Raw signed TX
     * @return Prepared request
     * @see TransactionSendResult
     */
    public Call<BCResult<BytesData>> sendTransaction(@NonNull TransactionSign transactionSign) {
        return getInstantService().sendTransaction(
                asMap("transaction", transactionSign.getTxSign())
        );
    }

    @NonNull
    @Override
    protected Class<BlockChainAccountEndpoint> getServiceClass() {
        return BlockChainAccountEndpoint.class;
    }

    public static class CoinBalanceDeserializer implements JsonDeserializer<Balance> {
        @Override
        public Balance deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (json.isJsonNull()) {
                return null;
            }

            Balance balance = new Balance();

            JsonObject o = json.getAsJsonObject();

            final Map<String, Balance.CoinBalance> out = new HashMap<>();
            for (String key : o.keySet()) {
                final String uKey = key.toUpperCase();
                final Balance.CoinBalance b = new Balance.CoinBalance();
                b.balance = new BigInteger(o.get(uKey).getAsString());
                b.coin = uKey;
                out.put(uKey, b);
            }

            balance.coins = out;

            return balance;
        }
    }


}
