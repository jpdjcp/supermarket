const API_BASE_URL = 'https://supermarket-api-jpdjcp.fly.dev/api/v1';

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