const API_BASE_URL = 'https://supermarket-api-jpdjcp.fly.dev/api/v1';

// ---------- BRANCHES ----------

export async function fetchBranches() {
  const res = await fetch(`${API_BASE_URL}/branches`);
  return res.json();
}

export async function createBranch(address) {
  const res = await fetch(`${API_BASE_URL}/branches`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ address })
  });
  return res.json();
}

export async function deleteBranch(branchId) {
  await fetch(`${API_BASE_URL}/branches/${branchId}`, {
    method: 'DELETE'
  });
}

// ---------- PRODUCTS ----------

export async function fetchProducts() {
  const res = await fetch(`${API_BASE_URL}/products`);
  return res.json();
}

export async function updateProductPrice(productId, price) {
  const res = await fetch(API_BASE_URL + `/products/${productId}`, {
    method: 'PATCH',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({ price })
  });

  if (!res.ok) {
    throw new Error('Failed to update product price');
  }

  return res.json();
}

export async function createProduct(product) {
  const response = await fetch(API_BASE_URL + `/products`, 
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(product)
    }
  )
  if (!response.ok) {
    throw new Error('Failed to create product');
  }
  
  return response;
}

export async function deleteProduct(productId) {
  const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
    method: 'DELETE'
  });

  if (!response.ok) {
    throw new Error('Failed to delete product');
  }
}

// ---------- SALES ----------

export async function createSale(branchId) {
  const res = await fetch(`${API_BASE_URL}/sales`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ branchId })
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
