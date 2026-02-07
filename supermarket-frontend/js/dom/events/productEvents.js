import * as productApi from "../../api/productApi.js";
import { renderProductsDropdown } from "../render/cards/productCard.js";
import { renderProductModalTable } from "../render/modals/productModal.js";
import { state } from "../../state/state.js";

export function bindProductDelete(row, product) {
  const deleteBtn = row.querySelector('.delete-product-btn');

  deleteBtn.onclick = async () => {
    const confirmed = confirm(`Delete product "${product.name}"?`);
    if (!confirmed) return;

    deleteBtn.disabled = true;

    await productApi.deleteProduct(product.id);

    // refresh state
    state.products = await productApi.fetchProducts();
    state.productMap.clear();
    state.products.forEach(p => state.productMap.set(p.id, p));

    // refresh UI
    renderProductsDropdown(state.products);
    renderProductModalTable(state.products);
  };
}