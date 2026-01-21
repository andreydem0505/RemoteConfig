package andreydem0505.remoteconfig.cache;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import andreydem0505.remoteconfig.security.UserRole;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class ValueKryoSerializer implements RedisSerializer<Object> {

    private static final Pool<Kryo> kryoPool = new Pool<>(true, false, 100) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.setRegistrationRequired(false);
            kryo.register(DynPropertyCache.class, new DynPropertyKryoSerializer());
            kryo.register(UserCache.class);
            kryo.register(String.class);
            kryo.register(UserRole.class);
            return kryo;
        }
    };

    @Override
    public byte[] serialize(Object value) throws SerializationException {
        try (Output output = new Output(512, -1)) {
            kryoPool.obtain().writeClassAndObject(output, value);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize value", e);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        try (Input input = new Input(bytes)) {
            return kryoPool.obtain().readClassAndObject(input);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize value", e);
        }
    }

    private static class DynPropertyKryoSerializer extends Serializer<DynPropertyCache> {
        @Override
        public void write(Kryo kryo, Output output, DynPropertyCache value) {
            kryo.writeObject(output, value.getType());
            kryo.writeClassAndObject(output, value.getData());
        }

        @Override
        public DynPropertyCache read(Kryo kryo, Input input, Class<? extends DynPropertyCache> type) {
            PropertyType propertyType = kryo.readObject(input, PropertyType.class);
            Object data = kryo.readClassAndObject(input);
            return new DynPropertyCache(propertyType, data);
        }
    }
}
