// frontend/src/App.jsx
import React, { useState, useEffect, useCallback } from "react";
import Header from "./components/Header";
import StatsBar from "./components/StatsBar";
import ReviewCard from "./components/ReviewCard";
import ReviewModal from "./components/ReviewModal";
import { fetchRecentReviews, fetchReviewsByRepo, checkHealth } from "./services/api";

export default function App() {
  const [reviews, setReviews]           = useState([]);
  const [loading, setLoading]           = useState(true);
  const [error, setError]               = useState(null);
  const [selectedReview, setSelected]   = useState(null);
  const [repoFilter, setRepoFilter]     = useState("");
  const [backendStatus, setBackendStatus] = useState("checking");

  // Check backend health on mount
  useEffect(() => {
    checkHealth()
      .then(() => setBackendStatus("UP"))
      .catch(() => setBackendStatus("DOWN"));
  }, []);

  const loadReviews = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const data = repoFilter.trim()
        ? await fetchReviewsByRepo(repoFilter.trim())
        : await fetchRecentReviews();
      setReviews(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  }, [repoFilter]);

  // Load on mount
  useEffect(() => { loadReviews(); }, []);

  // Auto-refresh every 30 seconds
  useEffect(() => {
    const interval = setInterval(loadReviews, 30000);
    return () => clearInterval(interval);
  }, [loadReviews]);

  const handleSearch = e => {
    e.preventDefault();
    loadReviews();
  };

  return (
    <div style={{ minHeight: "100vh", background: "#f3f4f6" }}>
      <Header backendStatus={backendStatus} />

      <div style={{ maxWidth: "860px", margin: "32px auto", padding: "0 16px" }}>

        {/* Search / Filter bar */}
        <form onSubmit={handleSearch} style={{ display: "flex", gap: "10px", marginBottom: "20px" }}>
          <input
            type="text"
            placeholder="Filter by repo (e.g. sandeepreddythippareddy/my-project)"
            value={repoFilter}
            onChange={e => setRepoFilter(e.target.value)}
            style={{
              flex: 1, padding: "10px 14px", borderRadius: "8px",
              border: "1px solid #d1d5db", fontSize: "14px",
              outline: "none", transition: "border-color 0.15s",
            }}
            onFocus={e => e.target.style.borderColor = "#6366f1"}
            onBlur={e => e.target.style.borderColor = "#d1d5db"}
          />
          <button
            type="submit"
            style={{
              padding: "10px 22px", background: "#4f46e5", color: "#fff",
              border: "none", borderRadius: "8px", cursor: "pointer",
              fontSize: "14px", fontWeight: 600,
            }}
          >
            {repoFilter ? "Search" : "Refresh"}
          </button>
          {repoFilter && (
            <button
              type="button"
              onClick={() => { setRepoFilter(""); }}
              style={{
                padding: "10px 16px", background: "#e5e7eb", color: "#374151",
                border: "none", borderRadius: "8px", cursor: "pointer", fontSize: "14px",
              }}
            >
              Clear
            </button>
          )}
        </form>

        {/* Stats */}
        {!loading && !error && <StatsBar reviews={reviews} />}

        {/* States */}
        {loading && (
          <div style={{ textAlign: "center", padding: "60px 0", color: "#6b7280" }}>
            <div style={{ fontSize: "32px", marginBottom: "12px" }}>⏳</div>
            Loading reviews...
          </div>
        )}

        {error && (
          <div style={{
            background: "#fee2e2", color: "#991b1b",
            padding: "16px 20px", borderRadius: "10px", marginBottom: "16px",
          }}>
            <strong>Error:</strong> {error}
            <br />
            <span style={{ fontSize: "13px" }}>Make sure the Spring Boot backend is running on port 8080.</span>
          </div>
        )}

        {!loading && !error && reviews.length === 0 && (
          <div style={{ textAlign: "center", padding: "80px 0", color: "#9ca3af" }}>
            <div style={{ fontSize: "48px", marginBottom: "16px" }}>🔍</div>
            <div style={{ fontSize: "16px", fontWeight: 600, color: "#6b7280" }}>No reviews yet</div>
            <div style={{ fontSize: "14px", marginTop: "8px" }}>
              Open a Pull Request in your GitHub repo and the bot will review it automatically.
            </div>
          </div>
        )}

        {/* Review list */}
        {!loading && reviews.map(review => (
          <ReviewCard
            key={review.id}
            review={review}
            onClick={setSelected}
          />
        ))}

        {/* Auto-refresh note */}
        {!loading && reviews.length > 0 && (
          <div style={{ textAlign: "center", fontSize: "12px", color: "#9ca3af", marginTop: "16px" }}>
            Auto-refreshes every 30 seconds
          </div>
        )}
      </div>

      {/* Detail modal */}
      <ReviewModal review={selectedReview} onClose={() => setSelected(null)} />
    </div>
  );
}
