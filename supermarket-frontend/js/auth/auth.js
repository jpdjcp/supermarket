const form = document.getElementById("auth-form");
const toggleText = document.getElementById("toggle-text");
const title = document.getElementById("form-title");
const usernameGroup = document.getElementById("username-group");
const errorDiv = document.getElementById("error");

let isLogin = true;

toggleText.addEventListener("click", () => {
  isLogin = !isLogin;

  title.textContent = isLogin ? "Login" : "Sign Up";
  usernameGroup.classList.toggle("d-none");

  toggleText.textContent = isLogin
    ? "Don't have an account? Sign up"
    : "Already have an account? Login";
});

form.addEventListener("submit", async (e) => {
  e.preventDefault();

  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;
  const username = document.getElementById("username").value;

  try {
    const url = isLogin
      ? "/auth/login"
      : "/auth/signup";

    const body = isLogin
      ? { email, password }
      : { username, email, password };

    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body)
    });

    if (!res.ok) throw new Error("Auth failed");

    const data = await res.json();

    // Save JWT
    localStorage.setItem("token", data.token);

    // Redirect
    window.location.href = "/index.html";

  } catch (err) {
    errorDiv.textContent = "Invalid credentials";
  }
});