// frontend/src/components/StatsBar.jsx
import React from "react";

export default function StatsBar({ reviews }) {
  const total     = reviews.length;
  const completed = reviews.filter(r => r.status === "COMPLETED").length;
  const pending   = reviews.filter(r => r.status === "PENDING").length;
  const failed    = reviews.filter(r => r.status === "FAILED").length;

  const stats = [
    { label: "Total Reviews", value: total,     color: "#1e1b4b" },
    { label: "Completed",     value: completed, color: "#065f46" },
    { label: "Pending",       value: pending,   color: "#92400e" },
    { label: "Failed",        value: failed,    color: "#991b1b" },
  ];

  return (
    <div style={{ display: "flex", gap: "12px", marginBottom: "20px", flexWrap: "wrap" }}>
      {stats.map(stat => (
        <div
          key={stat.label}
          style={{
            flex: "1 1 120px",
            background: "#fff",
            borderRadius: "10px",
            padding: "16px",
            border: "1px solid #e5e7eb",
            textAlign: "center",
          }}
        >
          <div style={{ fontSize: "28px", fontWeight: 700, color: stat.color }}>
            {stat.value}
          </div>
          <div style={{ fontSize: "12px", color: "#6b7280", marginTop: "4px" }}>
            {stat.label}
          </div>
        </div>
      ))}
    </div>
  );
}
