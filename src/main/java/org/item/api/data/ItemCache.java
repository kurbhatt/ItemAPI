package org.item.api.data;

import org.item.api.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Keyur on 12-08-2016.
 */

public class ItemCache {

    private static final Logger LOG = LoggerFactory.getLogger(ItemCache.class);

    private static final ConcurrentMap<Integer, Item> ITEM_CACHE = new ConcurrentHashMap<>();
    private static final Set<Integer> PRIORITIES = new HashSet<>();

    public static Item getFromCache(final Integer itemId) {
        final Item item = ItemCache.ITEM_CACHE.get(itemId);
        if (item != null) {
            ItemCache.LOG.info("Item identified by [{}] is in cache.", itemId);
        }
        return item;
    }

    public static Item getItemFromCacheUsingPriority(Integer priority){

        for (Item item : ItemCache.ITEM_CACHE.values()) {
            if (Objects.equals(item.getPriority(), priority)) {
                return item;
            }
        }
        return null;
    }

    public static List<Item> getAllFromCache(){
        return new ArrayList<>(ItemCache.ITEM_CACHE.values());
    }

    private static Integer getMaxId(){
        if(Util.isNullOrEmpty(ItemCache.ITEM_CACHE)){
            return 0;
        }
        return Collections.max(ItemCache.ITEM_CACHE.keySet());
    }

    public static Integer addToCache(final Item item) {
        Integer maxPlus = ItemCache.getMaxId() + 1;

        if(Util.isNullOrEmpty(item.getPriority()) || item.getPriority() == 0){
            ItemCache.LOG.info("Item named [{}] have empty priority.", item.getName());
            if(ItemCache.PRIORITIES.isEmpty()){
                item.setPriority(1);
                ItemCache.LOG.info("Item priority set to 1");
            }else {
                item.setPriority(Collections.max(ItemCache.PRIORITIES) + 1);
            }
        }

        item.setId(maxPlus);
        ItemCache.ITEM_CACHE.put(maxPlus, item);
        ItemCache.PRIORITIES.add(item.getPriority());
        ItemCache.LOG.info("Item Added with Id [{}]", item.getId());
        return maxPlus;
    }

    public static boolean updateToCache(Item item){
        if(ItemCache.ITEM_CACHE.keySet().contains(item.getId())){
            ItemCache.ITEM_CACHE.put(item.getId(), item);
            return true;
        }
        return false;
    }

    public static boolean removeFromCache(final Integer itemId) {
        if(ItemCache.ITEM_CACHE.keySet().contains(itemId)){
            ItemCache.LOG.info("Item Found with Id [{}] that will be removed.", itemId);
            ItemCache.ITEM_CACHE.remove(itemId);
            return true;
        }
        return false;
    }

    static int getCacheSize() {
        return ItemCache.ITEM_CACHE.size();
    }

    static void clearCache() {
        ItemCache.ITEM_CACHE.clear();
    }
}
