import { elements } from "../../elements.js";
import { state } from "../../../state/state.js";

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