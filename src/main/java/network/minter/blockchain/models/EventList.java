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

import java.math.BigInteger;
import java.util.List;

import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
@Parcel
public class EventList extends NodeResult {

    @SerializedName("events")
    public List<EventItem> events;

    public enum Type {
        Reward("minter/RewardEvent", RewardEvent.class),
        Slash("minter/SlashEvent", SlashEvent.class),
        ;

        private final String mName;
        private final Class<? extends BaseEvent> mCls;

        Type(String name, Class<? extends BaseEvent> cls) {
            mName = name;
            mCls = cls;
        }

        public static Type findByName(String type) {
            for (Type t : Type.values()) {
                if (t.getEventName().toLowerCase().equals(type.toLowerCase())) {
                    return t;
                }
            }

            return null;
        }

        public Class<? extends BaseEvent> getCls() {
            return mCls;
        }

        public String getEventName() {
            return mName;
        }
    }

    @Parcel
    public static class EventItem {
        public Type type;
        public BaseEvent value;

        @SuppressWarnings("unchecked")
        public <T extends BaseEvent> T getValue() {
            return (T) value;
        }
    }

    @Parcel
    public static class BaseEvent {
        @SerializedName("address")
        public MinterAddress address;
        @SerializedName("amount")
        public BigInteger amount;
        @SerializedName("validator_pub_key")
        public MinterPublicKey validatorPublicKey;
    }

    @Parcel
    public static class RewardEvent extends BaseEvent {
        @SerializedName("role")
        public String role;
    }

    @Parcel
    public static class SlashEvent extends BaseEvent {
        @SerializedName("coin")
        public String coin;
    }
}
