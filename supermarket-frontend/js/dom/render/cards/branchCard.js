import { elements } from "../../elements.js";

export function renderBranchesDropdown(branches) {
  elements.branchSelect.innerHTML = '';

  branches.forEach(branch => {
    const option = document.createElement('option');
    option.value = branch.id;
    option.textContent = `#${branch.id} - ${branch.address}`;
    elements.branchSelect.appendChild(option);
  });
}