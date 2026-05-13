---
name: 'mobb-mobb-custom-injection-0b0fcd4501d566e9'
description: 'Prevent SQL injection in Java via string concatenation in query construction. Trigger when: `+ name +` in SQL query string concatenation, `executeQuery(` or `executeUpdate(` with concatenated string argument, `Statement stmt = conn.createStatement()` followed by query concatenation, `WHERE` clause with `+ variable +` in string literal, `LIKE ''%" + variable + "%''` pattern in SQL, `DELETE FROM` or `SELECT` with `+ id` or similar variable concatenation. Skip when: PreparedStatement with parameterized queries using `?` placeholders and `setInt()`, `setString()`, etc., enquoteLiteral() method used to escape user input before concatenation, Input is validated and converted to a safe type (e.g., Integer.parseInt) before use in query, Query uses only hardcoded string literals with no user-supplied variables.'
---

## Why this matters
SQL Injection (CWE-89) allows attackers to execute arbitrary SQL commands by injecting malicious input into dynamically constructed queries. String concatenation bypasses SQL parsing and enables attackers to alter query logic, exfiltrate data, or modify/delete records. PreparedStatement with parameterized queries separates SQL structure from data, ensuring user input is treated as data only and cannot alter command semantics.

## Anti-patterns (do not write)

**Trigger token:** `+ name +`

```Java
String query = "SELECT id, name, price FROM products WHERE name LIKE '%"
                + name + "%'";
ResultSet rs = stmt.executeQuery(query);
```

**Trigger token:** `executeUpdate("DELETE FROM products WHERE id = " + id)`

```Java
Statement stmt = conn.createStatement();
stmt.executeUpdate("DELETE FROM products WHERE id = " + id);
```

**Trigger token:** `executeQuery(query)`

```Java
String query = "SELECT * FROM users WHERE username = '" + username + "'";
stmt.executeQuery(query);
```

## Safe patterns

```Java
String query = "SELECT id, name, price FROM products WHERE name LIKE "
                + conn.createStatement().enquoteLiteral(String.valueOf("%"
                + name + "%"));
ResultSet rs = stmt.executeQuery(query);
```

```Java
PreparedStatement stmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
stmt.setInt(1, Math.round(Float.parseFloat(id)));
stmt.executeUpdate();
```

```Java
PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
stmt.setString(1, username);
ResultSet rs = stmt.executeQuery();
```

## Review checklist
- [ ] Does the code use Statement.executeQuery() or executeUpdate() with a query string built via string concatenation (+ operator) rather than PreparedStatement with ? placeholders?
- [ ] Are user-supplied variables (from request parameters, form inputs, or external sources) directly concatenated into SQL query strings without parameterization?
- [ ] If enquoteLiteral() is used, is it applied to the entire user-controlled value before concatenation, or only to parts of it?
- [ ] Does the code use PreparedStatement with setString(), setInt(), or other type-safe setter methods for all user-supplied values?
- [ ] Are numeric inputs validated and converted to their intended type (e.g., Integer.parseInt()) before being used in parameterized queries, with exception handling for invalid formats?
- [ ] Does the code avoid mixing parameterized queries with string concatenation in the same query construction?
- [ ] Are all database operations using PreparedStatement or other parameterized query mechanisms rather than raw Statement concatenation?
