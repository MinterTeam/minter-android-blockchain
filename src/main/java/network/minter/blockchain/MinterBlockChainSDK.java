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

package network.minter.blockchain;

import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.math.BigInteger;

import javax.annotation.Nonnull;

import io.reactivex.schedulers.Schedulers;
import network.minter.blockchain.repo.NodeAddressRepository;
import network.minter.blockchain.repo.NodeBlockRepository;
import network.minter.blockchain.repo.NodeCoinRepository;
import network.minter.blockchain.repo.NodeEventRepository;
import network.minter.blockchain.repo.NodeStatusRepository;
import network.minter.blockchain.repo.NodeTransactionRepository;
import network.minter.blockchain.repo.NodeValidatorRepository;
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
import network.minter.core.internal.common.Acceptor;
import network.minter.core.internal.log.Mint;
import network.minter.core.internal.log.TimberLogger;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class MinterBlockChainSDK {
    private final static String BASE_NODE_URL = BuildConfig.BASE_NODE_URL + BuildConfig.BASE_NODE_VERSION + "/";
    private static MinterBlockChainSDK INSTANCE;
    private final ApiService.Builder mApiService;
    private NodeAddressRepository mAccountRepository;
    private NodeCoinRepository mCoinRepository;
    private NodeTransactionRepository mTransactionRepository;
    private NodeBlockRepository mBlockRepository;
    private NodeValidatorRepository mCandidateRepository;
    private NodeStatusRepository mStatusRepository;
    private NodeEventRepository mEventRepository;

    private MinterBlockChainSDK() {
        this(BASE_NODE_URL);
    }

    private MinterBlockChainSDK(@Nonnull String baseNodeApiUrl) {
        mApiService = new ApiService.Builder(baseNodeApiUrl, getGsonBuilder());
        mApiService.setRetrofitClientConfig(new Acceptor<Retrofit.Builder>() {
            @Override
            public void accept(Retrofit.Builder builder) {
                builder.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()));
            }
        });
        mApiService.addHeader("Content-Type", "application/json");
        mApiService.addHeader("X-Minter-Client-Name", "MinterAndroid");
        mApiService.addHeader("X-Minter-Client-Version", BuildConfig.VERSION_NAME);
        mApiService.addHttpInterceptor(new ResponseErrorToResultInterceptor());
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
    public static MinterBlockChainSDK createInstance(String baseNodeApiUrl, boolean debug, Mint.Leaf logger) {
        if (debug) {
            Mint.brew(logger);
        }
        MinterBlockChainSDK api = new MinterBlockChainSDK(baseNodeApiUrl);
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
        INSTANCE = new MinterBlockChainSDK(baseNodeApiUrl);
        INSTANCE.mApiService.setDebug(debug);
        if (debug) {
            INSTANCE.mApiService.setDebugRequestLevel(HttpLoggingInterceptor.Level.BODY);
        }
    }

    public static void initialize(boolean debug) {
        initialize(BASE_NODE_URL, debug, new TimberLogger());
    }

    public static void initialize(boolean debug, Mint.Leaf logger) {
        initialize(BASE_NODE_URL, debug, logger);
    }

    public static void initialize(String baseNodeApiUrl) {
        initialize(baseNodeApiUrl, false, new TimberLogger());
    }

    public static MinterBlockChainSDK getInstance() {
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

    public NodeEventRepository event() {
        if (mEventRepository == null) {
            mEventRepository = new NodeEventRepository(mApiService);
        }

        return mEventRepository;
    }

    public NodeStatusRepository status() {
        if (mStatusRepository == null) {
            mStatusRepository = new NodeStatusRepository(mApiService);
        }

        return mStatusRepository;
    }

    public NodeValidatorRepository validator() {
        if (mCandidateRepository == null) {
            mCandidateRepository = new NodeValidatorRepository(mApiService);
        }

        return mCandidateRepository;
    }

    public NodeBlockRepository block() {
        if (mBlockRepository == null) {
            mBlockRepository = new NodeBlockRepository(mApiService);
        }

        return mBlockRepository;
    }

    public NodeAddressRepository account() {
        if (mAccountRepository == null) {
            mAccountRepository = new NodeAddressRepository(mApiService);
        }

        return mAccountRepository;
    }

    public NodeTransactionRepository transactions() {
        if (mTransactionRepository == null) {
            mTransactionRepository = new NodeTransactionRepository(mApiService);
        }

        return mTransactionRepository;
    }

    public NodeCoinRepository coin() {
        if (mCoinRepository == null) {
            mCoinRepository = new NodeCoinRepository(mApiService);
        }

        return mCoinRepository;
    }

    /**
     * This class convert any HTTP error that contains valid json response to successful NodeResult response.
     * It was made to help handle error messages from service and avoid manual exception extraction error body and un-json it
     */
    public static class ResponseErrorToResultInterceptor implements Interceptor {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Response response = chain.proceed(request);
            if (response.body() != null && response.body().contentType() != null && response.body().contentType().toString().toLowerCase().startsWith("application/json")) {
                Response.Builder b = response.newBuilder();
                b.code(200);

                return b.build();
            }
            return response;
        }
    }
}
