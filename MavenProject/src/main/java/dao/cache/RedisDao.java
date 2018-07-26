package dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import entity.seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis缓存
 */
public class RedisDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    public RedisDao(String ip,int port) {
        this.jedisPool = new JedisPool(ip, port);
    }

    private RuntimeSchema<seckill> schema = RuntimeSchema.createFrom(seckill.class);

    public seckill getSeckill(long seckillId) {
        // redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();

            try {
                String key = "seckill:" + seckillId;
                // redis并没有实现内部序列化操作
                // 采用自定义序列化
                byte[] bytes = jedis.get(key.getBytes());
                if (bytes != null) {
                    // 空对象
                    seckill s = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes, s, schema);
                    // seckill被反序列化
                    return s;
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public String putSeckill(seckill s) {
        // set Object(seckill) -> 序列化 -> byte[]
        try {
            Jedis jedis = jedisPool.getResource();

            try {
                String key = "seckill:" + s.getId();
                byte[] bytes = ProtobufIOUtil.toByteArray(s, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                // 超时缓存
                int timeout = 60 * 60;  // 1小时
                String result = jedis.setex(key.getBytes(), timeout, bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
