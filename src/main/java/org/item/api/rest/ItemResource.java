
package org.item.api.rest;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.item.api.Util;
import org.item.api.data.Item;
import org.item.api.data.ItemCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@Api(value = "Item REST Resource")
@RestController
@RequestMapping(ItemResource.ITEM_RESOURCE_URL)
class ItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(ItemResource.class);
    static final String ITEM_RESOURCE_URL = "/todolist/items";

    @ApiOperation(value = "Hello", notes = "Hello")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> hello() {
        return new ResponseEntity<>("Hello from TODO List", HttpStatus.OK);
    }

    @ApiOperation(value = "List all Items", notes = "Get all Items")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> listItems(@RequestParam(value = "priority",
            defaultValue = "", required = false) Integer priority) {
        ItemResource.LOG.info("List All Items Called");
        if (!Util.isNullOrEmpty(priority)) {
            ItemResource.LOG.info("List all items called with priority");
            return new ResponseEntity<>(ItemCache.getItemFromCacheUsingPriority(priority), HttpStatus.OK);
        }
        return new ResponseEntity<>(ItemCache.getAllFromCache(), HttpStatus.OK);
    }

    @ApiOperation(value = "Get Item", notes = "Get Item by Id")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getItem(@PathVariable Integer id) {
        ItemResource.LOG.info("Get Item Called");
        Item item = ItemCache.getFromCache(id);
        if(item == null){
            ItemResource.LOG.info("Item with Id [{}] does not exist", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @ApiOperation(value = "Add Item", notes = "Add Item")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> addItem(@RequestBody ItemResource.ItemData itemData) {
        ItemResource.LOG.info("Add Item Called");
        ResponseEntity<?> result = ItemResource.validateItemData(itemData);
        if(result != null){
            return result;
        }
        Item item = new Item(itemData.getName(), itemData.getPriority());
        return new ResponseEntity<>(ItemCache.addToCache(item), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update Item", notes = "Update Existing Item")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> updateItem(@PathVariable Integer id, @RequestBody ItemResource.ItemData itemData) {
        ItemResource.LOG.info("Update Item Called");
        ResponseEntity<?> result = ItemResource.validateItemData(itemData);
        if(result != null){
            return result;
        }
        Item item = new Item(id, itemData.getName(), itemData.getPriority());
        return new ResponseEntity<>(ItemCache.updateToCache(item) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Delete Item", notes = "Delete Item by Id")
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity removeItem(@PathVariable Integer id) {
        ItemResource.LOG.info("Remove Item Called");
        return new ResponseEntity<>(ItemCache.removeFromCache(id) ? HttpStatus.NO_CONTENT : HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Multiply Number", notes = "Multiply Number")
    @RequestMapping(value = "/multiply", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> multiplyNumber(@RequestParam(value = "numA") Integer numA, @RequestParam(value = "numB")
            Integer numB) {
        ItemResource.LOG.info("Multiply Called");
        ResponseEntity<?> result = ItemResource.validateMultiplyData(numA, numB);
        if(result != null){
            return result;
        }
        Integer multiplyResult = numA * numB;
        return new ResponseEntity<>(multiplyResult, HttpStatus.OK);
    }

    private static ResponseEntity<?> validateItemData(ItemResource.ItemData itemData){
        ItemResource.LOG.info("Validating Item Data");
        if(Util.isNullOrEmpty(itemData.getName())){
            return new ResponseEntity<>("A name is required for an item", HttpStatus.BAD_REQUEST);
        }
        if(!Util.isNullOrEmpty(itemData.getPriority()) && itemData.getPriority() <= 0){
            return new ResponseEntity<>("Priority must be positive", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private static ResponseEntity<?> validateMultiplyData(Integer numA, Integer numB){
        ItemResource.LOG.info("Validating Multiply Data");
        if(Util.isNullOrEmpty(numA) || Objects.equals(numA, 0)) {
            return new ResponseEntity<>("Number A is required and should not be zero", HttpStatus.BAD_REQUEST);
        }
        if(Util.isNullOrEmpty(numB) || Objects.equals(numB, 0)) {
            return new ResponseEntity<>("Number B is required and should not be zero", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private static class ItemData {
        private String name;
        private Integer priority;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        Integer getPriority() {
            return priority;
        }

        public void setPriority(Integer priority) {
            this.priority = priority;
        }
    }
}
