import { state } from './state.js';
import * as api from './api.js';

export const elements = {
  branchSelect: document.getElementById('branchSelect'),
  createSaleBtn: document.getElementById('createSaleBtn'),
  productSelect: document.getElementById('productSelect'),
  saleTableBody: document.querySelector('#saleTable tbody'),
  totalCell: document.getElementById('totalCell'),
  addProductBtn: document.getElementById('addProductToSaleBtn'),
  finishSaleBtn: document.getElementById('finishSaleBtn'),
  cancelSaleBtn: document.getElementById('cancelSaleBtn'),

  // Branch Modal
  editBranchBtn: document.getElementById('editBranchBtn'),
  branchModal: document.getElementById('branchModal'),
  branchTableBody: document.getElementById('branchTableBody'),
  newBranchAddressInput: document.getElementById('newBranchAddress'),
  createBranchModalBtn: document.getElementById('createBranchModalBtn'),

  // Product Modal
  newProductNameInput: document.getElementById('newProductName'),
  newProductPriceInput: document.getElementById('newProductPrice'),
  createProductModalBtn: document.getElementById('createProductModalBtn'),
  editProductBtn: document.getElementById('editProductBtn'),
  productModal: document.getElementById('productModal'),
  productTableBody: document.getElementById('productTableBody')


};

// ---------- RENDER DROPDOWNS ----------

export function renderBranches(branches) {
  elements.branchSelect.innerHTML = '';

  branches.forEach(branch => {
    const option = document.createElement('option');
    option.value = branch.id;
    option.textContent = `#${branch.id} - ${branch.address}`;
    elements.branchSelect.appendChild(option);
  });
}

export function renderProducts(products) {
  elements.productSelect.innerHTML = '';

  products.forEach(product => {
    const option = document.createElement('option');
    option.value = product.id;
    option.textContent = `#${product.id} - ${product.name}`;
    elements.productSelect.appendChild(option);
  });
}

// ---------- SALE TABLE ----------

export function renderSaleTable(items, callbacks) {
  const { onIncrease, onDecrease, onDelete } = callbacks;

  elements.saleTableBody.innerHTML = '';
  let total = 0;

  items.forEach(item => {
    const product = state.productMap.get(item.productId);

    const productName = product?.name ?? 'Unknown';
    const unitPrice = product?.price ?? null;

    total += item.subtotal;

    const row = document.createElement('tr');
    row.innerHTML = `
      <td>${item.productId}</td>
      <td>${productName}</td>
      <td class="text-center">
        <button class="btn btn-sm btn-secondary quantity-btn">-</button>
        <span class="mx-2">${item.quantity}</span>
        <button class="btn btn-sm btn-secondary quantity-btn">+</button>
      </td>
      <td>
        ${unitPrice !== null ? `$${unitPrice.toFixed(2)}` : '-'}
      </td>
      <td>$${item.subtotal.toFixed(2)}</td>
      <td>
        <button class="btn btn-sm btn-danger">
          <i class="bi bi-trash"></i>
        </button>
      </td>
    `;

    const [minusBtn, plusBtn] = row.querySelectorAll('.quantity-btn');
    const deleteBtn = row.querySelector('.btn-danger');

    minusBtn.onclick = () => onDecrease(item.productId);
    plusBtn.onclick = () => onIncrease(item.productId);
    deleteBtn.onclick = () => onDelete(item.productId);

    elements.saleTableBody.appendChild(row);
  });

  elements.totalCell.textContent = `$${total.toFixed(2)}`;
}

// -------- UPDATE SALE-STATUS BADGE ---------
export function updateSaleStatusBadge(status) {
  const badge = document.getElementById('saleStatusBadge');

  if (!status) {
    badge.textContent = 'NO SALE';
    badge.className = 'badge bg-secondary';
    return;
  }

  badge.className = 'badge'; // reset
  badge.textContent = status;

  switch (status) {
    case 'OPEN':
      badge.classList.add('bg-success');
      break;
    case 'FINISHED':
      badge.classList.add('bg-primary');
      break;
    case 'CANCELLED':
      badge.classList.add('bg-danger');
      break;
    default:
      badge.classList.add('bg-secondary');
  }
}

// ---------- BRANCH MODAL TABLE ----------

export function renderBranchTable(branches, { onDelete }) {
  elements.branchTableBody.innerHTML = '';

  branches.forEach(branch => {
    const row = document.createElement('tr');

    row.innerHTML = `
      <td>${branch.id}</td>
      <td>${branch.address}</td>
      <td>
        <button class="btn btn-sm btn-danger">
          <i class="bi bi-trash"></i>
        </button>
      </td>
    `;

    const deleteBtn = row.querySelector('.btn-danger');
    deleteBtn.onclick = () => onDelete(branch.id);

    elements.branchTableBody.appendChild(row);
  });
}

// ---------- PRODUCT MODAL TABLE ----------

export function renderProductTable(products, { targetTbody } = {}) {
  const tbody = targetTbody ?? elements.productTableBody;
  tbody.innerHTML = '';

  products.forEach(product => {
    const row = createProductRow(product);
    tbody.appendChild(row);

    const deleteBtn = row.querySelector('.delete-product-btn');

    deleteBtn.onclick = async () => {
      const confirmed = confirm(`Delete product "${product.name}"?`);
      if (!confirmed) return;

      deleteBtn.disabled = true;
      await api.deleteProduct(product.id);

      // refresh state
      state.products = await api.fetchProducts();

      state.productMap.clear();
      state.products.forEach(p => state.productMap.set(p.id, p));

      // refresh UI
      renderProducts(state.products);
      renderProductTable(state.products);
    };
  });
}

// create new row
function createProductRow(product) {
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

// switch to edit price mode
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

// save new price
async function savePriceEdit(product, priceCell, editBtn) {
  const input = priceCell.querySelector('.price-input');
  const newPrice = parseFloat(input.value);

  if (isNaN(newPrice) || newPrice < 0) {
    alert('Invalid price');
    return;
  }

  const oldPrice = product.price;

  try {
    await api.updateProductPrice(product.id, newPrice);

    product.price = newPrice;
    state.productMap.set(product.id, product);

    priceCell.innerHTML = `
      <span class="price-text">$${product.price.toFixed(2)}</span>
    `;
  } catch (err) {
    alert('Failed to save price. Reverting.');
    priceCell.innerHTML = `
      <span class="price-text">$${oldPrice.toFixed(2)}</span>
    `;
  }

  editBtn.innerHTML = '<i class="bi bi-pencil"></i>';
}

