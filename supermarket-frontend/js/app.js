import { initApp, handleCreateSale, handleAddProduct, loadBranchesIntoModal, 
  loadProductsIntoModal, handleCreateBranchFromModal, handleCreateProductFromModal } from './handlers.js';
import { elements, renderProductTable } from './dom.js';
import * as api from './api.js';

document.addEventListener('DOMContentLoaded', async () => {
  await initApp();

  // Links branch and product modal
  const branchModal = new bootstrap.Modal(
    document.getElementById('branchModal')
  );

  const productModal = new bootstrap.Modal(
    document.getElementById('productModal')
  );

  /* BRANCH CARD */

  // Create sale button
  elements.createSaleBtn.addEventListener('click', handleCreateSale);

  // Edit branch button
  elements.editBranchBtn.addEventListener('click', async () => {
    await loadBranchesIntoModal();
    branchModal.show();
  });

  /* BRANCH MODAL */
  
  // Create branch button (modal)
  elements.createBranchModalBtn.addEventListener(
    'click',
    handleCreateBranchFromModal
  );

  /* PRODUCT CARD */

  // Add product button
  elements.addProductBtn.addEventListener('click', handleAddProduct);

  // Edit product button
  elements.editProductBtn.addEventListener('click', async () => {
    productModal.show();

    const products = await api.fetchProducts();
    renderProductTable(products, {
      targetTbody: elements.productTableBody
    })
  });

  /* PRODUCT MODAL */

  // Create new product button
  elements.createProductModalBtn.addEventListener('click', handleCreateProductFromModal);

});
