package com.wt;

import com.sun.org.apache.bcel.internal.generic.LUSHR;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用LinkedHashMap实现LRU算法：
 * 1、新建一个类继承于LinkedHashMap用来缓存数据
 *      1.1、两个属性：最大容量和负载因子
 *      1.2、通过加锁来实现线程安全 new ReentrantLock()
 *      1.3、构造函数： 传入最大容量、使用super调用父类构造函数，传入最大容量、负载因子、true(按访问顺序进行排序)、初始化最大容量
 *      1.4、重写方法：removeEldesEntry()、containsKey()、get()、put()
 *      1.5、三个方法： size（）、clear（）---加锁+调用父类方法
 *                    getAll（）---return new ArrayList<Map.Entry<K,V>>(super.entrySet())
 * @param <K>
 * @param <V>
 */
public class LRULinkedHashMap<K,V> extends LinkedHashMap<K, V> {
    private final int maxCapacity;

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private final Lock lock = new ReentrantLock();

    public LRULinkedHashMap(int maxCapacity) {
        super(maxCapacity,DEFAULT_LOAD_FACTOR,true);//accessOrder设置成true，按照访问顺序进行排序。
        this.maxCapacity = maxCapacity;
    }

    /**
     * 淘汰策略：当size（）> maxCapacity时 淘汰最先访问的数据
     * @param eldest
     * @return
     */
    @Override
    protected boolean removeEldestEntry(java.util.Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
    }

    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return super.containsKey(key);

        }finally {
            lock.unlock();
        }
    }

    @Override
    public V get(Object key) {
        try {
            lock.lock();
            return super.get(key);

        }finally {
            lock.unlock();
        }
    }

    @Override
    public V put(K key, V value) {
        try {
            lock.lock();
            return super.put(key, value);
        }finally {
            lock.unlock();
        }
    }

    public int size(){
        try {
            lock.lock();
            return super.size();
        }finally {
            lock.unlock();
        }
    }

    public void clear(){
        try {
            lock.lock();
            super.clear();
        }finally {
            lock.unlock();
        }
    }

    public Collection<Map.Entry<K,V>> getAll(){
        try {
            lock.lock();
            return new ArrayList<Map.Entry<K, V>>(super.entrySet());
        }finally {
            lock.unlock();
        }
    }
//    测试
//    public static void main(String[] args) {
//        LRULinkedHashMap map = new LRULinkedHashMap(10);
//        for (int i = 0; i < 12; i++){
//            map.put(i,i);
//        }
//        ArrayList arrayList = (ArrayList) map.getAll();
//        System.out.println(arrayList);
//        System.out.println(map.size());
//    }

}