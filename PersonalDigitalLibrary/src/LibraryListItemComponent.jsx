function LibraryListItemComponent({ data }) {
  return (
    <div>
      <h2>Library List</h2>
      <table border="1" cellPadding="10" style={{ borderCollapse: 'collapse', width: '100%' }}>
        <thead>
          <tr>
            <th>Title</th>
            <th>Type</th>
            <th>Description</th>
          </tr>
        </thead>
        <tbody>
          {data.map((book, index) => (
            <tr key={index}>
              <td>{book.title}</td>
              <td>{book.type}</td>
              <td>{book.description}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default LibraryListItemComponent;
