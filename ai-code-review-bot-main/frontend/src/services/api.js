// frontend/src/services/api.js
// All API calls to the Spring Boot backend live here

const API_BASE = process.env.REACT_APP_API_URL || "http://localhost:8080/api";

/**
 * Fetch the 10 most recent reviews
 */
export async function fetchRecentReviews() {
  const res = await fetch(`${API_BASE}/reviews/recent`);
  if (!res.ok) throw new Error(`Server error: ${res.status}`);
  return res.json();
}

/**
 * Fetch all reviews for a specific repo (e.g. "owner/repo-name")
 */
export async function fetchReviewsByRepo(repoFullName) {
  const encoded = encodeURIComponent(repoFullName);
  const res = await fetch(`${API_BASE}/reviews/repo?name=${encoded}`);
  if (!res.ok) throw new Error(`Server error: ${res.status}`);
  return res.json();
}

/**
 * Fetch a single review by ID
 */
export async function fetchReviewById(id) {
  const res = await fetch(`${API_BASE}/reviews/${id}`);
  if (!res.ok) throw new Error(`Server error: ${res.status}`);
  return res.json();
}

/**
 * Check if the backend is healthy
 */
export async function checkHealth() {
  const res = await fetch(`${API_BASE}/webhook/health`);
  if (!res.ok) throw new Error("Backend is down");
  return res.json();
}
