// frontend/src/components/ReviewModal.jsx
import React, { useEffect } from "react";
import StatusBadge from "./StatusBadge";

export default function ReviewModal({ review, onClose }) {
  // Close on Escape key
  useEffect(() => {
    const handler = e => { if (e.key === "Escape") onClose(); };
    window.addEventListener("keydown", handler);
    return () => window.removeEventListener("keydown", handler);
  }, [onClose]);

  if (!review) return null;

  return (
    <div
      onClick={onClose}
      style={{
        position: "fixed", inset: 0,
        background: "rgba(0,0,0,0.5)",
        display: "flex", alignItems: "center", justifyContent: "center",
        zIndex: 1000, padding: "16px",
      }}
    >
      {/* Stop click from closing when clicking inside modal */}
      <div
        onClick={e => e.stopPropagation()}
        style={{
          background: "#fff",
          borderRadius: "14px",
          padding: "28px",
          width: "100%",
          maxWidth: "720px",
          maxHeight: "85vh",
          overflowY: "auto",
          position: "relative",
        }}
      >
        {/* Close button */}
        <button
          onClick={onClose}
          aria-label="Close"
          style={{
            position: "absolute", top: "16px", right: "16px",
            background: "#f3f4f6", border: "none", borderRadius: "50%",
            width: "32px", height: "32px", fontSize: "18px",
            cursor: "pointer", color: "#374151", lineHeight: "32px",
          }}
        >
          ×
        </button>

        {/* Header */}
        <h2 style={{ fontSize: "18px", fontWeight: 700, marginBottom: "6px", paddingRight: "40px" }}>
          {review.prTitle}
        </h2>
        <div style={{ fontSize: "13px", color: "#6b7280", marginBottom: "6px" }}>
          📁 {review.repoFullName} &nbsp;·&nbsp; PR #{review.prNumber}
          &nbsp;·&nbsp; by @{review.prAuthor}
        </div>
        <div style={{ marginBottom: "20px" }}>
          <StatusBadge status={review.status} />
        </div>

        <hr style={{ border: "none", borderTop: "1px solid #e5e7eb", marginBottom: "20px" }} />

        {/* AI Review */}
        <h3 style={{ fontSize: "14px", fontWeight: 600, color: "#374151", marginBottom: "10px" }}>
          🤖 AI Code Review
        </h3>
        <pre style={{
          background: "#f8fafc",
          borderRadius: "8px",
          padding: "16px",
          fontSize: "13px",
          lineHeight: "1.7",
          whiteSpace: "pre-wrap",
          wordBreak: "break-word",
          border: "1px solid #e2e8f0",
          color: "#1e293b",
          fontFamily: "'Fira Code', 'Courier New', monospace",
        }}>
          {review.reviewComment || "No review content available."}
        </pre>

        {/* Diff (collapsed by default) */}
        {review.diffContent && (
          <details style={{ marginTop: "20px" }}>
            <summary style={{
              cursor: "pointer", fontSize: "13px", fontWeight: 600,
              color: "#6b7280", userSelect: "none",
            }}>
              📄 View raw diff
            </summary>
            <pre style={{
              background: "#0f172a",
              color: "#e2e8f0",
              borderRadius: "8px",
              padding: "16px",
              fontSize: "12px",
              lineHeight: "1.6",
              whiteSpace: "pre-wrap",
              wordBreak: "break-word",
              marginTop: "10px",
              fontFamily: "'Fira Code', 'Courier New', monospace",
              maxHeight: "300px",
              overflowY: "auto",
            }}>
              {review.diffContent}
            </pre>
          </details>
        )}
      </div>
    </div>
  );
}
