import { API_BASE_URL, getAuthHeaders, handleUnauthorized } from "./api.js";

export async function createSale(branchId) {
  const res = await fetch(`${API_BASE_URL}/sales`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({ branchId })
  });
  //handleUnauthorized(res);
  return res.json();
}

export async function finishSale(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/finish`, {
    method: 'POST',
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
  return res.json();
}

export async function cancelSale(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/cancel`, {
    method: 'POST',
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
  return res.json();
}

export async function fetchSaleItems(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/items`, {
    method: "GET",
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
  return res.json();
}

export async function addProductToSale(saleId, productId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/items`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({
        productId: productId,
        quantity: 1
    })
  });
  handleUnauthorized(res);
}

export async function increaseItem(saleId, productId) {
  const res = await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}/increase`,{
      method: 'PATCH',
      headers: getAuthHeaders()
  });
  handleUnauthorized(res);
}

export async function decreaseItem(saleId, productId) {
  const res = await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}/decrease`, {
      method: 'PATCH',
      headers: getAuthHeaders()
    });
  handleUnauthorized(res);
}

export async function deleteItem(saleId, productId) {
  const res = await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}`, {
      method: 'DELETE',
      headers: getAuthHeaders()
    });
  handleUnauthorized(res);
}