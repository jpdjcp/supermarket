import { API_BASE_URL } from "../api/api";

const form = document.getElementById("auth-form");
const toggleText = document.getElementById("toggle-text");
const title = document.getElementById("form-title");
const errorDiv = document.getElementById("error");

let isLogin = true;

toggleText.addEventListener("click", () => {
  isLogin = !isLogin;

  title.textContent = isLogin ? "Login" : "Sign Up";

  toggleText.textContent = isLogin
    ? "Don't have an account? Sign up"
    : "Already have an account? Login";
});

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const username = document.getElementById("username").value;
  const password = document.getElementById("password").value;

  errorDiv.textContent = "";

  try {
    const url = isLogin
      ? API_BASE_URL + "/auth/login"
      : API_BASE_URL + "/auth/signup";

    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password })
    });

    if (!res.ok) throw new Error();

    const data = await res.json();

    // Save JWT
    localStorage.setItem("token", data.token);
    console.log(data.token);
    // Redirect to main app
    window.location.href = "/index.html";

  } catch (err) {
    errorDiv.textContent = "Invalid credentials";
  }
});