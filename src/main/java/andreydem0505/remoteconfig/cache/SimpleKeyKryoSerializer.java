package andreydem0505.remoteconfig.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class SimpleKeyKryoSerializer implements RedisSerializer<String> {

    private static final ThreadLocal<Kryo> KRYO = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.register(String.class);
        return kryo;
    });

    @Override
    public byte[] serialize(String value) throws SerializationException {
        Kryo kryo = KRYO.get();
        try (Output output = new Output(256, -1)) {
            kryo.writeObject(output, value);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize SimpleKey", e);
        }
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        Kryo kryo = KRYO.get();
        try (Input input = new Input(bytes)) {
            return kryo.readObject(input, String.class);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize SimpleKey", e);
        }
    }
}
