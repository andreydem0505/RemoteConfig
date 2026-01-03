package andreydem0505.remoteconfig.cache;

import andreydem0505.remoteconfig.data.documents.PropertyType;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class DynPropertyKryoSerializer implements RedisSerializer<DynPropertyCache> {

    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setRegistrationRequired(false);
        kryo.register(DynPropertyCache.class, new KryoSerializer());
        return kryo;
    });

    @Override
    public byte[] serialize(DynPropertyCache value) throws SerializationException {
        Kryo kryo = KRYO.get();
        try (Output output = new Output(512, -1)) {
            kryo.writeObject(output, value);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize DynProperty", e);
        }
    }

    @Override
    public DynPropertyCache deserialize(byte[] bytes) throws SerializationException {
        Kryo kryo = KRYO.get();
        try (Input input = new Input(bytes)) {
            return kryo.readObject(input, DynPropertyCache.class);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize DynProperty", e);
        }
    }

    private static class KryoSerializer extends Serializer<DynPropertyCache> {
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
