import { initApp } from './handlers/appHandlers.js';
import * as branchHandlers from './handlers/branchHandlers.js';
import * as productHandlers from './handlers/productHandlers.js';
import * as saleHandlers from './handlers/saleHandlers.js';
import { elements } from './dom/elements.js';
import { showBranchModal, showProductModal } from './dom/ui/modals.js';

document.addEventListener('DOMContentLoaded', async () => {
  await initApp();

  /* BRANCH CARD */
  elements.createSaleBtn.addEventListener('click', saleHandlers.handleCreateSale);
  elements.editBranchBtn.addEventListener('click', async () => {
    await branchHandlers.loadBranchesIntoModal();
    showBranchModal();
  });

  /* PRODUCT CARD */
  elements.addProductBtn.addEventListener('click', productHandlers.handleAddProduct);
  elements.editProductBtn.addEventListener('click', async () => {
    await productHandlers.loadProductsIntoModal();
    showProductModal();
  });
  elements.finishSaleBtn.addEventListener('click', saleHandlers.handleFinishSale);
  elements.cancelSaleBtn.addEventListener('click', saleHandlers.handleCancelSale);

  /* MODALS */
  elements.createBranchModalBtn.addEventListener('click', branchHandlers.handleCreateBranchFromModal);
  elements.createProductModalBtn.addEventListener('click', productHandlers.handleCreateProductFromModal);
});
