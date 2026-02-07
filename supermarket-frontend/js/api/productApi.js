const API_BASE_URL = 'https://supermarket-api-jpdjcp.fly.dev/api/v1';

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