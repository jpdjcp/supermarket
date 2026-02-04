import { state } from './state.js';
import * as api from './api.js';
import * as dom from './dom.js';

// ---------- INIT LOAD ----------

export async function initApp() {
  state.branches = await api.fetchBranches();
  dom.renderBranches(state.branches);

  state.products = await api.fetchProducts();

  // build productMap once
  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));

  dom.renderProducts(state.products);
}

// ---------- SALE ----------

async function createSale(branchId) {
  const sale = await api.createSale(branchId);
  state.currentSaleId = sale.id;
  await loadSaleItems();
}

// ------- BRANCH MODAL --------

export async function loadBranchesIntoModal() {
  state.branches = await api.fetchBranches();

  // delete branch
  dom.renderBranchTable(state.branches, {
    onDelete: async branchId => {
      const confirmed = confirm('Delete this branch?');
      if (!confirmed) return;

      await api.deleteBranch(branchId);

      // reload state
      state.branches = await api.fetchBranches();

      // refresh UI
      dom.renderBranches(state.branches);
      await loadBranchesIntoModal();
    }
  });
}

// ------- PRODUCT MODAL --------

export async function loadProductsIntoModal() {
  state.products = await api.fetchProducts();

  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));

  // delete product
  dom.renderProductTable(state.products, {
    onDelete: async productId => {
      const confirmed = confirm('Delete this product?');
      if (!confirmed) return;

      await api.deleteProduct(productId);
      await loadProductsIntoModal(); // refresh modal
      dom.renderProducts(state.products); // refresh dropdown
    }
  });
}


// ---------- SALE ITEMS ----------

export async function loadSaleItems() {
  state.saleItems = await api.fetchSaleItems(state.currentSaleId);

  dom.renderSaleTable(state.saleItems, {
    onIncrease: async productId => {
      await api.increaseItem(state.currentSaleId, productId);
      await loadSaleItems();
    },
    onDecrease: async productId => {
      await api.decreaseItem(state.currentSaleId, productId);
      await loadSaleItems();
    },
    onDelete: async productId => {
      await api.deleteItem(state.currentSaleId, productId);
      await loadSaleItems();
    }
  });
}

// ---------- EVENT HANDLERS ----------

export async function handleCreateSale() {
  const branchId = dom.elements.branchSelect.value;
  if (!branchId) return;

  await createSale(branchId);
  dom.elements.addProductBtn.disabled = false;
  // enable finish button
  // enable cancel button
}

export async function handleAddProduct() {
  if (!state.currentSaleId) {
    alert('Please create a sale first');
    return;
  }

  const productId = dom.elements.productSelect.value;
  await api.addProductToSale(state.currentSaleId, productId);
  await loadSaleItems();
}

export async function handleCreateBranchFromModal() {
  const input = dom.elements.newBranchAddressInput;
  const address = input.value.trim();
  if (!address) return;

  await api.createBranch(address);

  input.value = '';

  state.branches = await api.fetchBranches();
  dom.renderBranches(state.branches);
  await loadBranchesIntoModal();
}

export async function handleCreateProductFromModal() {
  const name = dom.elements.newProductNameInput.value.trim();
  const price = parseFloat(dom.elements.newProductPriceInput.value);

  // validation
  if (!name || name.length < 2) {
    alert('Product name is required');
    return;
  }

  if (isNaN(price) || price <= 0) {
    alert('Price must be greater than 0');
    return;
  }

  // API call
  await api.createProduct({ name, price });

  // reset inputs
  dom.elements.newProductNameInput.value = '';
  dom.elements.newProductPriceInput.value = '';

  // refresh state + UI
  state.products = await api.fetchProducts();
  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));

  dom.renderProducts(state.products);
  dom.renderProductTable(state.products);
}
