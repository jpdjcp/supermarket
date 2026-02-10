const API_BASE_URL = 'https://supermarket-api-jpdjcp.fly.dev/api/v1';

export async function createSale(branchId) {
  const res = await fetch(`${API_BASE_URL}/sales`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ branchId })
  });
  return res.json();
}

export async function finishSale(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/finish`, {
    method: 'POST'
  });
  return res.json();
}

export async function cancelSale(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/cancel`, {
    method: 'POST'
  });
  return res.json();
}

export async function fetchSaleItems(saleId) {
  const res = await fetch(`${API_BASE_URL}/sales/${saleId}/items`);
  return res.json();
}

export async function addProductToSale(saleId, productId) {
  await fetch(`${API_BASE_URL}/sales/${saleId}/items`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        productId: productId,
        quantity: 1
    })
  });
}

export async function increaseItem(saleId, productId) {
  await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}/increase`,
    { method: 'PATCH' }
  );
}

export async function decreaseItem(saleId, productId) {
  await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}/decrease`,
    { method: 'PATCH' }
  );
}

export async function deleteItem(saleId, productId) {
  await fetch(
    `${API_BASE_URL}/sales/${saleId}/items/${productId}`,
    { method: 'DELETE' }
  );
}