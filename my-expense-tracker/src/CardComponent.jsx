import './CardComponent.css';
import React, { useState } from 'react';

function CardComponent() {
  const [expenses, setExpenses] = useState([]);
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState('');
  const initialDate = new Date();
  const [date, setDate] = useState(initialDate.toISOString().slice(0, 10)); // yyyy-MM-dd

  function addExpense(event) {
    event.preventDefault();
    if (!description || !amount) return; // avoid empty entries
    const newExpense = { description, amount: parseFloat(amount), date: new Date(date) };
    setExpenses([...expenses, newExpense]);
    // reset input fields
    setDescription("");
    setAmount("");
    setDate(initialDate.toISOString().slice(0, 10));
  }

  function deleteExpense(indexToDelete) {
    const updatedExpenses = expenses.filter((_, index) => index !== indexToDelete);
    setExpenses(updatedExpenses);
  }

  return (
    <>
      <h1 className="title">My Personal Tracker</h1>

      <div className="input-section">
        <input
          type="text"
          placeholder="Description"
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <input
          type="number"
          placeholder="Amount"
          value={amount}
          onChange={(e) => setAmount(e.target.value)}
        />
        <input
          type="date"
          value={date}
          onChange={(e) => setDate(e.target.value)}
        />
        <button className="button" onClick={addExpense}>Add Expense</button>
      </div>

      <table className="expenses-table">
        <thead>
          <tr>
            <th>Description</th>
            <th>Amount ($)</th>
            <th>Date</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {expenses.length === 0 ? (
            <tr>
              <td colSpan="4" style={{ textAlign: "center" }}>No expenses yet.</td>
            </tr>
          ) : (
            expenses.map((ex, index) => (
              <tr key={index}>
                <td>{ex.description}</td>
                <td>{ex.amount}</td>
                <td>{new Date(ex.date).toLocaleDateString()}</td>
                <td>
                  <button className="delete-btn" onClick={() => deleteExpense(index)}>Delete</button>
                </td>
              </tr>
            ))
          )}
        </tbody>
      </table>
    </>
  );
}

export default CardComponent;
