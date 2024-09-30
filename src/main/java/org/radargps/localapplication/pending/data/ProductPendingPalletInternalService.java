package org.radargps.localapplication.pending.data;

import org.radargps.localapplication.pending.data.domain.ProductPendingPallet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductPendingPalletInternalService {
    private final ProductPendingPalletRepository productPendingPalletRepository;

    public ProductPendingPalletInternalService(ProductPendingPalletRepository productPendingPalletRepository) {
        this.productPendingPalletRepository = productPendingPalletRepository;
    }

    public ProductPendingPallet save(ProductPendingPallet ppp) {
        return productPendingPalletRepository.save(ppp);
    }

    public List<ProductPendingPallet> findByPalletScanner(String uniqueId) {
        return productPendingPalletRepository.findByPalletScanner(uniqueId);
    }

    public void deleteAllById(List<UUID> ids) {
        productPendingPalletRepository.deleteAllById(ids);
    }
}
