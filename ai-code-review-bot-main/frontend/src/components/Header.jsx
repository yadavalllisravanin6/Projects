// frontend/src/components/Header.jsx
import React from "react";

export default function Header({ backendStatus }) {
  return (
    <div style={{
      background: "#1e1b4b",
      color: "#fff",
      padding: "18px 32px",
      display: "flex",
      alignItems: "center",
      justifyContent: "space-between",
      flexWrap: "wrap",
      gap: "12px",
    }}>
      <div>
        <div style={{ fontSize: "22px", fontWeight: 700, letterSpacing: "-0.5px" }}>
          🤖 AI Code Review Bot
        </div>
        <div style={{ fontSize: "13px", color: "#a5b4fc", marginTop: "2px" }}>
          Powered by Claude AI · Spring Boot + React
        </div>
      </div>

      <div style={{ display: "flex", alignItems: "center", gap: "16px" }}>
        {/* Backend status indicator */}
        <div style={{ display: "flex", alignItems: "center", gap: "6px", fontSize: "12px" }}>
          <span style={{
            width: "8px", height: "8px", borderRadius: "50%",
            background: backendStatus === "UP" ? "#34d399" : "#f87171",
            display: "inline-block",
          }} />
          <span style={{ color: "#c7d2fe" }}>
            Backend {backendStatus === "UP" ? "Online" : "Offline"}
          </span>
        </div>

        <code style={{
          background: "#312e81",
          color: "#c7d2fe",
          padding: "4px 10px",
          borderRadius: "6px",
          fontSize: "12px",
        }}>
          POST /api/webhook/github
        </code>
      </div>
    </div>
  );
}
