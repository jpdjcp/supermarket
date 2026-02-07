import { elements } from './../dom/elements.js';
import * as branchApi from "../api/branchApi.js";
import { state } from '../state/state.js';
import * as branchCard from '../dom/render/cards/branchCard.js';
import * as branchModal from '../dom/render/modals/branchModal.js';

export async function handleCreateBranchFromModal() {
  const input = elements.newBranchAddressInput;
  const address = input.value.trim();
  if (!address) return;

  await branchApi.createBranch(address);

  input.value = '';

  state.branches = await branchApi.fetchBranches();
  branchCard.renderBranchesDropdown(state.branches); // the same we talk about recently
  await loadBranchesIntoModal();
}

export async function loadBranchesIntoModal() {
  state.branches = await branchApi.fetchBranches();

  // delete branch
  branchModal.renderBranchModalTable(state.branches, {
    onDelete: async branchId => {
      const confirmed = confirm('Delete this branch?');
      if (!confirmed) return;

      await branchApi.deleteBranch(branchId);

      // reload state
      state.branches = await branchApi.fetchBranches();

      // refresh Branch card dropdown
      branchCard.renderBranchesDropdown(state.branches); // again
      await loadBranchesIntoModal();
    }
  });
}