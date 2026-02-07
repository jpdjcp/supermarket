import { elements  } from "../../elements.js";

export function renderBranchModalTable(branches, { onDelete }) {
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