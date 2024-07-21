package com.jedis.dataType;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
/**
 * 布谷鸟过滤器
 * 因为布隆过滤器  不能删除 且效率低（多个hash跳跃     不能有效缓存）
 *
 *
 * 使用两个hash确定位置   计算一个finger进行保存      两个位置都满了就会排挤别人  通过设定上限扩容
 *
 */
public class CuckooDemo {




    private static class CuckooFilter {
        private static final int MAX_KICKS = 500;
        private BitSet[] buckets;
        private int numBuckets;
        private int bucketSize;
        private int numItems;

        public CuckooFilter(int numBuckets, int bucketSize) {
            this.numBuckets = numBuckets;
            this.bucketSize = bucketSize;
            this.buckets = new BitSet[numBuckets];
            for (int i = 0; i < numBuckets; i++) {
                buckets[i] = new BitSet(bucketSize);
            }
            this.numItems = 0;
        }

        public boolean contains(String item) {
            int fingerprint = getFingerprint(item);
            int bucket1 = getBucket(item);
            int bucket2 = getAltBucket(bucket1, fingerprint);

            return buckets[bucket1].get(fingerprint) || buckets[bucket2].get(fingerprint);
        }

        public void insert(String item) {
            if (contains(item)) {
                return;
            }

            int fingerprint = getFingerprint(item);
            int bucket1 = getBucket(item);
            int bucket2 = getAltBucket(bucket1, fingerprint);

            if (buckets[bucket1].cardinality() < bucketSize) {
                buckets[bucket1].set(fingerprint);
                numItems++;
            } else if (buckets[bucket2].cardinality() < bucketSize) {
                buckets[bucket2].set(fingerprint);
                numItems++;
            } else {
                Random random = new Random();
                int bucket = random.nextBoolean() ? bucket1 : bucket2;
                int i = 0;
                while (i < MAX_KICKS) {
                    int evictedFingerprint = random.nextInt(bucketSize);
                    if (!buckets[bucket].get(evictedFingerprint)) {
                        buckets[bucket].set(evictedFingerprint);
                        String evictedItem = getItem(bucket, evictedFingerprint);
                        insert(evictedItem);
                        return;
                    }
                    i++;
                }
                rehash();
                insert(item);
            }
        }

        private int getFingerprint(String item) {
            // 使用合适的哈希函数生成指纹
            // 这里可以使用各种哈希算法，例如MurmurHash、SHA等
            // 这里简化处理，直接使用String的hashCode方法
            return item.hashCode();
        }

        private int getBucket(String item) {
            // 使用合适的哈希函数生成桶索引
            // 这里可以使用各种哈希算法，例如MurmurHash、SHA等
            // 这里简化处理，直接使用String的hashCode方法
            return Math.abs(item.hashCode()) % numBuckets;
        }

        private int getAltBucket(int bucket, int fingerprint) {
            // 使用异或操作产生备选桶索引
            return bucket ^ (fingerprint % numBuckets);
        }

        private String getItem(int bucket, int fingerprint) {
            // 根据桶索引和指纹反推出之前插入的元素
            // 这里简化处理，直接返回桶索引和指纹的拼接字符串
            return bucket + ":" + fingerprint;
        }

        private void rehash() {
            int newNumBuckets = numBuckets * 2;
            BitSet[] newBuckets = new BitSet[newNumBuckets];
            for (int i = 0; i < newNumBuckets; i++) {
                newBuckets[i] = new BitSet(bucketSize);
            }
            for (BitSet bucket : buckets) {
                for (int i = 0; i < bucketSize; i++) {
                    if (bucket.get(i)) {
//                        String item = getItem(buckets, i);
//                        int newBucket = getBucket(item);
//                        newBuckets[newBucket].set(getFingerprint(item));
                    }
                }
            }
            buckets = newBuckets;
            numBuckets = newNumBuckets;
        }
    }
}


