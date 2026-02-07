import * as branchApi from '../api/branchApi.js';
import * as productApi from '../api/productApi.js';
import { state } from '../state/state.js';
import * as branchCard from '../dom/render/cards/branchCard.js';
import * as productCard from '../dom/render/cards/productCard.js';

export async function initApp() {
  state.branches = await branchApi.fetchBranches();
  state.products = await productApi.fetchProducts();

  state.productMap.clear();
  state.products.forEach(p => state.productMap.set(p.id, p));

  branchCard.renderBranchesDropdown(state.branches);
  productCard.renderProductsDropdown(state.products);
}
