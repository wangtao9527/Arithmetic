package com.wt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LRULinkedHashMapByMySelf<K, V> extends LinkedHashMap<K, V> {
    private final int maxCapacity;

    private static final float DEFAULT_LOAD_FACTOR=0.75f;

    private Lock lock = new ReentrantLock();

    public LRULinkedHashMapByMySelf(int maxCapacity) {
        super(maxCapacity,DEFAULT_LOAD_FACTOR,true);
        this.maxCapacity = maxCapacity;
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
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxCapacity;
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

    @Override
    public boolean containsKey(Object key) {
        try {
            lock.lock();
            return super.containsKey(key);
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
            return new ArrayList<Map.Entry<K,V>>(super.entrySet());
        }finally {
            lock.unlock();
        }
    }

    //    测试
//        public static void main(String[] args) {
//            LRULinkedHashMap map = new LRULinkedHashMap(10);
//            for (int i = 0; i < 12; i++){
//                map.put(i,i);
//            }
//            ArrayList arrayList = (ArrayList) map.getAll();
//            System.out.println(arrayList);
//            System.out.println(map.size());
//        }
}
