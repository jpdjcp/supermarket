import { elements } from "../../elements.js";
import { createProductRow } from "./productRow.js";
import { bindProductDelete } from "../../events/productEvents.js";

export function renderProductModalTable(products, { targetTbody = null } = {}) {
  const tbody = targetTbody ?? elements.productTableBody;
  tbody.innerHTML = '';

  products.forEach(product => {
    const row = createProductRow(product);
    tbody.appendChild(row);

    bindProductDelete(row, product);
  });
}
