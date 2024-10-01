package org.radargps.localapplication.pending.data;

import org.radargps.localapplication.pending.data.domain.ProductPendingPallet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProductPendingPalletInternalService {
    private final ProductPendingPalletRepository productPendingPalletRepository;

    public ProductPendingPalletInternalService(ProductPendingPalletRepository productPendingPalletRepository) {
        this.productPendingPalletRepository = productPendingPalletRepository;
    }

    @Transactional
    public ProductPendingPallet save(ProductPendingPallet ppp) {
        return productPendingPalletRepository.save(ppp);
    }

    @Transactional
    public List<ProductPendingPallet> findByPalletScanner(String uniqueId) {
        return productPendingPalletRepository.findByPalletScanner(uniqueId);
    }

    @Transactional
    public void deleteAllById(List<UUID> ids) {
        productPendingPalletRepository.deleteAllById(ids);
    }
}
