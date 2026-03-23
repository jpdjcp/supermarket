//export const API_BASE_URL = 'https://supermarket-api-jpdjcp.fly.dev/api/v1';
export const API_BASE_URL = 'http://localhost:8080/api/v1';

export function getAuthHeaders() {
  const token = localStorage.getItem("token");

  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };
}

export function handleUnauthorized(res) {
  if (res.status === 401) {
    localStorage.removeItem("token");
    window.location.href = "/auth.html";
  }
}