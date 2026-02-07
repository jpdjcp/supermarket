import { handleUpdateProductPrice } from "../../../handlers/productHandlers.js";

export function createProductRow(product) {
  const row = document.createElement('tr');

  row.innerHTML = `
    <td>${product.id}</td>
    <td>${product.name}</td>

    <td class="price-cell">
      <span class="price-text">$${product.price.toFixed(2)}</span>
    </td>

    <td>
      <button class="btn btn-sm btn-outline-secondary edit-price-btn">
        <i class="bi bi-pencil"></i>
      </button>
    </td>
      
    <td class="text-end">
      <button class="btn btn-sm btn-danger delete-product-btn">
        <i class="bi bi-trash"></i>
      </button>
    </td>
  `;

  const priceCell = row.querySelector('.price-cell');
  const editBtn = row.querySelector('.edit-price-btn');

  attachEditPriceBehavior({
    product,
    priceCell,
    editBtn
  });

  return row;
}

function attachEditPriceBehavior({ product, priceCell, editBtn }) {
  editBtn.onclick = async () => {
    const input = priceCell.querySelector('.price-input');

    // SAVE MODE
    if (input) {
      await savePriceEdit(product, priceCell, editBtn);
      return;
    }

    // EDIT MODE
    enterEditMode(product, priceCell, editBtn);
  };
}

function enterEditMode(product, priceCell, editBtn) {
  priceCell.innerHTML = `
    <input
      type="number"
      step="0.01"
      class="form-control form-control-sm price-input"
      value="${product.price}"
    />
  `;

  editBtn.innerHTML = '<i class="bi bi-save"></i>';
}

async function savePriceEdit(product, priceCell, editBtn) {
  const input = priceCell.querySelector('.price-input');
  const newPrice = parseFloat(input.value);

  if (isNaN(newPrice) || newPrice < 0) {
    alert('Invalid price');
    return;
  }

  const oldPrice = product.price;

  try {
    await handleUpdateProductPrice(product, newPrice);

    priceCell.innerHTML = `
      <span class="price-text">$${product.price.toFixed(2)}</span>
    `;
  } catch {
    alert('Failed to save price. Reverting.');
    priceCell.innerHTML = `
      <span class="price-text">$${oldPrice.toFixed(2)}</span>
    `;
  }

  editBtn.innerHTML = '<i class="bi bi-pencil"></i>';
}
