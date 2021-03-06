/* 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package java.sql;

import java.util.Map;

/**
 * A Connection represents a link from a Java application to a database. All SQL
 * statements and results are returned within the context of a connection.
 * 
 */
public interface Connection {

    /**
     * A constant indicating that transactions are not supported.
     */
    public static final int TRANSACTION_NONE = 0;

    /**
     * No dirty reads are permitted. Transactions may not read a row containing
     * changes that have not yet been committed.
     */
    public static final int TRANSACTION_READ_COMMITTED = 2;

    /**
     * Dirty reads (reading from table rows containing changes that have not yet
     * been committed), non-repeatable reads (reading table rows more than once
     * in a transaction but getting back different data because other
     * transactions may have altered rows between reads), and phantom reads
     * (retrieving additional "phantom" rows in the course of repeated table
     * reads because other transactions may have inserted additional rows that
     * satisfy an SQL <code>WHERE</code> clause) are <b>all permitted</b>.
     */
    public static final int TRANSACTION_READ_UNCOMMITTED = 1;

    /**
     * A constant indicating that dirty reads and non-repeatable reads are
     * prevented; phantom reads can occur.
     */
    public static final int TRANSACTION_REPEATABLE_READ = 4;

    /**
     * Dirty reads (reading from table rows containing changes that have not yet
     * been committed), non-repeatable reads (reading table rows more than once
     * in a transaction but getting back different data because other
     * transactions may have altered rows between reads), and phantom reads
     * (retrieving additional "phantom" rows in the course of repeated table
     * reads because other transactions may have inserted additional rows that
     * satisfy an SQL <code>WHERE</code> clause) are <b>all prevented</b>.
     */
    public static final int TRANSACTION_SERIALIZABLE = 8;

    /**
     * Throws away any warnings that may have arisen for this connection.
     * Subsequent calls to {@link #getWarnings()} will return <code>null</code>
     * up until a brand new warning condition occurs.
     * 
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void clearWarnings() throws SQLException;

    /**
     * Causes the instant release of all database and driver connection
     * resources associated with this object. Any subsequent invocations of this
     * method will have no effect.
     * <p>
     * It is strongly recommended that all Connections are closed before they
     * are dereferenced by the application ready for garbage collection. While
     * the finalize method of the Connection will close the Connection before
     * garbage collection takes place, it is not advisable to leave the close
     * operation to take place in this way. Unpredictable performance may result
     * from closing Connections in the finalizer.
     * 
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void close() throws SQLException;

    /**
     * Commits all of the changes made subsequent to the last commit or rollback
     * of the associated transaction. All locks in the database held by this
     * connection are also relinquished. Calling this operation on connection
     * objects in auto-commit mode is an error.
     * 
     * @throws SQLException
     *             if there is a problem accessing the database or if the target
     *             connection instance is in auto-commit mode.
     */
    public void commit() throws SQLException;

    /**
     * Returns a new instance of <code>Statement</code> for issuing SQL
     * commands to the remote database.
     * <p>
     * ResultSets generated by the returned Statement will default to type
     * <code>TYPE_FORWARD_ONLY</code> and concurrency level
     * <code>CONCUR_READ_ONLY</code>.
     * 
     * @return a <code>Statement</code> object with default settings.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public Statement createStatement() throws SQLException;

    /**
     * Returns a new instance of <code>Statement</code> whose associated
     * <code>ResultSet</code>s will have the characteristics specified in the
     * type, concurrency and holdability arguments.
     * 
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            </ul>
     * @return a new instance of <code>Statement</code> capable of
     *         manufacturing <code>ResultSet</code>s that satisfy the
     *         specified <code>resultSetType</code> and
     *         <code>resultSetConcurrency</code> values.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException;

    /**
     * Returns a new instance of <code>Statement</code> whose associated
     * <code>ResultSet</code>s will have the characteristics specified in the
     * type, concurrency and holdability arguments.
     * 
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            </ul>
     * @param resultSetHoldability
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#HOLD_CURSORS_OVER_COMMIT}
     *            <li>{@link ResultSet#CLOSE_CURSORS_AT_COMMIT}
     *            </ul>
     * @return a new instance of <code>Statement</code> capable of
     *         manufacturing <code>ResultSet</code>s that satisfy the
     *         specified <code>resultSetType</code>,
     *         <code>resultSetConcurrency</code> and
     *         <code>resultSetHoldability</code> values.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public Statement createStatement(int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException;

    /**
     * Returns a boolean indication of whether or not this connection is in the
     * auto-commit operating mode.
     * 
     * @return <code>true</code> if auto-commit is on, otherwise
     *         <code>false</code>
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public boolean getAutoCommit() throws SQLException;

    /**
     * Gets this Connection object's current catalog name.
     * 
     * @return the catalog name. <code>null</code> if there is no catalog
     *         name.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public String getCatalog() throws SQLException;

    /**
     * Returns the kind of holdability that any <code>ResultSet</code>s made
     * from this instance will have.
     * 
     * @return one of :
     *         <ul>
     *         <li>{@link ResultSet#HOLD_CURSORS_OVER_COMMIT}
     *         <li>{@link ResultSet#CLOSE_CURSORS_AT_COMMIT}
     *         </ul>
     * @throws SQLException
     *             if there is a problem accessing the a database
     */
    public int getHoldability() throws SQLException;

    /**
     * Gets the metadata about the database referenced by this connection. The
     * returned <code>DatabaseMetaData</code> describes the database
     * topography, available stored procedures, SQL syntax and so on.
     * 
     * @return a <code>DatabaseMetaData</code> object containing the database
     *         description
     * @throws SQLException
     *             if there is a problem accessing the a database
     */
    public DatabaseMetaData getMetaData() throws SQLException;

    /**
     * Returns the present value of transaction isolation for this Connection
     * instance.
     * 
     * @return the transaction isolation value
     * @throws SQLException
     *             if there is a problem accessing the database
     * @see #TRANSACTION_NONE
     * @see #TRANSACTION_READ_COMMITTED
     * @see #TRANSACTION_READ_UNCOMMITTED
     * @see #TRANSACTION_REPEATABLE_READ
     * @see #TRANSACTION_SERIALIZABLE
     */
    public int getTransactionIsolation() throws SQLException;

    /**
     * Gets the first instance of any <code>SQLWarning</code> objects that may
     * have been created in the use of this connection. If at least one warning
     * has occurred then this operation returns the first one reported. A
     * <code>null</code> indicates that no warnings have occurred.
     * <p>
     * By invoking the {@link SQLWarning#getNextWarning()} method of the
     * returned <code>SQLWarning</code> object it is possible to obtain all
     * warning objects.
     * 
     * @return the first warning as an SQLWarning object (may be
     *         <code>null</code>)
     * @throws SQLException
     *             if there is a problem accessing the database or if the call
     *             has been made on a connection which has been previously
     *             closed.
     */
    public SQLWarning getWarnings() throws SQLException;

    /**
     * Returns a boolean indication of whether or not this connection is in the
     * closed state. The closed state may be entered into as a consequence of a
     * successful invocation of the {@link #close()} method or else if an error
     * has occurred that prevents the connection from functioning normally.
     * 
     * @return <code>true</code> if closed, otherwise <code>false</code>
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public boolean isClosed() throws SQLException;

    /**
     * Returns a boolean indication of whether or not this connection is
     * currently in read-only state.
     * 
     * @return <code>true</code> if in read-only state, otherwise
     *         <code>false</code>.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public boolean isReadOnly() throws SQLException;

    /**
     * Returns a string representation of the input SQL statement
     * <code>sql</code> expressed in the underlying system's native SQL
     * syntax.
     * 
     * @param sql
     *            the JDBC form of an SQL statement.
     * @return the SQL statement in native database format.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public String nativeSQL(String sql) throws SQLException;

    /**
     * Returns a new instance of <code>CallableStatement</code> that may be
     * used for making stored procedure calls to the database.
     * 
     * @param sql
     *            the SQL statement that calls the stored function
     * @return a new instance of <code>CallableStatement</code> representing
     *         the SQL statement. <code>ResultSet</code>s emitted from this
     *         <code>CallableStatement</code> will default to type
     *         {@link ResultSet#TYPE_FORWARD_ONLY} and concurrency
     *         {@link ResultSet#CONCUR_READ_ONLY}.
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public CallableStatement prepareCall(String sql) throws SQLException;

    /**
     * Returns a new instance of <code>CallableStatement</code> that may be
     * used for making stored procedure calls to the database.
     * <code>ResultSet</code>s emitted from this
     * <code>CallableStatement</code> will satisfy the specified
     * <code>resultSetType</code> and <code>resultSetConcurrency</code>
     * values.
     * 
     * @param sql
     *            the SQL statement
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            </ul>
     * @return a new instance of <code>CallableStatement</code> representing
     *         the precompiled SQL statement. <code>ResultSet</code>s emitted
     *         from this <code>CallableStatement</code> will satisfy the
     *         specified <code>resultSetType</code> and
     *         <code>resultSetConcurrency</code> values.
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException;

    /**
     * Returns a new instance of <code>CallableStatement</code> that may be
     * used for making stored procedure calls to the database. ResultSets
     * created from this <code>CallableStatement</code> will have
     * characteristics determined by the specified type, concurrency and
     * holdability arguments.
     * 
     * @param sql
     *            the SQL statement
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            </ul>
     * @param resultSetHoldability
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#HOLD_CURSORS_OVER_COMMIT}
     *            <li>{@link ResultSet#CLOSE_CURSORS_AT_COMMIT}
     *            </ul>
     * @return a new instance of <code>CallableStatement</code> representing
     *         the precompiled SQL statement. <code>ResultSet</code>s emitted
     *         from this <code>CallableStatement</code> will satisfy the
     *         specified <code>resultSetType</code>,
     *         <code>resultSetConcurrency</code> and
     *         <code>resultSetHoldability</code> values.
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public CallableStatement prepareCall(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException;

    /**
     * Returns a new instance of <code>PreparedStatement</code> that may be
     * used any number of times to execute parameterized requests on the
     * database server.
     * <p>
     * Subject to JDBC driver support, this operation will attempt to send the
     * precompiled version of the statement to the database. Alternatively, if
     * the driver is not capable of flowing precompiled statements, the
     * statement will not reach the database server until it is executed. This
     * will have a bearing on precisely when <code>SQLException</code>
     * instances get raised.
     * <p>
     * By default, ResultSets from the returned object will be
     * {@link ResultSet#TYPE_FORWARD_ONLY} type with a
     * {@link ResultSet#CONCUR_READ_ONLY} mode of concurrency.
     * 
     * @param sql
     *            the SQL statement.
     * @return the PreparedStatement containing the supplied SQL statement
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public PreparedStatement prepareStatement(String sql) throws SQLException;

    /**
     * Creates a default PreparedStatement that can retrieve automatically
     * generated keys. Parameter <code>autoGeneratedKeys</code> may be used to
     * specify to the driver if such keys should be made accessible. This is
     * only the case when <code>sql</code> is an insert statement.
     * <p>
     * An SQL statement which may have IN parameters can be stored and
     * precompiled in a PreparedStatement. The PreparedStatement can then be
     * used to execute the statement multiple times in an efficient way.
     * <p>
     * Subject to JDBC driver support, this operation will attempt to send the
     * precompiled version of the statement to the database. Alternatively, if
     * the driver is not capable of flowing precompiled statements, the
     * statement will not reach the database server until it is executed. This
     * will have a bearing on precisely when <code>SQLException</code>
     * instances get raised.
     * <p>
     * By default, ResultSets from the returned object will be
     * {@link ResultSet#TYPE_FORWARD_ONLY} type with a
     * {@link ResultSet#CONCUR_READ_ONLY} mode of concurrency.
     * 
     * @param sql
     *            the SQL statement.
     * @param autoGeneratedKeys
     *            one of :
     *            <ul>
     *            <li>{@link Statement#RETURN_GENERATED_KEYS}
     *            <li>{@link Statement#NO_GENERATED_KEYS}
     *            </ul>
     * @return a new <code>PreparedStatement</code> instance representing the
     *         input SQL statement.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException;

    /**
     * Creates a default PreparedStatement that can retrieve the auto-generated
     * keys designated by a supplied array. If <code>sql</code> is an SQL
     * <code>INSERT</code> statement, parameter <code>columnIndexes</code>
     * is expected to hold the index values for each column in the statement's
     * intended database table containing the autogenerated-keys of interest.
     * Otherwise <code>columnIndexes</code> is ignored.
     * <p>
     * Subject to JDBC driver support, this operation will attempt to send the
     * precompiled version of the statement to the database. Alternatively, if
     * the driver is not capable of flowing precompiled statements, the
     * statement will not reach the database server until it is executed. This
     * will have a bearing on precisely when <code>SQLException</code>
     * instances get raised.
     * <p>
     * By default, ResultSets from the returned object will be
     * {@link ResultSet#TYPE_FORWARD_ONLY} type with a
     * {@link ResultSet#CONCUR_READ_ONLY} mode of concurrency.
     * 
     * @param sql
     *            the SQL statement.
     * @param columnIndexes
     *            the indexes of the columns for which auto-generated keys
     *            should be made available.
     * @return the PreparedStatement containing the supplied SQL statement
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException;

    /**
     * Creates a PreparedStatement that generates ResultSets with the specified
     * values of <code>resultSetType</code> and
     * <code>resultSetConcurrency</code>.
     * 
     * @param sql
     *            the SQL statement. It can contain one or more '?' IN parameter
     *            placeholders
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            </ul>
     * @return a new instance of <code>PreparedStatement</code> containing the
     *         SQL statement <code>sql</code>. <code>ResultSet</code>s
     *         emitted from this <code>PreparedStatement</code> will satisfy
     *         the specified <code>resultSetType</code> and
     *         <code>resultSetConcurrency</code> values.
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency) throws SQLException;

    /**
     * Creates a PreparedStatement that generates ResultSets with the specified
     * type, concurrency and holdability
     * 
     * @param sql
     *            the SQL statement. It can contain one or more '?' IN parameter
     *            placeholders
     * @param resultSetType
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#TYPE_SCROLL_SENSITIVE}
     *            <li>{@link ResultSet#TYPE_SCROLL_INSENSITIVE}
     *            <li>{@link ResultSet#TYPE_FORWARD_ONLY}
     *            </ul>
     * @param resultSetConcurrency
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CONCUR_READ_ONLY}
     *            <li>{@link ResultSet#CONCUR_UPDATABLE}
     *            </ul>
     * @param resultSetHoldability
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#HOLD_CURSORS_OVER_COMMIT}
     *            <li>{@link ResultSet#CLOSE_CURSORS_AT_COMMIT}
     *            </ul>
     * 
     * @return a new instance of <code>PreparedStatement</code> containing the
     *         SQL statement <code>sql</code>. <code>ResultSet</code>s
     *         emitted from this <code>PreparedStatement</code> will satisfy
     *         the specified <code>resultSetType</code>,
     *         <code>resultSetConcurrency</code> and
     *         <code>resultSetHoldability</code> values.
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public PreparedStatement prepareStatement(String sql, int resultSetType,
            int resultSetConcurrency, int resultSetHoldability)
            throws SQLException;

    /**
     * Creates a default PreparedStatement that can retrieve the auto-generated
     * keys designated by a supplied array. If <code>sql</code> is an SQL
     * <code>INSERT</code> statement, <code>columnNames</code> is expected
     * to hold the names of each column in the statement's associated database
     * table containing the autogenerated-keys of interest. Otherwise
     * <code>columnNames</code> is ignored.
     * <p>
     * Subject to JDBC driver support, this operation will attempt to send the
     * precompiled version of the statement to the database. Alternatively, if
     * the driver is not capable of flowing precompiled statements, the
     * statement will not reach the database server until it is executed. This
     * will have a bearing on precisely when <code>SQLException</code>
     * instances get raised.
     * <p>
     * By default, ResultSets from the returned object will be
     * {@link ResultSet#TYPE_FORWARD_ONLY} type with a
     * {@link ResultSet#CONCUR_READ_ONLY} mode of concurrency.
     * 
     * @param sql
     *            the SQL statement.
     * @param columnNames
     *            the names of the columns for which auto-generated keys should
     *            be made available.
     * @return the PreparedStatement containing the supplied SQL statement
     * @throws SQLException
     *             if a problem occurs accessing the database
     */
    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException;

    /**
     * Releases <code>savepoint</code> from the present transaction. Once
     * removed, the <code>Savepoint</code> is considered invalid and should
     * not be referenced further.
     * 
     * @param savepoint
     *            the object targeted for removal
     * @throws SQLException
     *             if there is a problem with accessing the database or if
     *             <code>savepoint</code> is considered not valid in this
     *             transaction.
     */
    public void releaseSavepoint(Savepoint savepoint) throws SQLException;

    /**
     * Rolls back all updates made so far in this transaction as well as
     * relinquishing all acquired database locks. It is an error to invoke this
     * operation when in auto-commit mode.
     * 
     * @throws SQLException
     *             if there is a problem with the database or if the method is
     *             called while in auto-commit mode of operation.
     */
    public void rollback() throws SQLException;

    /**
     * Undoes all changes made after the supplied Savepoint object was set. This
     * method should only be used when auto-commit mode is disabled.
     * 
     * @param savepoint
     *            the Savepoint to roll back to
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void rollback(Savepoint savepoint) throws SQLException;

    /**
     * Sets this connection's auto-commit mode on or off.
     * <p>
     * Putting a Connection into auto-commit mode means that all associated SQL
     * statements will be run and committed in their own separate transactions.
     * Alternatively, auto-commit set to off means that associated SQL
     * statements get grouped into transactions that need to be completed by
     * explicit calls to either the {@link #commit()} or {@link #rollback()}
     * methods.
     * <p>
     * Auto-commit is the default mode for new connection instances.
     * <p>
     * When in this mode, commits will automatically occur upon successful SQL
     * statement completion or upon successful completion of an execute.
     * Statements are not considered successfully complete until all associated
     * <code>ResultSet</code>s and output parameters have been obtained or
     * closed.
     * <p>
     * Calling this operation during an uncommitted transaction will result in
     * it being committed.
     * 
     * @param autoCommit
     *            boolean indication of whether to put the target connection
     *            into auto-commit mode (<code>true</code>) or not (<code>false</code>)
     * 
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void setAutoCommit(boolean autoCommit) throws SQLException;

    /**
     * Sets the catalog name for this connection. This is used to select a
     * subspace of the database for future work. If the driver does not support
     * catalog names, this method is ignored.
     * 
     * @param catalog
     *            the catalog name to use.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void setCatalog(String catalog) throws SQLException;

    /**
     * Sets the holdability of ResultSets created by this Connection.
     * 
     * @param holdability
     *            one of :
     *            <ul>
     *            <li>{@link ResultSet#CLOSE_CURSORS_AT_COMMIT}
     *            <li>{@link ResultSet#HOLD_CURSORS_OVER_COMMIT}
     *            <li>
     *            </ul>
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void setHoldability(int holdability) throws SQLException;

    /**
     * Sets this connection to read-only mode.
     * <p>
     * This serves as a hint to the driver, which can enable database
     * optimizations.
     * 
     * @param readOnly
     *            true to set the Connection to read only mode. false disables
     *            read-only mode
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public void setReadOnly(boolean readOnly) throws SQLException;

    /**
     * Creates an unnamed Savepoint in the current transaction.
     * 
     * @return a Savepoint object for this savepoint.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public Savepoint setSavepoint() throws SQLException;

    /**
     * Creates a named Savepoint in the current transaction.
     * 
     * @param name
     *            the name to use for the new Savepoint.
     * @return a Savepoint object for this savepoint.
     * @throws SQLException
     *             if there is a problem accessing the database
     */
    public Savepoint setSavepoint(String name) throws SQLException;

    /**
     * Sets the transaction isolation level for this Connection.
     * <p>
     * If this method is called during a transaction, the results are
     * implementation defined.
     * 
     * @param level
     *            the new transaction isolation level to use from the following
     *            list of possible values :
     *            <ul>
     *            <li>{@link #TRANSACTION_READ_COMMITTED}
     *            <li>{@link #TRANSACTION_READ_UNCOMMITTED}
     *            <li>{@link #TRANSACTION_REPEATABLE_READ}
     *            <li>{@link #TRANSACTION_SERIALIZABLE}
     *            </ul>
     * @throws SQLException
     *             if there is a problem with the database or if the value of
     *             <code>level</code> is not one of the expected constant
     *             values.
     */
    public void setTransactionIsolation(int level) throws SQLException;

}
