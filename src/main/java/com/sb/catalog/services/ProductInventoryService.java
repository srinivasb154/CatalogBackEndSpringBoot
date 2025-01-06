package com.sb.catalog.services;

import com.sb.catalog.models.ProductInventory;
import com.sb.catalog.repositories.ProductInventoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductInventoryService {

    private final ProductInventoryRepository productInventoryRepository;

    @Autowired
    public ProductInventoryService(ProductInventoryRepository productInventoryRepository) {
        this.productInventoryRepository = productInventoryRepository;
    }

    public List<ProductInventory> getAllProductInventories() {
        return productInventoryRepository.findAll();
    }

    public Optional<ProductInventory> getProductInventoryById(UUID productId) {
        return productInventoryRepository.findById(productId);
    }

    public List<ProductInventory> getInventoriesByProductId(UUID productId) {
        return productInventoryRepository.findByProductId(productId);
    }

    public ProductInventory saveProductInventory(ProductInventory inventory) {
        try {
            return productInventoryRepository.save(inventory);
        } catch (ObjectOptimisticLockingFailureException e) {
            throw new RuntimeException("Concurrent update detected for inventory ID: " + inventory.getProductId(), e);
        }
    }

    @Transactional
    public ProductInventory updateProductInventory(UUID productId, ProductInventory updatedProductInventory) {
        return productInventoryRepository.findById(productId).map(existingInventory -> {
            existingInventory.setBin(updatedProductInventory.getBin());
            existingInventory.setLocation(updatedProductInventory.getLocation());
            existingInventory.setSource(updatedProductInventory.getSource());
            existingInventory.setOnHand(updatedProductInventory.getOnHand());
            existingInventory.setOnHold(updatedProductInventory.getOnHold());
            return productInventoryRepository.save(existingInventory);
        }).orElseThrow(() -> new RuntimeException("ProductInventory not found with id: " + productId));
    }

    public void deleteProductInventory(UUID productId) {
        if (productInventoryRepository.existsById(productId)) {
            productInventoryRepository.deleteById(productId);
        } else {
            throw new RuntimeException("ProductInventory not found with id: " + productId);
        }
    }
}

