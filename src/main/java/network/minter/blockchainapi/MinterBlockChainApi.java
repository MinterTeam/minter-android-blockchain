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

package network.minter.blockchainapi;

import android.support.annotation.NonNull;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import network.minter.blockchainapi.repo.BlockChainAccountRepository;
import network.minter.blockchainapi.repo.BlockChainCoinRepository;
import network.minter.blockchainapi.repo.BlockChainTransactionRepository;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.internal.api.ApiService;
import network.minter.mintercore.internal.api.converters.BigIntegerDeserializer;
import network.minter.mintercore.internal.api.converters.BytesDataDeserializer;
import network.minter.mintercore.internal.api.converters.MinterAddressDeserializer;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterBlockChainApi {
    private final static String BASE_NODE_URL = BuildConfig.BASE_NORE_URL;
    private static MinterBlockChainApi INSTANCE;
    private ApiService.Builder mApiService;
    private BlockChainAccountRepository mAccountRepository;
    private BlockChainCoinRepository mCoinRepository;
    private BlockChainTransactionRepository mTransactionRepository;

    public MinterBlockChainApi() {
        this(BASE_NODE_URL);
    }

    public MinterBlockChainApi(@NonNull String baseNodeApiUrl) {
        mApiService = new ApiService.Builder(baseNodeApiUrl, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
    }

    public static void initialize(boolean debug) {
        if (INSTANCE != null) {
            return;
        }

        INSTANCE = new MinterBlockChainApi();
        INSTANCE.mApiService.setDebug(debug);
        if (debug) {
            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    public static MinterBlockChainApi getInstance() {
        return INSTANCE;
    }

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressDeserializer());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerDeserializer());
        out.registerTypeAdapter(BytesData.class, new BytesDataDeserializer());

        return out;
    }

    public BlockChainAccountRepository account() {
        if (mAccountRepository == null) {
            mAccountRepository = new BlockChainAccountRepository(mApiService);
        }

        return mAccountRepository;
    }

    public BlockChainTransactionRepository transactions() {
        if (mTransactionRepository == null) {
            mTransactionRepository = new BlockChainTransactionRepository(mApiService);
        }

        return mTransactionRepository;
    }

    public BlockChainCoinRepository coin() {
        if (mCoinRepository == null) {
            mCoinRepository = new BlockChainCoinRepository(mApiService);
        }

        return mCoinRepository;
    }
}
