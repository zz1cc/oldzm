package com.zm.platform.common.util.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * https://www.cnblogs.com/zeng1994/p/03303c805731afc9aa9c60dbbd32a323.html
 * @author Administrator
 *
 */

@Component
public class RedisUtil {
	
	private static RedisTemplate<String, Object> redisTemplate;

	@Autowired
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
		RedisUtil.redisTemplate = redisTemplate;
	}
	
    /** 
     * 	指定缓存失效时间 
     * @param key 键 
     * @param time 时间(秒) 
     * @return 
     */  
    public static boolean expire(String key,long time){  
        try {  
            if(time>0){  
                redisTemplate.expire(key, time, TimeUnit.SECONDS);  
            }  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /** 
     * 	判断key是否存在 
     * @param key 键 
     * @return true 存在 false不存在 
     */  
    public static boolean hasKey(String key){  
        try {  
            return redisTemplate.hasKey(key);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /** 
     * 	删除缓存 
     * @param key 可以传一个值 或多个 
     */  
    @SuppressWarnings("unchecked")  
    public static void del(String ... key){  
        if(key!=null&&key.length>0){  
            if(key.length==1){  
                redisTemplate.delete(key[0]);  
            }else{  
                redisTemplate.delete(CollectionUtils.arrayToList(key));  
            }  
        }  
    }
    
    /** 
     * 	普通缓存获取 
     * @param key 键 
     * @return 值 
     */  
    public static Object get(String key){  
        return key==null?null:redisTemplate.opsForValue().get(key);  
    }
    
    /** 
     * 	普通缓存放入 
     * @param key 键 
     * @param value 值 
     * @return true成功 false失败 
     */  
    public static boolean set(String key,Object value) {  
         try {  
            redisTemplate.opsForValue().set(key, value);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
          
    }
    
    /** 
     * 	普通缓存放入并设置时间 
     * @param key 键 
     * @param value 值 
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期 
     * @return true成功 false 失败 
     */  
    public static boolean set(String key,Object value,long time){  
        try {  
            if(time>0){  
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);  
            }else{  
                set(key, value);  
            }  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /** 
     * HashGet 
     * @param key 键 不能为null 
     * @param item 项 不能为null 
     * @return 值 
     */  
    public static Object hget(String key,String item){  
        return redisTemplate.opsForHash().get(key, item);  
    }
    
    /** 
     * 	获取hashKey对应的所有键值 
     * @param key 键 
     * @return 对应的多个键值 
     */  
    public static Map<Object,Object> hmget(String key){  
        return redisTemplate.opsForHash().entries(key);  
    }
    
    /** 
     * HashSet 
     * @param key 键 
     * @param map 对应多个键值 
     * @return true 成功 false 失败 
     */  
    public static boolean hmset(String key, Map<String,Object> map){    
        try {  
            redisTemplate.opsForHash().putAll(key, map);  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /** 
     * HashSet 并设置时间 
     * @param key 键 
     * @param map 对应多个键值 
     * @param time 时间(秒) 
     * @return true成功 false失败 
     */  
    public static boolean hmset(String key, Map<String,Object> map, long time){    
        try {  
            redisTemplate.opsForHash().putAll(key, map);  
            if(time>0){  
                expire(key, time);  
            }  
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
    
    /**
     * 	获取redis list 0 到 -1代表取出所有
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> getList(String key, long start, long end){    
        try {  
        	return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {  
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 	list存入redis
     * @param key
     * @param value
     * @return
     */
    public static boolean setList(String key, Object value) {
        try {  
        	redisTemplate.opsForList().rightPush(key, value);
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();
            return false;  
        }  
    }

    /**
     * 	list存入redis
     * @param key
     * @param value
     * @param time 失效时间
     * @return
     */
    public static boolean setList(String key, Object value, long time) {
        try {  
        	redisTemplate.opsForList().rightPush(key, value);
        	if (time > 0) expire(key, time);
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }

    public static boolean setList(String key, List<Object> value) {
        try {  
        	redisTemplate.opsForList().rightPushAll(key, value);
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }

    public static boolean setList(String key, List<Object> value, long time) {
        try {  
        	redisTemplate.opsForList().rightPushAll(key, value);
        	if (time > 0) expire(key, time);
            return true;  
        } catch (Exception e) {  
            e.printStackTrace();  
            return false;  
        }  
    }
}
