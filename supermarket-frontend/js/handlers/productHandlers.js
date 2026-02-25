import * as productApi from "../api/productApi.js";
import * as saleApi from "../api/saleApi.js";
import { state } from "../state/state.js";
import { elements } from './../dom/elements.js';
import * as productCard from '../dom/render/cards/productCard.js';
import * as productModal from '../dom/render/modals/productModal.js';
import { loadSaleItems } from "./saleHandlers.js";
import { updateSaleActionsUI } from "./../dom/render/saleActions.js"

export async function handleCreateProductFromModal() {
  const name = elements.newProductNameInput.value.trim();
  const sku = elements.newProductSkuInput.value.trim();
  const price = parseFloat(elements.newProductPriceInput.value);

  // validation
  if (!name || name.length < 2) {
    alert('Product name is required');
    return;
  }

  if (!sku || sku.length < 6) {
    alert('Product SKU is required');
    return;
  }

  if (isNaN(price) || price <= 0) {
    alert('Price must be greater than 0');
    return;
  }

  // API call
  await productApi.createProduct({ sku, name, price });

  // reset inputs
  elements.newProductNameInput.value = '';
  elements.newProductPriceInput.value = '';

  // refresh state
  state.products = await productApi.fetchProducts();
  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));

  // refresh UI
  productCard.renderProductsDropdown(state.products);
  productModal.renderProductModalTable(state.products);
}

export async function handleAddProduct() {
  const productId = elements.productSelect.value;
  await saleApi.addProductToSale(state.currentSaleId, productId);
  await loadSaleItems();
  updateSaleActionsUI();
}

export async function handleUpdateProductPrice(product, newPrice) {
  await productApi.updateProductPrice(product.id, newPrice);
  product.price = newPrice;
  state.productMap.set(product.id, product);
}

export async function loadProductsIntoModal() {
  await refreshProductsState();

  // delete product
  productModal.renderProductModalTable(state.products, {
    onDelete: async productId => {
      const confirmed = confirm('Delete this product?');
      if (!confirmed) return;

      await productApi.deleteProduct(productId);
      await loadProductsIntoModal(); // refresh modal
      productCard.renderProductsDropdown(state.products); // refresh dropdown card
    }
  });
}

async function refreshProductsState() {
  state.products = await productApi.fetchProducts();
  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));
}
