// frontend/src/components/ReviewCard.jsx
import React from "react";
import StatusBadge from "./StatusBadge";

export default function ReviewCard({ review, onClick }) {
  const date = new Date(review.createdAt).toLocaleString();

  return (
    <div
      onClick={() => onClick(review)}
      role="button"
      tabIndex={0}
      onKeyDown={e => e.key === "Enter" && onClick(review)}
      style={{
        border: "1px solid #e5e7eb",
        borderRadius: "10px",
        padding: "18px 20px",
        marginBottom: "12px",
        cursor: "pointer",
        background: "#fff",
        transition: "box-shadow 0.15s, border-color 0.15s",
      }}
      onMouseEnter={e => {
        e.currentTarget.style.boxShadow = "0 4px 14px rgba(0,0,0,0.08)";
        e.currentTarget.style.borderColor = "#a5b4fc";
      }}
      onMouseLeave={e => {
        e.currentTarget.style.boxShadow = "none";
        e.currentTarget.style.borderColor = "#e5e7eb";
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start", gap: "12px" }}>
        <div style={{ flex: 1, minWidth: 0 }}>
          <div style={{ fontWeight: 600, fontSize: "15px", color: "#111827", marginBottom: "6px" }}>
            {review.prTitle}
          </div>
          <div style={{ fontSize: "13px", color: "#6b7280" }}>
            📁 <strong>{review.repoFullName}</strong>
            &nbsp;·&nbsp; PR <strong>#{review.prNumber}</strong>
            &nbsp;·&nbsp; by <strong>@{review.prAuthor}</strong>
          </div>
          <div style={{ fontSize: "12px", color: "#9ca3af", marginTop: "4px" }}>
            🕒 {date}
          </div>
        </div>
        <StatusBadge status={review.status} />
      </div>
    </div>
  );
}
