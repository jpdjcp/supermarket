import { API_BASE_URL, getAuthHeaders, handleUnauthorized } from "./api.js";

export async function fetchProducts() {
  const res = await fetch(`${API_BASE_URL}/products`, {
    method: "GET",
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
  return res.json();
}

export async function updateProductPrice(productId, price) {
  const res = await fetch(API_BASE_URL + `/products/${productId}`, {
    method: 'PATCH',
    headers: getAuthHeaders(),
    body: JSON.stringify({ price })
  });

  handleUnauthorized(res);

  if (!res.ok) {
    throw new Error('Failed to update product price');
  }
  return res.json();
}

export async function createProduct(product) {
  const response = await fetch(API_BASE_URL + `/products`, {
      method: 'POST',
      headers: getAuthHeaders(),
      body: JSON.stringify(product)
    });

  handleUnauthorized(response);
  
  if (!response.ok) {
    throw new Error('Failed to create product');
  }
  return response;
}

export async function deleteProduct(productId) {
  const response = await fetch(`${API_BASE_URL}/products/${productId}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });

  handleUnauthorized(response);
  
  if (!response.ok) {
    throw new Error('Failed to delete product');
  }
}