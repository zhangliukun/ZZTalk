package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class UserSocketMap<K,V> extends HashMap<K, V>{
	//根据value来删除指定的项
	public void removeByValue(Object value){
		for(Object key:keySet()){
			if (get(key) == value) {
				remove(key);
				break;
			}
		}
	}
	
	//获取所有的value组成的set集合
	public Set<V> valueSet()
	{
		Set<V> result = new HashSet<V>();
		//遍历所有的key组成的集合
		for(K key:keySet())
		{
			//将每个key对于的value添加到result中
			result.add(get(key));
		}
		return result;
	}
	
	//根据value查找key
	public K getKeyByValue(V val){
		//便利所有key组成的集合
		for(K key:keySet()){
			//如果指定的key对应的value与被搜索的value相同则返回对于的key
			if (get(key).equals(val)&&get(key)==val) {
				return key;
			}
		}
		return null;
	} 
	
	//重写HashMap的put方法，不允许value重复
	public V put(K key,V value){
		//便利所有的value组成的集合
		for(V val:valueSet())
		{
			//如果指定的value与试图放入的value相同，则抛出一个RuntimeException异常
			if (val.equals(value)&&val.hashCode() == value.hashCode()) {
				throw new RuntimeException("Map中不允许有重复的value");
			}
		}
		return super.put(key, value);
	}
}
