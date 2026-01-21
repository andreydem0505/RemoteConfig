package andreydem0505.remoteconfig.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.util.Pool;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class KeyKryoSerializer implements RedisSerializer<String> {

    private static final Pool<Kryo> kryoPool = new Pool<>(true, false, 100) {
        @Override
        protected Kryo create() {
            Kryo kryo = new Kryo();
            kryo.register(String.class);
            return kryo;
        }
    };

    @Override
    public byte[] serialize(String value) throws SerializationException {
        try (Output output = new Output(256, -1)) {
            kryoPool.obtain().writeObject(output, value);
            return output.toBytes();
        } catch (Exception e) {
            throw new SerializationException("Failed to serialize key", e);
        }
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        try (Input input = new Input(bytes)) {
            return kryoPool.obtain().readObject(input, String.class);
        } catch (Exception e) {
            throw new SerializationException("Failed to deserialize key", e);
        }
    }
}
