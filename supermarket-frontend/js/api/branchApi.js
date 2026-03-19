import { API_BASE_URL, getAuthHeaders, handleUnauthorized } from "./api.js";

export async function fetchBranches() {
  const res = await fetch(`${API_BASE_URL}/branches`, {
    method: "GET",
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
  return res.json();
}

export async function createBranch(address) {
  const res = await fetch(`${API_BASE_URL}/branches`, {
    method: 'POST',
    headers: getAuthHeaders(),
    body: JSON.stringify({ address })
  });
  handleUnauthorized(res);
  return res.json();
}

export async function deleteBranch(branchId) {
  const res = await fetch(`${API_BASE_URL}/branches/${branchId}`, {
    method: 'DELETE',
    headers: getAuthHeaders()
  });
  handleUnauthorized(res);
}