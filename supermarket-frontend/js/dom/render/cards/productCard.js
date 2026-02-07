import { elements } from "../../elements.js";

export function renderProductsDropdown(products) {
  elements.productSelect.innerHTML = '';

  products.forEach(product => {
    const option = document.createElement('option');
    option.value = product.id;
    option.textContent = `#${product.id} - ${product.name}`;
    elements.productSelect.appendChild(option);
  });
}