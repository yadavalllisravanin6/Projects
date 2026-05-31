// frontend/src/components/StatusBadge.jsx
import React from "react";

const STATUS_CONFIG = {
  COMPLETED: { bg: "#d1fae5", color: "#065f46", label: "✅ Completed" },
  PENDING:   { bg: "#fef3c7", color: "#92400e", label: "⏳ Pending"   },
  FAILED:    { bg: "#fee2e2", color: "#991b1b", label: "❌ Failed"     },
};

export default function StatusBadge({ status }) {
  const cfg = STATUS_CONFIG[status] || STATUS_CONFIG.PENDING;
  return (
    <span style={{
      padding: "3px 12px",
      borderRadius: "999px",
      fontSize: "12px",
      fontWeight: 600,
      background: cfg.bg,
      color: cfg.color,
      whiteSpace: "nowrap",
    }}>
      {cfg.label}
    </span>
  );
}
