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

package network.minter.blockchain;

import com.google.gson.GsonBuilder;

import java.math.BigInteger;

import javax.annotation.Nonnull;

import network.minter.blockchain.repo.BlockChainAccountRepository;
import network.minter.blockchain.repo.BlockChainBlockRepository;
import network.minter.blockchain.repo.BlockChainCandidateRepository;
import network.minter.blockchain.repo.BlockChainCoinRepository;
import network.minter.blockchain.repo.BlockChainEventRepository;
import network.minter.blockchain.repo.BlockChainStatusRepository;
import network.minter.blockchain.repo.BlockChainTransactionRepository;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterHash;
import network.minter.core.crypto.MinterPublicKey;
import network.minter.core.internal.api.ApiService;
import network.minter.core.internal.api.converters.BigIntegerJsonConverter;
import network.minter.core.internal.api.converters.BytesDataJsonConverter;
import network.minter.core.internal.api.converters.MinterAddressJsonConverter;
import network.minter.core.internal.api.converters.MinterHashJsonConverter;
import network.minter.core.internal.api.converters.MinterPublicKeyJsonConverter;
import network.minter.core.internal.log.Mint;
import network.minter.core.internal.log.TimberLogger;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterBlockChainApi {
    private final static String BASE_NODE_URL = BuildConfig.BASE_NODE_URL;
    private static MinterBlockChainApi INSTANCE;
    private final ApiService.Builder mApiService;
    private BlockChainAccountRepository mAccountRepository;
    private BlockChainCoinRepository mCoinRepository;
    private BlockChainTransactionRepository mTransactionRepository;
    private BlockChainBlockRepository mBlockRepository;
    private BlockChainCandidateRepository mBlockChainCandidateRepository;
    private BlockChainStatusRepository mStatusRepository;
    private BlockChainEventRepository mEventRepository;

    private MinterBlockChainApi() {
        this(BASE_NODE_URL);
    }

    private MinterBlockChainApi(@Nonnull String baseNodeApiUrl) {
        mApiService = new ApiService.Builder(baseNodeApiUrl, getGsonBuilder());
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
    }

    public static void initialize() {
        initialize(BASE_NODE_URL, false, new TimberLogger());
    }

    /**
     * Use this if no need to use singleton, for example, for accessing multiple instances simultaneously
     * @param baseNodeApiUrl
     * @param debug
     * @param logger
     * @return
     */
    public static MinterBlockChainApi createInstance(String baseNodeApiUrl, boolean debug, Mint.Leaf logger) {
        if (debug) {
            Mint.brew(logger);
        }
        MinterBlockChainApi api = new MinterBlockChainApi(baseNodeApiUrl);
        api.mApiService.setDebug(debug);
        if (debug) {
            api.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }

        return api;
    }

    public static void initialize(String baseNodeApiUrl, boolean debug, Mint.Leaf logger) {
        if (INSTANCE != null) {
            return;
        }

        if (debug) {
            Mint.brew(logger);
        }
        INSTANCE = new MinterBlockChainApi(baseNodeApiUrl);
        INSTANCE.mApiService.setDebug(debug);
        if (debug) {
            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    public static void initialize(boolean debug) {
        initialize(BASE_NODE_URL, debug, new TimberLogger());
    }

    public static void initialize(String baseNodeApiUrl) {
        initialize(baseNodeApiUrl, false, new TimberLogger());
    }

    public static MinterBlockChainApi getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("You forget to call MinterBlockchainApi.initialize");
        }
        return INSTANCE;
    }

    public ApiService.Builder getApiService() {
        return mApiService;
    }

    public GsonBuilder getGsonBuilder() {
        GsonBuilder out = new GsonBuilder();
        out.registerTypeAdapter(MinterAddress.class, new MinterAddressJsonConverter());
        out.registerTypeAdapter(MinterHash.class, new MinterHashJsonConverter());
        out.registerTypeAdapter(BigInteger.class, new BigIntegerJsonConverter());
        out.registerTypeAdapter(BytesData.class, new BytesDataJsonConverter());
        out.registerTypeAdapter(MinterPublicKey.class, new MinterPublicKeyJsonConverter());

        return out;
    }

    public BlockChainEventRepository event() {
        if (mEventRepository == null) {
            mEventRepository = new BlockChainEventRepository(mApiService);
        }

        return mEventRepository;
    }

    public BlockChainStatusRepository status() {
        if (mStatusRepository == null) {
            mStatusRepository = new BlockChainStatusRepository(mApiService);
        }

        return mStatusRepository;
    }

    public BlockChainCandidateRepository candidate() {
        if (mBlockChainCandidateRepository == null) {
            mBlockChainCandidateRepository = new BlockChainCandidateRepository(mApiService);
        }

        return mBlockChainCandidateRepository;
    }

    public BlockChainBlockRepository block() {
        if (mBlockRepository == null) {
            mBlockRepository = new BlockChainBlockRepository(mApiService);
        }

        return mBlockRepository;
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
