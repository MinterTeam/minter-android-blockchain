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
public class EventList {

    @SerializedName("events")
    public List<EventItem> events;

    public enum Type {
        Reward("minter/RewardEvent", RewardEvent.class),
        Slash("minter/SlashEvent", SlashEvent.class),
        ;

        private String mName;
        private Class<? extends BaseEvent> mCls;

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
