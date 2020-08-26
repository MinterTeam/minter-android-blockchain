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

import com.google.gson.Gson;

import java.io.IOException;

import javax.annotation.Nonnull;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.functions.Function;
import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.core.internal.exceptions.NetworkException;
import network.minter.core.internal.log.Mint;
import retrofit2.HttpException;

import static network.minter.core.internal.common.Preconditions.firstNonNull;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */

public class NodeResult {
    public String error;
    public Integer code;
    public String message;

    public static NodeResult copyError(NodeResult another) {
        NodeResult out = new NodeResult();
        out.code = another.code;
        out.error = another.error;
        out.message = another.message;

        return out;
    }

    public static Function<? super Throwable, ? extends ObservableSource<? extends NodeResult>> toNodeError() {
        return (Function<Throwable, ObservableSource<? extends NodeResult>>) throwable -> {
            if (throwable instanceof HttpException) {
                return Observable.just(createNodeError(((HttpException) throwable)));
            }

            return Observable.just(createNodeError(NetworkException.convertIfNetworking(throwable)));
        };
    }

    public static NodeResult createNodeError(Throwable t) {
        Throwable e = NetworkException.convertIfNetworking(t);
        if (e instanceof NetworkException) {
            return createNodeError(((NetworkException) e).getStatusCode(), ((NetworkException) e).getUserMessage());
        }

        return createNodeError(-1, e.getMessage());
    }

    public static NodeResult createNodeError(final HttpException exception) {
        final String errorBodyString;
        try {
            errorBodyString = ((HttpException) exception).response().errorBody().string();
        } catch (IOException e) {
            Mint.e(e, "Unable to resolve http exception response");
            return createNodeError(exception.code(), exception.message());
        }

        return createNodeError(errorBodyString, exception.code(), exception.message());
    }

    public static NodeResult createNodeError(final String json, int code, String message) {
        Gson gson = MinterBlockChainSDK.getInstance().getGsonBuilder().create();

        NodeResult out;
        try {
            if (json == null || json.isEmpty()) {
                out = createNodeError(code, message);
            } else {
                out = gson.fromJson(json, NodeResult.class);
            }

        } catch (Exception e) {
            Mint.e(e, "Unable to parse node error: %s", json);
            out = createNodeError(code, message);
        }

        return out;
    }

    public static NodeResult createNodeError(int code, String message) {
        NodeResult out = new NodeResult();
        out.code = code;
        out.error = message;
        out.message = message;
        return out;
    }

    public boolean isOk() {
        return error == null && (code == null || code == 0);
    }

    @Nonnull
    @Override
    public String toString() {
        return String.format("NodeResult{code=%s, error=%s, message=%s}",
                firstNonNull(code, 0),
                firstNonNull(error, "null"),
                firstNonNull(message, "{no message}")
        );
    }
}
