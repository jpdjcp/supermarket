import { state } from "../state/state.js";
import { elements } from './../dom/elements.js';
import * as saleApi from "../api/saleApi.js";
import * as saleCard from '../dom/render/cards/saleCard.js';
import { updateSaleActionsUI } from "../dom/render/saleActions.js";

export async function handleCreateSale() {
  
  const branchId = elements.branchSelect.value;
  if (!branchId) return;

  await createSale(branchId);
  updateSaleActionsUI();
  saleCard.updateSaleStatusBadge(state.currentSaleStatus);
}

async function createSale(branchId) {
  const sale = await saleApi.createSale(branchId);
  applySale(sale);
  await loadSaleItems();
}

export async function loadSaleItems() {
  state.saleItems = await saleApi.fetchSaleItems(state.currentSaleId);

  saleCard.renderSaleTable(state.saleItems, {
    onIncrease: async productId => {
      await saleApi.increaseItem(state.currentSaleId, productId);
      await loadSaleItems();
    },
    onDecrease: async productId => {
      await saleApi.decreaseItem(state.currentSaleId, productId);
      await loadSaleItems();
    },
    onDelete: async productId => {
      await saleApi.deleteItem(state.currentSaleId, productId);
      await loadSaleItems();
    }
  });

  updateSaleActionsUI();
}

export async function handleFinishSale() {
  const sale = await saleApi.finishSale(state.currentSaleId);
  applySale(sale);
  saleCard.updateSaleStatusBadge(state.currentSaleStatus);
  updateSaleActionsUI();
}

export async function handleCancelSale() {
  const sale = await saleApi.cancelSale(state.currentSaleId);
  applySale(sale);
  saleCard.updateSaleStatusBadge(state.currentSaleStatus);
  updateSaleActionsUI();
}

function applySale(sale) {
  state.currentSaleId = sale.id;
  state.currentSaleStatus = sale.status;
}